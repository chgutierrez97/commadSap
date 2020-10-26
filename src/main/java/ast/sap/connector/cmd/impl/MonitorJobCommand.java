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
import ast.sap.connector.job.run.JobRunner;
import ast.sap.connector.job.run.SmartJobRunner;
import ast.sap.connector.job.track.JobFullStatus;
import ast.sap.connector.job.track.JobMonitor;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Comando de lanzamiento y seguimiento de jobs.
 * 
 * Ejecutara un job hasta su finalizacion.
 * 
 * @author martin.zaragoza
 *
 */
public class MonitorJobCommand extends SapXmiCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorJobCommand.class);
	private JobRunData jobData;
	private Optional<StepVariantValuesTuple> stepVariantValues = Optional.absent();

	public MonitorJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData, StepVariantValuesTuple stepVariantValues) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.stepVariantValues = Optional.fromNullable(stepVariantValues);
	}

	public MonitorJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
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

		JobRunner jobRunner = new SmartJobRunner(repository);
		LOGGER.debug("Corriendo job " + jobData);
		SapBapiret2 runRet = jobRunner.runJob(jobData);

		if (errorArised(runRet)) {
			LOGGER.error("Ocurrio un error al disparar la tarea: " + runRet.getMessage());
			return new SapCommandResult(runRet);
		}

		JobMonitor jobMonitor = new JobMonitor(repository);
		JobFullStatus jobFullStatus = jobMonitor.monitorJob(jobData, Configuration.printContinuously());
		return new SapCommandResult(jobFullStatus);
	}

	private boolean errorArised(SapBapiret2 runRet) {
		/* SI EL TYPE DEL BAPIRET2 ES 'E' ENTONCES OCURRIO UN ERROR. */
		return runRet.hasError();
	}
}
