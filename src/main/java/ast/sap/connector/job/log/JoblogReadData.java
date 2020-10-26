package ast.sap.connector.job.log;

import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobTrackData;

public class JoblogReadData implements JobTrackData {
	private JobData jobData;

	public static enum Direction {
		BEGINNING("B"), END("E");

		public final String code;

		private Direction(String code) {
			this.code = code;
		}
	}

	private int lines = 0;
	private Direction direction;

	public JoblogReadData(String jobName, String jobId, String externalUsername, int lines, Direction direction) {
		jobData = JobData.newJobTrackData(jobName, externalUsername, jobId);
		this.lines = lines;
		this.direction = direction;
	}

	public JoblogReadData(String jobName, String jobId, String externalUsername, Direction direction) {
		jobData = JobData.newJobTrackData(jobName, externalUsername, jobId);
		this.direction = direction;
	}

	public JoblogReadData(String jobName, String jobId, String externalUsername, int lines) {
		jobData = JobData.newJobTrackData(jobName, externalUsername, jobId);
		this.lines = lines;
	}

	public JoblogReadData(String jobName, String jobId, String externalUsername) {
		jobData = JobData.newJobTrackData(jobName, externalUsername, jobId);
	}

	public int getLines() {
		return lines;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getJobName() {
		return jobData.getJobName();
	}

	public String getExternalUsername() {
		return jobData.getExternalUsername();
	}

	public String getJobId() {
		return jobData.getJobId();
	}
}
