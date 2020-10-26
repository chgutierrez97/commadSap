package ast.sap.connector.job.run;

import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobRunData;

/**
 * Ejecutor de JOBS.
 * 
 * @author martin
 *
 */
public interface JobRunner {

	/**
	 * Ejecuta un JOB.
	 * 
	 * @see https://www.sapdatasheet.org/abap/func/bapi_xbp_job_start_asap.html
	 * @see https://www.sapdatasheet.org/abap/func/sxmi_xbp_job_start_immediately.html
	 * 
	 * @param jobData
	 *            - Informacion del job a correr.
	 * @return Resultado del job. Estructura {@link SapBapiret2}.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	SapBapiret2 runJob(JobRunData jobData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException;

}