package ast.sap.connector.job.track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Monitor de status de jobs.
 * 
 * @author martin
 *
 */
public class JobTracker {
	public static final Logger LOGGER = LoggerFactory.getLogger(JobTracker.class);

	private final SapRepository sapRepository;

	public JobTracker(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	/**
	 * Obtiene el estado de un job.
	 * 
	 * @see http://www.sapdatasheet.org/abap/func/bapi_xbp_job_status_get.html
	 * 
	 * @param jobData
	 *            - Informacion del job a monitorear.
	 * @return Estado del job.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public JobStatus getStatus(JobTrackData jobData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		LOGGER.debug("Obteniendo status de job {} - {}", jobData.getJobId(), jobData.getJobName());

		SapFunction function = sapRepository.getFunction("BAPI_XBP_JOB_STATUS_GET")
				.setInParameter("JOBNAME", jobData.getJobName())
				.setInParameter("JOBCOUNT", jobData.getJobId())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());

		SapFunctionResult result = function.execute();
		String status = result.getOutParameterValue("STATUS").toString();
		SapStruct ret = result.getStructure("RETURN");

		SapBapiret2 bapiRet = new SapBapiret2(ret);
		LOGGER.debug("Estado de job: {} ; retorno: {}|{}", status, bapiRet.getType(), bapiRet.getMessage());

		return new JobStatus(status, bapiRet);
	}

	/**
	 * Obtiene el estado completo de un job. Se entiende por estado completo a su codigo de estado final y a su log.
	 * 
	 * @param jobData
	 *            Datos del job a obtener el estado.
	 * @return Estado completo del job.
	 * @throws FunctionGetFailException
	 *             En caso que no sea posible obtener la funcion de sap necesaria para monitorear el estado del job.
	 * @throws FunctionExecuteException
	 *             En caso que la ejecucion de la funcion falle.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public JobFullStatus getFullStatus(JobTrackData jobData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		JobStatus jobStatus = getStatus(jobData);

		JoblogReader joblogReader = new JoblogReader(sapRepository);
		String jobName = jobData.getJobName();
		String jobId = jobData.getJobId();
		String externalUsername = jobData.getExternalUsername();
		JobLog jobLog = joblogReader.readLog(new JoblogReadData(jobName, jobId, externalUsername));

		return new JobFullStatus(jobLog, jobStatus);
	}

	public static JobTracker build(SapRepository sapRepository) {
		return new JobTracker(sapRepository);
	}
}
