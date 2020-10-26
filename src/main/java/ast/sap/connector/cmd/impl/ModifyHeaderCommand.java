package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.mod.JobHeaderModifier;
import ast.sap.connector.xmi.XmiLoginData;

@Deprecated
public class ModifyHeaderCommand extends SapXmiCommand {

	private JobRunData jobRunData;

	public ModifyHeaderCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobRunData) {
		super(sapRepository, xmiLoginData);
		this.jobRunData = jobRunData;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();
		JobHeaderModifier jobHeaderModifier = new JobHeaderModifier(repository);
		SapBapiret2 bapiRet2 = jobHeaderModifier.modifyHeader(jobRunData);
		return new SapCommandResult(bapiRet2);
	}

}
