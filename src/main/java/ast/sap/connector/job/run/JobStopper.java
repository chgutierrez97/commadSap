package ast.sap.connector.job.run;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobTrackData;


/**
 * Permite detener / matar un job en ejecucion.
 * 
 * @author martin.zaragoza
 *
 */
public final class JobStopper {
	private final SapRepository sapRepository;

	public JobStopper(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}
	
	/**
	 * Detiene un job en curso.
	 * 
	 * @see https://www.sapdatasheet.org/abap/func/bapi_xbp_job_abort.html
	 * 
	 * @param jobData - Informacion sobre el Job a detener.
	 * @return Informacion del job detenido.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public SapBapiret2 stopJob(JobTrackData jobData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException{
		SapFunction function = sapRepository.getFunction("BAPI_XBP_JOB_ABORT")
				.setInParameter("JOBNAME", jobData.getJobName())
				.setInParameter("JOBCOUNT", jobData.getJobId())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());

		SapFunctionResult result = function.execute();
		return new SapBapiret2(result.getStructure("RETURN"));
	}
}
