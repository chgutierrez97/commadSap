package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.cmd.VariantChangerHelper;
import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobCreateData;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.create.JobCreator;
import ast.sap.connector.job.create.NewJobData;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.run.JobRunner;
import ast.sap.connector.job.run.SmartJobRunner;
import ast.sap.connector.job.track.JobFullStatus;
import ast.sap.connector.job.track.JobMonitor;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Comando para la creacion y corrida y monitoreo de jobs.
 * 
 * @author martin.zaragoza
 *
 */
public class CreateMonitorJobCommand extends SapXmiCommand {
	private final JobCreateData jobData;
	private final StepVariantValuesTuple stepVariantValues;

	public CreateMonitorJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobCreateData jobData, StepVariantValuesTuple stepVariantValue) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.stepVariantValues = stepVariantValue;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();

		/* modifico los valores de la variante a asociar al programa */
		if (stepVariantValues.getVariant().isPresent() && stepVariantValues.getVariantValuePairs().isPresent()) {
			SapBapiret2 changeVariantRet = VariantChangerHelper.INSTANCE.changeVariant(repository, jobData.getExternalUsername(), stepVariantValues);
			if (changeVariantRet.hasError()) return new SapCommandResult(changeVariantRet);
		}

		/* Creo un job */
		JobCreator jobCreator = new JobCreator(repository);
		NewJobData newJobData = jobCreator.createJob(jobData, stepVariantValues);
		if (newJobData.hasError()) return new SapCommandResult(newJobData);
		
		// Corro el job creado 
		String jobCount = newJobData.getJobCount();
		JobRunner jobRunner = new SmartJobRunner(repository);
		JobData runJobData = JobData.newJobRunData(jobData.getJobName(), jobData.getExternalUsername(), jobCount);
		SapBapiret2 runRet = jobRunner.runJob(runJobData);
		if(runRet.hasError()) return new SapCommandResult(runRet);

		 //Monitoreo el job corriendo
		JobMonitor jobMonitor = new JobMonitor(repository);
		JobFullStatus monitorRet = jobMonitor.monitorJob(runJobData, Configuration.printContinuously()); 
		return new SapCommandResult(monitorRet);
	}
}