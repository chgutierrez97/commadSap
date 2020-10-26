package ast.sap.connector.job.def;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.job.JobRunData;

public class JobDefinitionReader {

	private SapRepository sapRepository;

	public JobDefinitionReader(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	public JobDefinition readJobDefinition(JobRunData jobData) {

		SapFunction function = sapRepository.getFunction("BAPI_XBP_JOB_DEFINITION_GET")
				.setInParameter("JOBNAME", jobData.getJobName())
				.setInParameter("JOBCOUNT", jobData.getJobId())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());

		SapFunctionResult result = function.execute();
		return new JobDefinition(new SapBapiret2(result.getStructure("RETURN")), new BapiXmJob(result.getStructure("JOB_HEAD")));
	}

}
