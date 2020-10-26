package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.def.JobDefinition;
import ast.sap.connector.job.def.JobDefinitionReader;
import ast.sap.connector.xmi.XmiLoginData;

public class ReadJobDefinitionCommand extends SapXmiCommand {

	private JobRunData jobData;

	public ReadJobDefinitionCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();
		JobDefinitionReader reader = new JobDefinitionReader(repository);
		JobDefinition jobDefinition = reader.readJobDefinition(jobData);
		return new SapCommandResult(jobDefinition);
	}

}
