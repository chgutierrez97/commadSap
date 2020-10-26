package ast.sap.connector.job.create;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobCreateData;

/**
 * Creador de jobs de sap.
 * 
 * @author martin.zaragoza
 *
 */
public class JobCreator {
	private final SapRepository sapRepository;

	public JobCreator(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	/**
	 * Crea un job de un solo step.
	 * 
	 * @param jobData
	 *            Informacion del job a crear.
	 * @param singleStep
	 *            Step del job.
	 * @return Datos del nuevo job creado.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public NewJobData createJob(JobCreateData jobData, StepVariantPair singleStep) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		return this.createJob(jobData, Collections.singletonList(singleStep));
	}

	/**
	 * Crea un job.
	 * 
	 * @param jobData
	 *            Informacion del job a crear.
	 * @param steps
	 *            Steps del job.
	 * @return Datos del nuevo job creado.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 */
	public NewJobData createJob(JobCreateData jobData, List<StepVariantPair> steps) throws FunctionGetFailException, FunctionExecuteException {
		String jobName = jobData.getJobName();
		String externalUsername = jobData.getExternalUsername();

		/* CREACION DEL JOB */
		SapFunction openFunction = sapRepository.getFunction("BAPI_XBP_JOB_OPEN")
				.setInParameter("JOBNAME", jobName)
				.setInParameter("EXTERNAL_USER_NAME", externalUsername);

		SapFunctionResult openResult = openFunction.execute();
		SapBapiret2 openRet = new SapBapiret2(openResult.getStructure("RETURN"));

		if (openRet.hasError()) {
			System.err.println("Ocurrio un error al crear el job " + jobData);
			return new NewJobData("", openRet);
		}

		String jobCount = openResult.getOutParameterValue("JOBCOUNT").toString();

		/* AGREGADO DE STEPS Y VARIANTES AL JOB */
		for (StepVariantPair step : steps) {
			SapFunction addStepFunction = sapRepository.getFunction("BAPI_XBP_JOB_ADD_ABAP_STEP")
					.setInParameter("JOBNAME", jobName)
					.setInParameter("JOBCOUNT", jobCount)
					.setInParameter("EXTERNAL_USER_NAME", externalUsername)
					.setInParameter("ABAP_PROGRAM_NAME", step.getProgram());

			Optional<String> variant = step.getVariant();
			if (variant.isPresent()) {
				addStepFunction.setInParameter("ABAP_VARIANT_NAME", variant.get());
			}

			SapFunctionResult addStepResult = addStepFunction.execute();
			SapBapiret2 addStepRet = new SapBapiret2(addStepResult.getStructure("RETURN"));
			if (addStepRet.hasError()) {
				System.err.println("Ocurrio un error al agregar el step: " + step);
				return new NewJobData("", addStepRet);
			}
		}

		/* CIERRE DE LA DEFINICION DEL JOB */
		SapFunction closeFunction = sapRepository.getFunction("BAPI_XBP_JOB_CLOSE")
				.setInParameter("JOBNAME", jobName)
				.setInParameter("JOBCOUNT", jobCount)
				.setInParameter("EXTERNAL_USER_NAME", externalUsername);

		SapFunctionResult closeResult = closeFunction.execute();
		SapBapiret2 closeRet = new SapBapiret2(closeResult.getStructure("RETURN"));
		if (closeRet.hasError()) System.err.println("Ocurrio un error al cerrar el job " + jobData);

		return new NewJobData(jobCount, closeRet);
	}
}
