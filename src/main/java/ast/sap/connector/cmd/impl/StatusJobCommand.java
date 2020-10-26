package ast.sap.connector.cmd.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.cmd.VariantChangerHelper;
import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.track.JobFullStatus;
import ast.sap.connector.job.track.JobMonitor;
import ast.sap.connector.job.track.JobMonitorStatus;
import ast.sap.connector.xmi.XmiLoginData;

public class StatusJobCommand extends SapXmiCommand  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatusJobCommand.class);
	private JobRunData jobData;
	private Optional<StepVariantValuesTuple> stepVariantValues = Optional.absent();
	
	public StatusJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData, StepVariantValuesTuple stepVariantValues) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.stepVariantValues = Optional.fromNullable(stepVariantValues);
	}
	
	public StatusJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
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
				SapBapiret2 changeVariantRet = VariantChangerHelper.INSTANCE.changeVariant(repository, jobData.getExternalUsername(), stepVariantTuple);
				if (changeVariantRet.hasError())
					return new SapCommandResult(changeVariantRet);
			}
		}

		

		JobMonitorStatus jobMonitorStatus = new JobMonitorStatus(repository);
		JobFullStatus jobFullStatus = jobMonitorStatus.monitorJobStatus(jobData, Configuration.printContinuously());
		return new SapCommandResult(jobFullStatus);
	}

	private boolean errorArised(SapBapiret2 runRet) {
		/* SI EL TYPE DEL BAPIRET2 ES 'E' ENTONCES OCURRIO UN ERROR. */
		return runRet.hasError();
	}

}
