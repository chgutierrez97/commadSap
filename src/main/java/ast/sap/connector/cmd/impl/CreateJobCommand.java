package ast.sap.connector.cmd.impl;

import java.util.List;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.job.JobCreateData;
import ast.sap.connector.job.create.JobCreator;
import ast.sap.connector.job.create.NewJobData;
import ast.sap.connector.job.create.StepVariantPair;
import ast.sap.connector.xmi.XmiLoginData;

public class CreateJobCommand extends SapXmiCommand {
	private final JobCreateData jobData;
	private List<StepVariantPair> steps;

	public CreateJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobCreateData jobData, List<StepVariantPair> steps) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.steps = steps;
	}

	@Override
	protected SapCommandResult perform() {
		JobCreator jobCreator = new JobCreator(repository());
		NewJobData newJobData = jobCreator.createJob(jobData, steps);
		return new SapCommandResult(newJobData);
	}
}
