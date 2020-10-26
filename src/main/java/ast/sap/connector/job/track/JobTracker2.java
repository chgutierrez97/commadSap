package ast.sap.connector.job.track;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobTrackData;
import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.log.JoblogReadData;
import ast.sap.connector.job.log.JoblogReader;
import ast.sap.connector.util.RootBootUtil;
import ast.sap.connector.job.track.JobStatusTable;

import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

public class JobTracker2 {

	public static final Logger LOGGER = LoggerFactory.getLogger(JobTracker.class);

	private final SapRepository sapRepository;

	RootBootUtil util = new RootBootUtil();

	public JobTracker2(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	/**
	 * Permite convertir un String en fecha (Date).
	 * 
	 * @param fecha Cadena de fecha dd/MM/yyyy
	 * @return Objeto Date
	 */
	public Date ParseFecha(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fecha);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return fechaDate;
	}

	/**
	 * Obtiene el estado de un job.
	 * 
	 * @see http://www.sapdatasheet.org/abap/func/bapi_xbp_job_status_get.html
	 * 
	 * @param jobData - Informacion del job a monitorear.
	 * @return Estado del job.
	 * @throws FunctionGetFailException      En caso que ocurra un error al obtener
	 *                                       las funciones de sap.
	 * @throws FunctionExecuteException      En caso que ocurra un error al ejecutar
	 *                                       las funciones de sap.
	 * @throws FunctionNetworkErrorException Si ocurrio un error en la red al
	 *                                       ejecutar la funcion.
	 */
	public JobStatus getStatus(JobTrackData jobData)
			throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		String paht = new File("").getAbsolutePath();
		String tabla = "";
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		Boolean flag = Boolean.FALSE;
		String status = "";
		String pahts = "";
		String fecha = "";
		String hora = "";		
		Integer codError = 0;
		
		if(!jobData.getJobId().equals("***")) {
			String fecaHora[] = jobData.getJobId().split("-");
			fecha = fecaHora[0];
			hora  = fecaHora[1];	
		}
		
