package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.xmi.XmiLoginData;

public class JobCountCommand extends SapXmiCommand {

	private JobRunData jobData;

	public JobCountCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();
		SapFunction function = repository.getFunction("BAPI_XBP_JOB_COUNT")
				.setInParameter("JOBNAME", jobData.getJobName())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());
		
		SapFunctionResult result = function.execute();
		System.out.println("NUMBER OF JOBS: "+result.getOutParameterValue("NUMBER_OF_JOBS"));
		System.out.println(new SapBapiret2(result.getStructure("RETURN")));
		System.out.println(result.getOutTableParameter("JOB_TABLE"));
		
		return SapCommandResult.emptyResult();
	}
}
