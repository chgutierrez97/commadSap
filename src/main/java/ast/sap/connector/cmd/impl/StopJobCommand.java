package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobTrackData;
import ast.sap.connector.job.run.JobStopper;
import ast.sap.connector.xmi.XmiLoginData;

public class StopJobCommand extends SapXmiCommand {
	private final JobTrackData jobData;

	public StopJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobTrackData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository sapRepository = repository();
		JobStopper jobStopper = new JobStopper(sapRepository);
		SapBapiret2 ret = jobStopper.stopJob(jobData);
		return new SapCommandResult(ret);
	}
}
