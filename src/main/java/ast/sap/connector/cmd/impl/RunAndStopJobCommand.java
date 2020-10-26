package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.log.JoblogReadData;
import ast.sap.connector.job.log.JoblogReader;
import ast.sap.connector.job.run.AsapJobRunner;
import ast.sap.connector.job.run.JobRunner;
import ast.sap.connector.job.run.JobStopper;
import ast.sap.connector.job.track.JobStatus;
import ast.sap.connector.job.track.JobTracker;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Comando de prueba que dispara, monitorea, mata a un job y verifica su log.
 * 
 * @author mzaragoz
 *
 */
public class RunAndStopJobCommand extends SapXmiCommand {
	private JobRunData jobData;

	public RunAndStopJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository sapRepository = repository();
		System.out.println("Corriendo job: " + jobData);
		JobRunner jobRunner = new AsapJobRunner(sapRepository);
		SapBapiret2 runRet = jobRunner.runJob(jobData);

		if ("E".equals(runRet.getType())) {
			System.err.println("Ocurrio un error al disparar el job: " + runRet.getMessage());
			return new SapCommandResult(runRet);
		}

		sleep();

		printJobStatus(sapRepository);

		sleep();

		printJobStatus(sapRepository);
		
		printJobLog(sapRepository);

		System.out.println("Deteniendo job");
		JobStopper jobStopper = new JobStopper(sapRepository);
		SapBapiret2 stopRet = jobStopper.stopJob(jobData);
		if ("E".equals(stopRet.getType())) {
			System.err.println("Ocurrio un error al detener el job: " + stopRet.getMessage());
		}

		printJobStatus(sapRepository);
		
		sleep();

		printJobStatus(sapRepository);

		return new SapCommandResult(stopRet);
	}

	private void printJobLog(SapRepository sapRepository) {
		JoblogReader joblogReader = new JoblogReader(sapRepository);
		JobLog jobLog = joblogReader.readLog(new JoblogReadData(jobData.getJobName(), jobData.getJobId(), jobData.getExternalUsername()));
		System.out.println("Job log: " + jobLog);
	}

	private void sleep() {
		try {
			System.out.println("Durmiendo");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void printJobStatus(SapRepository sapRepository) {
		JobTracker jobTracker = new JobTracker(sapRepository);
		JobStatus jobStatus = jobTracker.getStatus(jobData);
		System.out.println("statusCode: " + jobStatus.getStatusCode());
	}
}