		try {
			do {
				LOGGER.debug("Obteniendo status de job {} - {}", jobData.getJobId(), jobData.getJobName());
				String jobName = jobData.getJobName();
				SapFunction function;
				
				if(!jobData.getJobId().equals("***")) {
					 function = sapRepository.getFunction("Z_RFC_BP_JOB_SELECT_SM37B")
							.setInParameter("I_JOBNAME", jobName)
							.setInParameter("I_USERNAME", "*")
							.setInParameter("I_FROM_DATE", ParseFecha(fecha))
							.setInParameter("I_FROM_TIME", hora)
							.setInParameter("I_TO_DATE", util.addDays(ParseFecha(fecha), +1))
							.setInParameter("I_TO_TIME", "23:59:59")
							.setInParameter("I_PRELIM", "X")
							.setInParameter("I_SCHEDUL", "X")
							.setInParameter("I_READY", "X")
							.setInParameter("I_RUNNING", "X")
							.setInParameter("I_FINISHED", "X")
							.setInParameter("I_ABORTED", "X");
					
				}else{
					
					 function = sapRepository.getFunction("Z_RFC_BP_JOB_SELECT_SM37B")
							.setInParameter("I_JOBNAME", jobName)
							.setInParameter("I_USERNAME", "*")
							.setInParameter("I_FROM_DATE", util.addDays(new Date(), -1))
							.setInParameter("I_FROM_TIME", "00:00:00")
							.setInParameter("I_TO_DATE", util.addDays(new Date(), +1))
							.setInParameter("I_TO_TIME", "23:59:59")
							.setInParameter("I_PRELIM", "X")
							.setInParameter("I_SCHEDUL", "X")
							.setInParameter("I_READY", "X")
							.setInParameter("I_RUNNING", "X")
							.setInParameter("I_FINISHED", "X")
							.setInParameter("I_ABORTED", "X");
					
				}
				
				

				SapFunctionResult result = function.execute();
				tabla = result.getOutParameterValue("ET_JOBLIST").toString();
				//System.out.println(tabla);
				pahts = paht + "\\fileTrans" + new Date().getTime() + ".txt";
				FileWriter escritura = new FileWriter(pahts);
				for (int i = 0; i < tabla.length(); i++) {
					escritura.write(tabla.charAt(i));
				}
				escritura.close();

				archivo = new File(pahts);
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
				String linea;

				int ru = 0, fi = 0, ab = 0;
				while ((linea = br.readLine()) != null) {
					if (util.comparadorDeCaracteres(linea, jobData.getJobName().replace("*", ""))) {
						linea = linea.replaceAll(" +", "");
						String[] parts = linea.split("\\|");

						if (parts[5].equals("R")) {
							ru++;
						} else if (parts[5].equals("F")) {
							fi++;
						} else if (parts[5].equals("A")) {
							ab++;
						}

					}
				}
               // System.out.println("RU = "+ru+" FI = "+fi+" AB = "+ab);
				if (ru > 0 && fi == 0 && ab == 0) {
					flag = Boolean.TRUE;
					Thread.sleep(60000L);
				} else if (ru > 0 && fi > 0 && ab == 0) {
					flag = Boolean.TRUE;
					Thread.sleep(60000L);
				} else if (ru > 0 && fi == 0 && ab > 0) {
					flag = Boolean.TRUE;
					Thread.sleep(60000L);
				} else if (ru > 0 && fi > 0 && ab > 0) {
					flag = Boolean.TRUE;
					Thread.sleep(60000L);
				} else if (ru == 0 && fi == 0 && ab == 0) {
					flag = Boolean.FALSE;
					status = "N";
					codError = 1;
				} else if (ru==0 && fi > 0 && ab == 0) {
					flag = Boolean.FALSE;
					status = "F";
					codError = 1;
				} else if (ru==0 && fi > 0 && ab > 0) {
					flag = Boolean.FALSE;
					status = "A";
					codError = 1;
				} else if (ru==0 && ru==0 && fi == 0 && ab > 0) {
					flag = Boolean.FALSE;
					status = "A";
					codError = 2;
				} else {
					flag = Boolean.FALSE;
					status = "F";
				}
				
				fr.close();
				if (new File(pahts).delete()) {
					LOGGER.debug("El fichero fileTrans" + new Date().getTime() + ".txt ha sido borrado satisfactoriamente");
				} else {
					LOGGER.debug("El fichero fileTrans" + new Date().getTime() + ".txt no puede ser borrado");
				}

			} while (flag);


		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SapBapiret2 bapiRet = new SapBapiret2("Status error", "CodError:" + codError, 0, tabla, "logNo", 0,
				jobData.getJobName(), "messagev2", "messagev3", tabla, "parameter", 0, "field", "system");

		return new JobStatus(status, bapiRet);
	}

	/**
	 * Obtiene el estado completo de un job. Se entiende por estado completo a su
	 * codigo de estado final y a su log.
	 * 
	 * @param jobData Datos del job a obtener el estado.
	 * @return Estado completo del job.
	 * @throws FunctionGetFailException      En caso que no sea posible obtener la
	 *                                       funcion de sap necesaria para
	 *                                       monitorear el estado del job.
	 * @throws FunctionExecuteException      En caso que la ejecucion de la funcion
	 *                                       falle.
	 * @throws FunctionNetworkErrorException Si ocurrio un error en la red al
	 *                                       ejecutar la funcion.
	 */

	public JobFullStatus getFullStatus(JobTrackData jobData)
			throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		JobStatus jobStatus = getStatus(jobData);

		JoblogReader joblogReader = new JoblogReader(sapRepository);
		String jobName = jobData.getJobName();
		String jobId = jobData.getJobId();
		String externalUsername = jobData.getExternalUsername();
		JobLog jobLog = joblogReader.readLog(new JoblogReadData(jobName, jobId, externalUsername));

		return new JobFullStatus(jobLog, jobStatus);
	}

	public static JobTracker2 build(SapRepository sapRepository) {
		return new JobTracker2(sapRepository);
	}
}
