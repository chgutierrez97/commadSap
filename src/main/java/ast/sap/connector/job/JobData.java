package ast.sap.connector.job;

/**
 * Datos de un job.
 * 
 * @author martin.zaragoza
 *
 */
public final class JobData implements JobRunData {
	private String jobName = "";
	private String externalUsername = "";
	private String jobId = "";
	private String targetServer;

	private JobData(String jobName, String externalUsername) {
		this.jobName = jobName;
		this.externalUsername = externalUsername;
	}

	private JobData(String jobName, String externalUsername, String jobId) {
		this.jobName = jobName;
		this.externalUsername = externalUsername;
		this.jobId = jobId;
	}

	private JobData(String jobName, String externalUsername, String jobId, String targetServer) {
		this.jobName = jobName;
		this.externalUsername = externalUsername;
		this.jobId = jobId;
		this.targetServer = targetServer;
	}

	public static JobData newJobCreateData(String jobName, String externalUsername) {
		return new JobData(jobName, externalUsername);
	}

	public static JobData newJobTrackData(String jobName, String externalUsername, String jobId) {
		return new JobData(jobName, externalUsername, jobId);
	}

	public static JobData newJobRunData(String jobName, String externalUsername, String jobId) {
		return new JobData(jobName, externalUsername, jobId);
	}
	
	public static JobData newJobRunData(String jobName, String externalUsername, String jobId, String targetServer) {
		return new JobData(jobName, externalUsername, jobId, targetServer);
	}

	@Override
	public String getJobName() {
		return jobName;
	}

	@Override
	public String getExternalUsername() {
		return externalUsername;
	}

	@Override
	public String getJobId() {
		return jobId;
	}

	@Override
	public String getTargetServer() {
		return targetServer;
	}

	@Override
	public String toString() {
		return "JobData [jobName=" + jobName + ", externalUsername=" + externalUsername + ", jobId=" + jobId
				+ ", targetServer=" + targetServer + "]";
	}
}