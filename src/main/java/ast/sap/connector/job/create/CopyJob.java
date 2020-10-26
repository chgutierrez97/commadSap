package ast.sap.connector.job.create;

import java.util.Collections;
import java.util.Date;
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

public class CopyJob {
	private final SapRepository sapRepository;
	private final String idJob;
	private final String nameNewJob;
	public CopyJob(SapRepository sapRepository,String idJob,String nameNewJob) {
		this.sapRepository = sapRepository;
		this.idJob=idJob;
		this.nameNewJob=nameNewJob;
		
	}

	public NewJobData createCopyJob(JobCreateData jobData, StepVariantPair singleStep) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		return this.createCopyJob(jobData, Collections.singletonList(singleStep));
	}

	public NewJobData createCopyJob(JobCreateData jobData, List<StepVariantPair> steps) throws FunctionGetFailException, FunctionExecuteException {
		String jobName = jobData.getJobName();
		String externalUsername = jobData.getExternalUsername();

		/* CLONACION DEL JOB */
	
		SapFunction copyJobFunction = sapRepository.getFunction("BAPI_XBP_JOB_COPY")
				.setInParameter("SOURCE_JOBCOUNT",idJob)
				.setInParameter("SOURCE_JOBNAME",jobName)
				.setInParameter("TARGET_JOBNAME",this.nameNewJob)
				.setInParameter("EXTERNAL_USER_NAME", externalUsername)
				.setInParameter("STEP_NUMBER", 0);
		 SapFunctionResult copyResult = copyJobFunction.execute();
		 
		 String jobCount = copyResult.getOutParameterValue("TARGET_JOBCOUNT").toString();
		 SapBapiret2 copyRet = new SapBapiret2(copyResult.getStructure("RETURN"));

	 	return new NewJobData(jobCount, copyRet);
	}
	
}
