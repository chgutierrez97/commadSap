package ast.sap.connector.chain.monitor;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.processes.ChainProcessBundle;
import ast.sap.connector.chain.processes.ChainProcessesReader;
import ast.sap.connector.chain.processes.ProcessEntry;
import ast.sap.connector.chain.processes.ProcessLogPair;
import ast.sap.connector.chain.status.ChainStatus;
import ast.sap.connector.chain.status.ChainStatusReader;
import ast.sap.connector.chain.status.ChainStatusReaderInput;
import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobTrackData;
import ast.sap.connector.job.track.JobFullStatus;
import ast.sap.connector.job.track.JobTracker;
import ast.sap.connector.job.track.ReconnectFailException;
import ast.sap.connector.util.Connector;
import ast.sap.connector.xmi.XmiSessionManager;
import ast.sap.connector.xmi.exception.XmiLoginException;
import com.sap.conn.jco.JCoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitor de cadenas en ejecucion.
 *
 * @author franco.milanese
 */
public class ChainMonitor {

	static interface SapAction<E> {
		public E act() throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException;
	}

	/**
	 * Bandera utilizada para testing. Indica si el monitor al reconectarse debe llamar a {@link Connector} o no.
	 */
	boolean doReconnect = true;

	/**
	 * Bandera utilizada para testing. Indica si el monitor al dormir debe llamar a Thread.sleep.
	 */
	boolean doSleep = true;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChainMonitor.class);
	private static final String RSPC_PROCESS_SUFFIX = "BI_PROCESS_";
	private SapRepository sapRepository;

	public ChainMonitor(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	private String buildJobName(String processType) {
		return RSPC_PROCESS_SUFFIX + processType;
	}

	ChainMonitor noSleep() {
		this.doSleep = false;
		return this;
	}

	private void sleep() {
		try {
			if (this.doSleep) { Thread.sleep(500); }
		} catch (InterruptedException e) {}
	}

	/**
	 * Monitorea el estado de una cadena hasta que la misma termine. Obtiene el estado de la misma como tambien, el estado y logs de sus procesos asociados.
	 *
	 * @param chainData - ChainId y LogId de la cadena a monitorear
	 * @param username  - Usuario que ejecuta el monitoreo
	 * @return ChainFullStatus - Estado de la cadena. - Estado y logs de los procesos asociados a la misma
	 */
	public ChainFullStatus monitorChain(ChainData chainData, String username) {
		LOGGER.info("Monitoreando la cadena {} - {}", chainData.getChain(), chainData.getLogId());
		ChainStatus chainStatus;
		do {
			ChainStatusReaderInput chainStatusReaderInput = new ChainStatusReaderInput(chainData.getChain(), chainData.getLogId());
			chainStatus = readChainStatus(chainStatusReaderInput);

			// Si la cadena no termino de ejecutar entonces mando a dormir el thread
			if (chainStatus.getStatus().notFinished()) { sleep(); }
		} while (chainStatus.getStatus().notFinished());

		LOGGER.info("Estado de la ejecucion de la cadena {} ", chainStatus.getStatus().toString());
		// Obtengo los procesos de la cadena una vez terminada la ejecucion
		List<ProcessEntry> chainProcesses = getChainProcesses(chainData).getProcesses();

		// Por cada proceso imprimo los logs de la cadena
		List<ProcessLogPair> processLogPairs = new ArrayList<ProcessLogPair>();
		LOGGER.info("Cantidad total de procesos de la cadena {} - {}", chainData.getChain(), chainProcesses.size());

		for (ProcessEntry processEntry : chainProcesses) {
			String jobName = buildJobName(processEntry.getType());

			JobTrackData jobData = JobData.newJobTrackData(jobName, username, processEntry.getJobCount());
			JobFullStatus jobFullStatus = getJobFullStatus(jobData);

			ProcessLogPair processLogPair = new ProcessLogPair(processEntry, jobFullStatus);
			processLogPairs.add(processLogPair);
			
			/* Imprimo en la terminal el log del job */
			System.out.println(
					String.format(
							"%s::%s(id:%s)------------------------------------------------------------------------------------------------------------------",
							processEntry.getChainId(), jobData.getJobName(), jobData.getJobId()));
			LOGGER.debug(
					"{}::{}(id:{})------------------------------------------------------------------------------------------------------------------",
					processEntry.getChainId(), jobData.getJobName(), jobData.getJobId());
			processLogPair.getJobLog().printLogStdout();
		}

		return new ChainFullStatus(chainStatus, processLogPairs);
	}

	private JobFullStatus getJobFullStatus(final JobTrackData jobData) {
		return doAndReconnect(new SapAction<JobFullStatus>() {

			@Override
			public JobFullStatus act() throws FunctionGetFailException, FunctionExecuteException {
				return JobTracker.build(sapRepository).getFullStatus(jobData);
			}

		}, String.format("Error al obtener el estado completo del proceso %s:%s", jobData.getJobName(), jobData.getJobId()));
	}

	private ChainProcessBundle getChainProcesses(final ChainData chainData) {
		return doAndReconnect(new SapAction<ChainProcessBundle>() {

			@Override
			public ChainProcessBundle act() throws FunctionGetFailException {
				return ChainProcessesReader.build(sapRepository).readProcesses(chainData);
			}
		}, String.format("Error al obtener el listado de procesos de la cadena %s, en su ejecucion %s", chainData.getChain(), chainData.getLogId()));
	}

	private ChainStatus readChainStatus(final ChainStatusReaderInput chainStatusReaderInput) {
		return doAndReconnect(new SapAction<ChainStatus>() {
			@Override
			public ChainStatus act() throws FunctionGetFailException {
				return ChainStatusReader.build(sapRepository).readChainStatus(chainStatusReaderInput);
			}
		}, String.format("Error al obtener el status de la cadena %s, en su ejecucion %s", chainStatusReaderInput.getChainId(),
				chainStatusReaderInput.getLogId()));
	}

	/**
	 * Realiza una accion e intenta reestablecer la conexion en caso de error.
	 *
	 * @param action Accion a realizar contra el server sap.
	 * @param errMsg Mensaje de error a mostrar en caso de falla de conexion.
	 * @return Resultado de la accion.
	 * @throws ReconnectFailException En caso que se haya superado la cantidad de intentos de reconexion.
	 */
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
	 * @throws ReconnectFailException En caso que se haya superado la cantidad de intentos de reconexion.
	 */
	void reconnect() throws ReconnectFailException {
		reconnect(0, Configuration.getReconnectMaxTries());
	}

	/**
	 * Intenta reestablecer la sesion con sap. El numero de intentos esta determinado por {@link Configuration#getReconnectMaxTries()}.
	 *
	 * @throws ReconnectFailException En caso que se haya superado la cantidad de intentos de reconexion.
	 */
	void reconnect(int tryCount, int maxTries, Exception... lastEx) throws ReconnectFailException {
		if (tryCount >= maxTries) {
			if (lastEx.length == 0) { throw new ReconnectFailException("Imposible reestablecer conexion con server SAP"); }
			else { throw new ReconnectFailException("Imposible reestablecer conexion con server SAP", lastEx[0]); }
		}

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

	private void sleep(long... time) {
		long ttime = time.length == 0 ? 500 : time[0];
		try {
			if (this.doSleep) { Thread.sleep(ttime); }
		} catch (InterruptedException e) { LOGGER.error(e.getMessage()); }
	}

}
