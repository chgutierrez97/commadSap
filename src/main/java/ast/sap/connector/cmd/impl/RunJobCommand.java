package ast.sap.connector.cmd.impl;

import com.google.common.base.Optional;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.cmd.VariantChangerHelper;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.run.JobRunner;
import ast.sap.connector.job.run.SmartJobRunner;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Comando de disparo de jobs (sin monitoreo).
 * 
 * @author martin.zaragoza
 *
 */
public class RunJobCommand extends SapXmiCommand {
	private JobRunData jobData;
	private Optional<StepVariantValuesTuple> stepVariantValues = Optional.absent();

	public RunJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData, StepVariantValuesTuple stepVariantValues) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.stepVariantValues = Optional.fromNullable(stepVariantValues);
	}

	public RunJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	public SapCommandResult perform() {
		SapRepository repository = repository();

		/* Si se indico un valor de variante a modificar, se lo modifica */
		if (stepVariantValues.isPresent()) {
			StepVariantValuesTuple stepVariantTuple = stepVariantValues.get();
			if (stepVariantTuple.getVariant().isPresent() && stepVariantTuple.getVariantValuePairs().isPresent()) {
				SapBapiret2 changeVariantRet = VariantChangerHelper.INSTANCE.changeVariant(repository,jobData.getExternalUsername(), stepVariantTuple);
				if (changeVariantRet.hasError()) return new SapCommandResult(changeVariantRet);
			}
		}

		JobRunner jobRunner = new SmartJobRunner(repository);
		SapBapiret2 ret = jobRunner.runJob(jobData);
		return new SapCommandResult(ret);
	}
}
