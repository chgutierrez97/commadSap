package ast.sap.connector.job.read;

import java.util.Date;

import ast.sap.connector.func.SapStruct;
import ast.sap.connector.util.DateUtils;

/**
 * Estructura para capturar BP20JOB
 * 
 * @author franco.milanese
 *
 */
public final class Bp20job {

	private final String jobName;
	private final String jobCount;
	private final String jobGroup;
	private final String intReport;
	private final Integer stepCount;
	private Date plannedStartDate;
	private final String bgTgtSystem;
	private Date scheduleDate;
	private final String scheduleUsername;
	private Date lastChangeDate;
	private final String lastChangeUsername;
	private Date releaseDate;
	private final String releaseUsername;
	private Date startDate;
	private Date endDate;
	private final String periodMins;
	private final String periodHours;
	private final String periodDays;
	private final String periodWeeks;
	private final String periodMonths;
	private final String periodic;
	private final String delAftProc;
	private final String emergMode;
	private final String status;
	private final String newFlag;
	private final String authUser;
	private final String authClient;
	private final Integer succJobs;
	private final Integer prevJobs;
	private final String jobLog;
	private Date lastExecDate;
	private final Integer wpNumber;
	private final Integer wpProcId;
	private final String eventId;
	private final String eventParam;
	private final String bgTgtSystemReax;
	private final String jobClass;
	private final Integer priority;
	private final String eventCount;
	private final String checkStat;
	private final String calendarId;
	private final String periodBehav;
	private final String execServer;
	private final Integer eomCorrect;
	private final Integer calCorrect;
	private final String reaxServer;
	private final String recLogSys;
	private final String recObjType;
	private final String recObjKey;
	private final String recDescrib;
	private final String tgtSrvGrp;

