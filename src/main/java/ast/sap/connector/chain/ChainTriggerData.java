package ast.sap.connector.chain;

import java.util.Date;

import ast.sap.connector.func.SapStruct;

/**
 * @author franco.milanese
 *
 *         Estructura correspondiente a la tabla TBTCSTRT
 */
public final class ChainTriggerData {

	private Date plannedStartDate;
	private Date plannedStartTime;
	private String lastStartDate;
	private Date lastStartTime;
	private String previousJob;
	private String jobId;
	private String jobStatusCheck;
	private String eventId;
	private String eventParam;
	private String startDateType;
	private String periodMins;
	private String periodHours;
	private String periodDays;
	private String periodWeeks;
	private String periodMonth;
	private String periodic;
	private String serverName;
	private String calendarId;
	private Integer eomCorrect;
	private Integer calCorrect;
	private String immediateFlag;
	private String periodBehav;
	private String workdayNumber;
	private String workdayDir;
	private String notBefore;

	public ChainTriggerData(SapStruct trigger) {
		this.plannedStartDate = trigger.getValue("SDLSTRTDT");
		this.plannedStartTime = trigger.getValue("SDLSTRTTM");
		this.lastStartDate = trigger.getValue("LASTSTRTDT");
		this.lastStartTime = trigger.getValue("LASTSTRTTM");
		this.previousJob = trigger.getValue("PREDJOB");
		this.jobId = trigger.getValue("PREDJOBCNT");
		this.jobStatusCheck = trigger.getValue("CHECKSTAT");
		this.eventId = trigger.getValue("EVENTID");
		this.eventParam = trigger.getValue("EVENTPARM");
		this.startDateType = trigger.getValue("STARTDTTYP");
		this.periodMins = trigger.getValue("PRDMINS");
		this.periodHours = trigger.getValue("PRDHOURS");
		this.periodDays = trigger.getValue("PRDDAYS");
		this.periodWeeks = trigger.getValue("PRDWEEKS");
		this.periodMonth = trigger.getValue("PRDMONTHS");
		this.periodic = trigger.getValue("PERIODIC");
		this.serverName = trigger.getValue("INSTNAME");
		this.calendarId = trigger.getValue("CALENDARID");
		this.eomCorrect = trigger.getValue("EOMCORRECT");
		this.calCorrect = trigger.getValue("CALCORRECT");
		this.immediateFlag = trigger.getValue("IMSTRTPOS");
		this.periodBehav = trigger.getValue("PRDBEHAV");
		this.workdayNumber = trigger.getValue("WDAYNO");
		this.workdayDir = trigger.getValue("WDAYCDIR");
		this.notBefore = trigger.getValue("NOTBEFORE");
	}

	public Date getPlannedStartDate() {
		return plannedStartDate;
	}

	public Date getPlannedStartTime() {
		return plannedStartTime;
	}

	public String getLastStartDate() {
		return lastStartDate;
	}

	public Date getLastStartTime() {
		return lastStartTime;
	}

	public String getPreviousJob() {
		return previousJob;
	}

	public String getJobId() {
		return jobId;
	}

	public String getJobStatusCheck() {
		return jobStatusCheck;
	}

	public String getEventId() {
		return eventId;
	}

	public String getEventParam() {
		return eventParam;
	}

	public String getStartDateType() {
		return startDateType;
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

	public String getPeriodMonth() {
		return periodMonth;
	}

	public String getPeriodic() {
		return periodic;
	}

	public String getServerName() {
		return serverName;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public Integer getEomCorrect() {
		return eomCorrect;
	}

	public Integer getCalCorrect() {
		return calCorrect;
	}

	public String getImmediateFlag() {
		return immediateFlag;
	}

	public String getPeriodBehav() {
		return periodBehav;
	}

	public String getWorkdayNumber() {
		return workdayNumber;
	}

	public String getWorkdayDir() {
		return workdayDir;
	}

	public String getNotBefore() {
		return notBefore;
	}

	@Override
	public String toString() {
		return "ChainTriggerData [plannedStartDate=" + plannedStartDate + ", plannedStartTime=" + plannedStartTime + ", lastStartDate=" + lastStartDate
				+ ", lastStartTime=" + lastStartTime + ", previousJob=" + previousJob + ", jobId=" + jobId + ", jobStatusCheck=" + jobStatusCheck
				+ ", eventId=" + eventId + ", eventParam=" + eventParam + ", startDateType=" + startDateType + ", periodMins=" + periodMins + ", periodHours="
				+ periodHours + ", periodDays=" + periodDays + ", periodWeeks=" + periodWeeks + ", periodMonth=" + periodMonth + ", periodic=" + periodic
				+ ", serverName=" + serverName + ", calendarId=" + calendarId + ", eomCorrect=" + eomCorrect + ", calCorrect=" + calCorrect
				+ ", immediateFlag=" + immediateFlag + ", periodBehav=" + periodBehav + ", workdayNumber=" + workdayNumber + ", workdayDir=" + workdayDir
				+ ", notBefore=" + notBefore + "]";
	}

}
