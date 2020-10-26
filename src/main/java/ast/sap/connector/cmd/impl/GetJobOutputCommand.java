package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.job.JobTrackData;
import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.log.JoblogReadData;
import ast.sap.connector.job.log.JoblogReader;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Obtiene el JOB LOG de un sap JOB.
 * 
 * @author martin.zaragoza
 *
 */
public class GetJobOutputCommand extends SapXmiCommand {
	private JobTrackData jobData;

	public GetJobOutputCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobTrackData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	protected SapCommandResult perform() {
		JoblogReader joblogReader = new JoblogReader(repository());
		JoblogReadData joblogReadData = new JoblogReadData(jobData.getJobName(), jobData.getJobId(), jobData.getExternalUsername());
		JobLog jobLog = joblogReader.readLog(joblogReadData);

		return new SapCommandResult(jobLog);
	}

}
