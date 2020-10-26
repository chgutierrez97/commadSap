package ast.sap.connector.cmd.impl;

import java.util.Date;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.cmd.VariantChangerHelper;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobCreateData;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.create.CopyJob;
import ast.sap.connector.job.create.JobCreator;
import ast.sap.connector.job.create.NewJobData;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.run.JobRunner;
import ast.sap.connector.job.run.SmartJobRunner;
import ast.sap.connector.main.args.InputArgumentsData;
import ast.sap.connector.xmi.XmiLoginData;

public class CopyRunJobCommand  extends SapXmiCommand {
	
	private final JobCreateData jobData;
	private final StepVariantValuesTuple stepVariantValues;
	private final String jobId;
	
	
	public CopyRunJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobCreateData jobData,	StepVariantValuesTuple stepVariantValues,String idJob) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.stepVariantValues = stepVariantValues;
		this.jobId = idJob;
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

		return new SapCommandResult(runRet);
		
	}
	
	
	
	

}
