package ast.sap.connector.job.track;

import ast.sap.connector.job.log.JobLog;

/**
 * Estado final de un job. Incluye su log de ejecucion y el estado final.
 * 
 * @author mzaragoz
 *
 */
public class JobFullStatus {
	private JobLog jobLog;
	private JobStatus jobStatus;

	/**
	 * Crea una instancia de estado final de un job.
	 * 
	 * @param jobLog
	 *            Log del job.
	 * @param jobStatus
	 *            Estado del job.
	 */
	public JobFullStatus(JobLog jobLog, JobStatus jobStatus) {
		this.jobLog = jobLog;
		this.jobStatus = jobStatus;
	}

	public JobLog getJobLog() {
		return jobLog;
	}

	public JobStatus getJobStatus() {
		return jobStatus;
	}

	@Override
	public String toString() {
		return "JobFullStatus [jobLog=" + jobLog + ", jobStatus=" + jobStatus + "]";
	}
	
	
}
