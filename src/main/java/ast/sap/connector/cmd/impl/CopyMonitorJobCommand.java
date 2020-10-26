package ast.sap.connector.cmd.impl;

import java.util.Date;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.cmd.VariantChangerHelper;
import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobCreateData;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.create.CopyJob;
import ast.sap.connector.job.create.NewJobData;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.run.JobRunner;
import ast.sap.connector.job.run.SmartJobRunner;
import ast.sap.connector.job.track.JobFullStatus;
import ast.sap.connector.job.track.JobMonitor;
import ast.sap.connector.xmi.XmiLoginData;

public class CopyMonitorJobCommand extends SapXmiCommand{

	private final JobCreateData jobData;
	private final StepVariantValuesTuple stepVariantValues;
	private final String jobId;
	 
	
	
	public CopyMonitorJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobCreateData jobData,	StepVariantValuesTuple stepVariantValues, String jobId) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.stepVariantValues = stepVariantValues;
		this.jobId = jobId;
	}
	
	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();

		/* modifico los valores de la variante a asociar al programa */
		if (stepVariantValues.getVariant().isPresent() && stepVariantValues.getVariantValuePairs().isPresent()) {
			SapBapiret2 changeVariantRet = VariantChangerHelper.INSTANCE.changeVariant(repository, jobData.getExternalUsername(), stepVariantValues);
			if (changeVariantRet.hasError()) return new SapCommandResult(changeVariantRet);
		}
				
		/* Copiar un job */
		String nameNewJob = jobData.getJobName()+"_"+new Date().getTime();
		CopyJob jobCopy = new CopyJob(repository,jobId,nameNewJob);
		NewJobData newJobData = jobCopy.createCopyJob(jobData, stepVariantValues);
		if (newJobData.hasError()) return new SapCommandResult(newJobData);
				
		/* Correr el job creado */
		String jobCount = newJobData.getJobCount();
		JobRunner jobRunner = new SmartJobRunner(repository);
		SapBapiret2 runRet = jobRunner.runJob(JobData.newJobRunData(nameNewJob, jobData.getExternalUsername(), jobCount));
		if(runRet.hasError()) return new SapCommandResult(runRet);
		
		/* //Monitoreo el job corriendo*/
		JobRunData jobDataMonitor = JobData.newJobTrackData(nameNewJob, jobData.getExternalUsername(), jobCount);
		JobMonitor jobMonitor = new JobMonitor(repository);
		JobFullStatus jobFullStatus = jobMonitor.monitorJob(jobDataMonitor, Configuration.printContinuously());
		return new SapCommandResult(jobFullStatus);
	
	}
}	
