package ast.sap.connector.job.track;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.JCoException;

import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobTrackData;
import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.log.JoblogReadData;
import ast.sap.connector.job.log.JoblogReadData.Direction;
import ast.sap.connector.job.log.JoblogReader;
import ast.sap.connector.job.log.LogEntry;
import ast.sap.connector.util.Connector;
import ast.sap.connector.xmi.XmiSessionManager;
import ast.sap.connector.xmi.exception.XmiLoginException;

/**
 * Monitor de jobs en ejecucion.
 * 
 * @author martin.zaragoza
 *
 */
public class JobMonitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobMonitor.class);

	static interface SapAction<E> {
		public E act() throws FunctionGetFailException, FunctionExecuteException;
	}

	private SapRepository sapRepository;
	private final int maxStatusChangeTries = 3;

	/**
	 * Bandera utilizada para testing. Indica si el monitor al dormir debe llamar a Thread.sleep.
	 */
	boolean doSleep = true;

	/**
	 * Bandera utilizada para testing. Indica si el monitor al reconectarse debe llamar a {@link Connector} o no.
	 */
	boolean doReconnect = true;

	public JobMonitor(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	/**
	 * Monitorea un job hasta que el mismo termine. Imprime el log del job a medida que se ejecuta y obtiene el log completo al finalizar la ejecucion.
	 * 
	 * @param jobData
	 *            - Informacion del job a correr y monitorear.
	 * @param printContinuously
	 *            - [OPCIONAL] true si se desea que el log se imprima continuamente. False en caso contrario. VALOR POR DEFECTO: FALSE.
	 * @return Log del job y su estado.
	 */
	public JobFullStatus monitorJob(final JobTrackData jobData, boolean... printContinuously) {
		boolean pContinuously = printContinuously.length > 0 && printContinuously[0];

		LOGGER.debug("Monitoreando job");

		int currLogLine = 0;
		boolean jobRunning = true;
		int statusChangeTries = 0;
		JobStatus jobStatus = null;

		while (jobRunning) {
			jobStatus = getJobStatus(jobData);

			JobStatusCode statusCode = jobStatus.getStatusCode();
			LOGGER.debug("statusCode: " + statusCode.label);

			while (statusCode.isReleased() && (statusChangeTries++) < maxStatusChangeTries) {
				LOGGER.debug("statusCode es released. Esperamos a que cambie (intento %d)", statusChangeTries);
				sleep();
				statusCode = getJobStatus(jobData).getStatusCode();
			}

			/* Si se habilito impresion continua -> el log del job se imprime en a medida que se monitorea */
			if (pContinuously) currLogLine = printLog(jobData, currLogLine);

			jobRunning = statusCode.isRunning();
			if (jobRunning) sleep();
		}

		final JoblogReadData jobLogReadData = new JoblogReadData(jobData.getJobName(), jobData.getJobId(), jobData.getExternalUsername());
		JobLog jobLog = getJobLog(jobLogReadData);
		/* Si NO se habilito impresion continua -> el log del job se imprime todo junto cuando la ejecucion finaliza */
		if (!pContinuously) jobLog.printLogStdout();

		printStatus(jobStatus);

		return new JobFullStatus(jobLog, jobStatus);
	}

	private void printStatus(JobStatus jobStatus) {
		System.out.println(jobStatus.getStatusCode());
	}

	/**
	 * Imprime el log del job en standard output.
	 * 
	 * @param jobData
	 *            - Informacion del job cuyo log se debe imprimir.
	 * @param currLogLine
	 *            - Linea actual de lectura del log.
	 * @return Nueva posicion de linea actual de log.
	 */
	private int printLog(JobTrackData jobData, int currLogLine) {
		// printLog__(sapRepository);
		final JoblogReadData jobLogReadData = new JoblogReadData(jobData.getJobName(), jobData.getJobId(), jobData.getExternalUsername(), Direction.BEGINNING);

		JobLog jobLog = getJobLog(jobLogReadData);

		List<LogEntry> logEntries = jobLog.getLogEntries();
		int entriesCount = logEntries.size();
		LOGGER.debug("entriesCount: " + entriesCount);

		int entryReadCount = entriesCount - currLogLine;
		LOGGER.debug("entryReadCount: " + entryReadCount);
		if (entryReadCount > 0) {
			List<LogEntry> entriesToPrint = logEntries.subList(currLogLine, entriesCount);
			for (LogEntry entry : entriesToPrint) {
				System.out.println(entry);
			}
		}

		return entriesCount;
	}

	private void sleep(long... time) {
		long ttime = time.length == 0 ? 500 : time[0];
		try {
			if (this.doSleep) Thread.sleep(ttime);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}

	private JobStatus getJobStatus(final JobTrackData jobData) {
		return doAndReconnect(new SapAction<JobStatus>() {
			@Override
			public JobStatus act() throws FunctionGetFailException {
				return JobTracker.build(sapRepository).getStatus(jobData);
			}
		}, String.format("Error al obtener el estado del job %s:%s", jobData.getJobName(), jobData.getJobId()));
	}

	private JobLog getJobLog(final JoblogReadData jobLogReadData) {
		return doAndReconnect(new SapAction<JobLog>() {
			@Override
			public JobLog act() throws FunctionGetFailException {
				return JoblogReader.build(sapRepository).readLog(jobLogReadData);
			}
		}, "Error al obtener el log del job");
	}

	/**
	 * Realiza una accion e intenta reestablecer la conexion en caso de error.
	 * 
	 * @param action
	 *            Accion a realizar contra el server sap.
	 * @param errMsg
	 *            Mensaje de error a mostrar en caso de falla de conexion.
	 * @return Resultado de la accion.
	 * @throws ReconnectFailException
	 *             En caso que se haya superado la cantidad de intentos de reconexion.
	 * 
	 */
	// TODO : LOS TIPOS DE EXCEPCION CATCHEADOS SON INCORRECTOS. VERIFICAR CUAL ES EL TIPO DE EXCEPCION ANTE ERRORES DE CONEXION
	private <E> E doAndReconnect(SapAction<E> action, String errMsg) throws ReconnectFailException {
		try {
			return action.act();
		} catch (FunctionNetworkErrorException e) {
			LOGGER.error(errMsg);
			LOGGER.info("Intentando recuperar conexion con sap");

			// Intento reestablecer la conexion con sap
			reconnect();
			// luego de haber reestablecido la conexion, ejecuto recursivamente este mismo metodo
			return doAndReconnect(action, errMsg);
		}
	}

	/**
	 * Intenta reestablecer la sesion con sap. El numero de intentos esta determinado por {@link Configuration#getReconnectMaxTries()}.
	 * 
	 * @throws ReconnectFailException
	 *             En caso que se haya superado la cantidad de intentos de reconexion.
	 */
	void reconnect() throws ReconnectFailException {
		reconnect(0, Configuration.getReconnectMaxTries());
	}

	/**
	 * Intenta reestablecer la sesion con sap. El numero de intentos esta determinado por {@link Configuration#getReconnectMaxTries()}.
	 * 
	 * @throws ReconnectFailException
	 *             En caso que se haya superado la cantidad de intentos de reconexion.
	 */
	void reconnect(int tryCount, int maxTries, Exception... lastEx) throws ReconnectFailException {
		if (tryCount >= maxTries) {
			if(lastEx.length == 0) throw new ReconnectFailException("Imposible reestablecer conexion con server SAP");
			else throw new ReconnectFailException("Imposible reestablecer conexion con server SAP", lastEx[0]);
		};

		try {
			if (doReconnect) {
				this.sapRepository = Connector.INSTANCE.loadDestination().openContext();
				XmiSessionManager.INSTANCE.reLogin(sapRepository);
			}
			LOGGER.info("Conexion con server sap reestablecida!");
			return;
		} catch (RepositoryGetFailException | JCoException | XmiLoginException e) {
			LOGGER.error("Intento de reconexion {} fallo...", tryCount);
			sleep(1000);
			reconnect(tryCount + 1, maxTries, e);
		}
	}
}
