package ast.sap.connector.chain.monitor;

import java.util.ArrayList;
import java.util.List;

import ast.sap.connector.job.log.JobLog;

public class ChainLogBundle {
	
	private List<JobLog> jobLogs = new ArrayList<JobLog>();

	public List<JobLog> getProcesses() {
		return new ArrayList<>(jobLogs);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ChainProcessBundle [processes= ");
		for (JobLog jobLog : jobLogs) {
			sb.append("\n " + jobLog.toString());
		}
		sb.append("\n]");
		return sb.toString();
	}

	public void add(JobLog jobLog) {
		jobLogs.add(jobLog);
	}

}