	public Bp20job(SapStruct sapStruct) {
		jobName = sapStruct.getValue("JOBNAME");
		jobCount = sapStruct.getValue("JOBCOUNT");
		jobGroup = sapStruct.getValue("JOBGROUP");
		intReport = sapStruct.getValue("INTREPORT");
		stepCount = sapStruct.getValue("STEPCOUNT");

		Date plannedStartDate = sapStruct.getValue("SDLSTRTDT");
		Date plannedStartTime = sapStruct.getValue("SDLSTRTTM");
		if (plannedStartDate != null && plannedStartTime != null) {
			this.plannedStartDate = DateUtils.INSTANCE.addHours(plannedStartDate, plannedStartTime);
		}

		bgTgtSystem = sapStruct.getValue("BTCSYSTEM");

		Date scheduleDate = sapStruct.getValue("SDLDATE");
		Date scheduleTime = sapStruct.getValue("SDLTIME");
		if (scheduleDate != null && scheduleTime != null) {
			this.scheduleDate = DateUtils.INSTANCE.addHours(scheduleDate, scheduleTime);
		}

		scheduleUsername = sapStruct.getValue("SDLUNAME");

		Date lastChangeDate = sapStruct.getValue("LASTCHDATE");
		Date lastChangeTime = sapStruct.getValue("LASTCHTIME");
		if (lastChangeDate != null && plannedStartTime != null) {
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

		periodMins = sapStruct.getValue("PRDMINS");
		periodHours = sapStruct.getValue("PRDHOURS");
		periodDays = sapStruct.getValue("PRDDAYS");
		periodWeeks = sapStruct.getValue("PRDWEEKS");
		periodMonths = sapStruct.getValue("PRDMONTHS");
		periodic = sapStruct.getValue("PERIODIC");
		delAftProc = sapStruct.getValue("DELANFREP");
		emergMode = sapStruct.getValue("EMERGMODE");
		status = sapStruct.getValue("STATUS");
		newFlag = sapStruct.getValue("NEWFLAG");
		authUser = sapStruct.getValue("AUTHCKNAM");
		authClient = sapStruct.getValue("AUTHCKMAN");
		succJobs = sapStruct.getValue("SUCCNUM");
		prevJobs = sapStruct.getValue("PREDNUM");
		jobLog = sapStruct.getValue("JOBLOG");

		Date lastExecDate = sapStruct.getValue("LASTSTRTDT");
		Date lastExecTime = sapStruct.getValue("LASTSTRTTM");
		if (lastExecDate != null && lastExecTime != null) {
			this.lastExecDate = DateUtils.INSTANCE.addHours(lastExecDate, lastExecTime);
		}

		wpNumber = sapStruct.getValue("WPNUMBER");
		wpProcId = sapStruct.getValue("WPPROCID");
		eventId = sapStruct.getValue("EVENTID");
		eventParam = sapStruct.getValue("EVENTPARM");
		bgTgtSystemReax = sapStruct.getValue("BTCSYSREAX");
		jobClass = sapStruct.getValue("JOBCLASS");
		priority = sapStruct.getValue("PRIORITY");
		eventCount = sapStruct.getValue("EVENTCOUNT");
		checkStat = sapStruct.getValue("CHECKSTAT");
		calendarId = sapStruct.getValue("CALENDARID");
		periodBehav = sapStruct.getValue("PRDBEHAV");
		execServer = sapStruct.getValue("EXECSERVER");
		eomCorrect = sapStruct.getValue("EOMCORRECT");
		calCorrect = sapStruct.getValue("CALCORRECT");
		reaxServer = sapStruct.getValue("REAXSERVER");
		recLogSys = sapStruct.getValue("RECLOGSYS");
		recObjType = sapStruct.getValue("RECOBJTYPE");
		recObjKey = sapStruct.getValue("RECOBJKEY");
		recDescrib = sapStruct.getValue("RECDESCRIB");
		tgtSrvGrp = sapStruct.getValue("TGTSRVGRP");

	}

	public String getJobName() {
		return jobName;
	}

	public String getJobCount() {
		return jobCount;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public String getIntReport() {
		return intReport;
	}

	public Integer getStepCount() {
		return stepCount;
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public String getBgTgtSystem() {
		return bgTgtSystem;
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

	public String getDelAftProc() {
		return delAftProc;
	}

	public String getEmergencyMode() {
		return emergMode;
	}

	public String getStatus() {
		return status;
	}

	public String getNewFlag() {
		return newFlag;
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

	public String getJobLog() {
		return jobLog;
	}

	public Date getLastExecDate() {
		return lastExecDate;
	}

	public Integer getWpNumber() {
		return wpNumber;
	}

	public Integer getWpProcId() {
		return wpProcId;
	}

	public String getEventId() {
		return eventId;
	}

	public String getEventParam() {
		return eventParam;
	}

	public String getBgTgtSystemReax() {
		return bgTgtSystemReax;
	}

	public String getJobClass() {
		return jobClass;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getEventCount() {
		return eventCount;
	}

	public String getCheckStat() {
		return checkStat;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public String getPeriodBehav() {
		return periodBehav;
	}

	public String getExecServer() {
		return execServer;
	}

	public Integer getEomCorrect() {
		return eomCorrect;
	}

	public Integer getCalCorrect() {
		return calCorrect;
	}

	public String getReaxServer() {
		return reaxServer;
	}

	public String getRecLogSys() {
		return recLogSys;
	}

	public String getRecObjType() {
		return recObjType;
	}

	public String getRecObjKey() {
		return recObjKey;
	}

	public String getRecDescrib() {
		return recDescrib;
	}

	public String getTgtSrvGrp() {
		return tgtSrvGrp;
	}

	public String getPeriodMins() {
		return periodMins;
	}

	public String getPeriodHours() {
		return periodHours;
	}

	public String getPeriodDays() {
		return periodDays;
	}

	public String getPeriodWeeks() {
		return periodWeeks;
	}

	public String getPeriodMonths() {
		return periodMonths;
	}

	public String getEmergMode() {
		return emergMode;
	}

	@Override
	public String toString() {
		return "JobRead [jobName=" + jobName + ", jobCount=" + jobCount + ", jobGroup=" + jobGroup + ", intReport="
				+ intReport + ", stepCount=" + stepCount + ", plannedStartDate=" + plannedStartDate + ", bgTgtSystem="
				+ bgTgtSystem + ", scheduleDate=" + scheduleDate + ", scheduleUsername=" + scheduleUsername
				+ ", lastChangeDate=" + lastChangeDate + ", lastChangeUsername=" + lastChangeUsername
				+ ", releaseDate=" + releaseDate + ", releaseUsername=" + releaseUsername + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", periodMins=" + periodMins + ", periodHours=" + periodHours
				+ ", periodDays=" + periodDays + ", periodWeeks=" + periodWeeks + ", periodMonths=" + periodMonths
				+ ", periodic=" + periodic + ", delAftProc=" + delAftProc + ", emergMode=" + emergMode + ", status="
				+ status + ", newFlag=" + newFlag + ", authUser=" + authUser + ", authClient=" + authClient
				+ ", succJobs=" + succJobs + ", prevJobs=" + prevJobs + ", jobLog=" + jobLog + ", lastExecDate="
				+ lastExecDate + ", wpNumber=" + wpNumber + ", wpProcId=" + wpProcId + ", eventId=" + eventId
				+ ", eventParam=" + eventParam + ", bgTgtSystemReax=" + bgTgtSystemReax + ", jobClass=" + jobClass
				+ ", priority=" + priority + ", eventCount=" + eventCount + ", checkStat=" + checkStat
				+ ", calendarId=" + calendarId + ", periodBehav=" + periodBehav + ", execServer=" + execServer
				+ ", eomCorrect=" + eomCorrect + ", calCorrect=" + calCorrect + ", reaxServer=" + reaxServer
				+ ", recLogSys=" + recLogSys + ", recObjType=" + recObjType + ", recObjKey=" + recObjKey
				+ ", recDescrib=" + recDescrib + ", tgtSrvGrp=" + tgtSrvGrp + "]";
	}

}
