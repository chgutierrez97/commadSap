package ast.sap.connector.job.def;

import java.util.Date;

import ast.sap.connector.func.SapStruct;
import ast.sap.connector.util.DateUtils;

/**
 * @author franco.milanese
 *
 * Estructura de la clase BAPIXMJOB de SAP
 */
public final class BapiXmJob {
	private final String jobName;
	private final String jobCount;
	private final Integer stepCount;
	private Date plannedStartDate;
	private final String targetSystem;
	private Date scheduleDate;
	private final String scheduleUsername;
	private Date lastChangeDate;
	private final String lastChangeUsername;
	private Date releaseDate;
	private final String releaseUsername;
	private Date startDate;
	private Date endDate;
	private final String periodic;
	private final String status;
	private final String authUser;
	private final String authClient;
	private final Integer succJobs;
	private final Integer prevJobs;
	private Date lastExeDate;
	private final Integer processNumber;
	private final Integer processId;
	private final String eventID;
	private final String eventParams;
	private final String jobClass;
	private final String calendarId;
	private final String execServer;
	private final String reaxServer;

	public BapiXmJob(SapStruct sapStruct) {
		jobName = sapStruct.getValue("JOBNAME");
		jobCount = sapStruct.getValue("JOBCOUNT");
		stepCount = sapStruct.getValue("STEPCOUNT");

		Date plannedStartDate = sapStruct.getValue("SDLSTRTDT");
		Date plannedStartTime = sapStruct.getValue("SDLSTRTTM");
		if (plannedStartDate != null && plannedStartTime != null) {
			this.plannedStartDate = DateUtils.INSTANCE.addHours(plannedStartDate, plannedStartTime);
		}

		targetSystem = sapStruct.getValue("BTCSYSTEM");

		Date scheduleDate = sapStruct.getValue("SDLDATE");
		Date scheduleTime = sapStruct.getValue("SDLTIME");
		if (scheduleDate != null && scheduleTime != null) {
			this.scheduleDate = DateUtils.INSTANCE.addHours(scheduleDate, scheduleTime);
		}
		scheduleUsername = sapStruct.getValue("SDLUNAME");

		Date lastChangeDate = sapStruct.getValue("LASTCHDATE");
		Date lastChangeTime = sapStruct.getValue("LASTCHTIME");
		if (lastChangeDate != null && lastChangeTime != null) {
			this.lastChangeDate = DateUtils.INSTANCE.addHours(lastChangeDate, lastChangeTime);
		}

		lastChangeUsername = sapStruct.getValue("LASTCHNAME");

		Date releaseDate = sapStruct.getValue("RELDATE");
		Date releaseTime = sapStruct.getValue("RELTIME");
		if (releaseDate != null && releaseTime != null) {
			this.releaseDate = DateUtils.INSTANCE.addHours(releaseDate, releaseTime);
		}

		releaseUsername = sapStruct.getValue("RELUNAME");

		Date startDate = sapStruct.getValue("STRTDATE");
		Date startTime = sapStruct.getValue("STRTTIME");
		if (startDate != null && startTime != null) {
			this.startDate = DateUtils.INSTANCE.addHours(startDate, startTime);
		}

		Date endDate = sapStruct.getValue("ENDDATE");
		Date endTime = sapStruct.getValue("ENDTIME");
		if (endDate != null && endTime != null) {
			this.endDate = DateUtils.INSTANCE.addHours(endDate, endTime);
		}

		periodic = sapStruct.getValue("PERIODIC");
		status = sapStruct.getValue("STATUS");
		authUser = sapStruct.getValue("AUTHCKNAM");
		authClient = sapStruct.getValue("AUTHCKMAN");
		succJobs = sapStruct.getValue("SUCCNUM");
		prevJobs = sapStruct.getValue("PREDNUM");

		Date lastExeDate = sapStruct.getValue("LASTSTRTDT");
		Date lastExeTime = sapStruct.getValue("LASTSTRTTM");
		if (lastExeDate != null && lastExeTime != null) {
			this.lastExeDate = DateUtils.INSTANCE.addHours(lastExeDate, lastExeTime);
		}

		processNumber = sapStruct.getValue("WPNUMBER");
		processId = sapStruct.getValue("WPPROCID");
		eventID = sapStruct.getValue("EVENTID");
		eventParams = sapStruct.getValue("EVENTPARM");
		jobClass = sapStruct.getValue("JOBCLASS");
		calendarId = sapStruct.getValue("CALENDARID");
		execServer = sapStruct.getValue("EXECSERVER");
		reaxServer = sapStruct.getValue("REAXSERVER");
	}

	public String getJobName() {
		return jobName;
	}

	public String getJobCount() {
		return jobCount;
	}

	public Integer getStepCount() {
		return stepCount;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public String getTargetSystem() {
		return targetSystem;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public String getScheduleUsername() {
		return scheduleUsername;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public String getLastChangeUsername() {
		return lastChangeUsername;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public String getReleaseUsername() {
		return releaseUsername;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getPeriodic() {
		return periodic;
	}

	public String getStatus() {
		return status;
	}

	public String getAuthUser() {
		return authUser;
	}

	public String getAuthClient() {
		return authClient;
	}

	public Integer getSuccJobs() {
		return succJobs;
	}

	public Integer getPrevJobs() {
		return prevJobs;
	}

	public Date getLastExeDate() {
		return lastExeDate;
	}

	public Integer getProcessNumber() {
		return processNumber;
	}

	public Integer getProcessId() {
		return processId;
	}

	public String getEventID() {
		return eventID;
	}

	public String getEventParams() {
		return eventParams;
	}

	public String getJobClass() {
		return jobClass;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public String getExecServer() {
		return execServer;
	}

	public String getReaxServer() {
		return reaxServer;
	}

	@Override
	public String toString() {
		return "JobDefinition [jobName=" + jobName + ", jobCount=" + jobCount + ", stepCount=" + stepCount
				+ ", plannedStartDate=" + plannedStartDate + ", targetSystem=" + targetSystem + ", scheduleDate="
				+ scheduleDate + ", scheduleUsername=" + scheduleUsername + ", lastChangeDate=" + lastChangeDate
				+ ", lastChangeUsername=" + lastChangeUsername + ", releaseDate=" + releaseDate + ", releaseUsername="
				+ releaseUsername + ", startDate=" + startDate + ", endDate=" + endDate + ", periodic=" + periodic
				+ ", status=" + status + ", authUser=" + authUser + ", authClient=" + authClient + ", succJobs="
				+ succJobs + ", prevJobs=" + prevJobs + ", lastExeDate=" + lastExeDate + ", processNumber="
				+ processNumber + ", processId=" + processId + ", eventID=" + eventID + ", eventParams=" + eventParams
				+ ", jobClass=" + jobClass + ", calendarId=" + calendarId + ", execServer=" + execServer
				+ ", reaxServer=" + reaxServer + "]";
	}
}
