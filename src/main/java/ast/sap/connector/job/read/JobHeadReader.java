package ast.sap.connector.job.read;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobRunData;

/**
 * Lector de parametros del Job Header
 * 
 * @author franco.milanese
 *
 */
public class JobHeadReader {

	private SapRepository sapRepository;

	public JobHeadReader(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	/**
	 * 
	 * Obtiene la estructura BP20JOB de un job
	 * 
	 * @see http://www.sapdatasheet.org/abap/func/bapi_xbp_job_read.html
	 * 
	 * @param jobData
	 *            - Informacion del job a obtener
	 * 
	 * @return Estructura BP20JOB
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public JobHead readJob(JobRunData jobData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		SapFunction function = sapRepository.getFunction("BAPI_XBP_JOB_READ")
				.setInParameter("JOBNAME", jobData.getJobName()).setInParameter("JOBCOUNT", jobData.getJobId())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());

		SapFunctionResult result = function.execute();
		return new JobHead(new SapBapiret2(result.getStructure("RETURN")), new Bp20job(result.getStructure("JOBHEAD")));
	}

}
