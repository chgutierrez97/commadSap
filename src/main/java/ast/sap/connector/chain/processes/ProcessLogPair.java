package ast.sap.connector.chain.processes;

import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.track.JobFullStatus;

/**
 * Par Proceso/Estado
 * 
 * @author franco.milanese
 *
 */
public class ProcessLogPair {
	private ProcessEntry processEntry;
	private JobFullStatus jobFullStatus;

	/**
	 * @param processEntry
	 *            - Proceso asociado a una cadena.
	 * @param jobFullStatus
	 *            - Estado y Log del proceso.
	 */
	public ProcessLogPair(ProcessEntry processEntry, JobFullStatus jobFullStatus) {
		this.processEntry = processEntry;
		this.jobFullStatus = jobFullStatus;
	}

	public ProcessEntry getProcessEntry() {
		return processEntry;
	}

	public JobFullStatus getJobFullStatus() {
		return jobFullStatus;
	}

	public JobLog getJobLog() {
		return jobFullStatus.getJobLog();
	}

	@Override
	public String toString() {
		return "ProcessLogPair [processEntry=" + processEntry + ", jobFullStatus=" + jobFullStatus + "]";
	}
}
