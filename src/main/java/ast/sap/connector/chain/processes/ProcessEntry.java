package ast.sap.connector.chain.processes;

import ast.sap.connector.func.OutTableRow;

/**
 * @author franco.milanese
 *
 *         Estructura correspondiente a la tabla RSPC_S_PROCESSLIST
 * 
 * @see https://www.sapdatasheet.org/abap/tabl/rspc_s_processlist.html
 */
public class ProcessEntry {

	private String chainId;
	private String eventStart;
	private String eventStartParam;
	private String eventNumber;
	private String backLinkStart;
	private String type;
	private String variant;
	private String predecessor;
	private String instance;
	private String state;
	private String eventEnd;
	private String eventEndParam;
	private String backLinkEnd;
	private String actualState;
	private String eventGreen;
	private String eventGreenParam;
	private String backLinkGreen;
	private String eventRed;
	private String eventRedParam;
	private String backLinkRed;
	private String greenEqRed;
	private String wait;
	private String startTimeStamp;
	private String endTimeStamp;
	private String jobCount;

	String asString(Object value) {
		return value == null ? "" : value.toString();
	}

	public ProcessEntry(OutTableRow currentRow) {
		this.chainId = asString(currentRow.getValue("CHAIN_ID"));
		this.eventStart = asString(currentRow.getValue("EVENT_START"));
		this.eventStartParam = asString(currentRow.getValue("EVENTP_START"));
		this.eventNumber = asString(currentRow.getValue("EVENTNO_START"));
		this.backLinkStart = asString(currentRow.getValue("BACKLINK_START"));
		this.type = asString(currentRow.getValue("TYPE"));
		this.variant = asString(currentRow.getValue("VARIANTE"));
		this.predecessor = asString(currentRow.getValue("PREDECESSOR"));
		this.instance = asString(currentRow.getValue("INSTANCE"));
		this.state = asString(currentRow.getValue("STATE"));
		this.eventEnd = asString(currentRow.getValue("EVENT_END"));
		this.eventEndParam = asString(currentRow.getValue("EVENTP_END"));
		this.backLinkEnd = asString(currentRow.getValue("BACKLINK_END"));
		this.actualState = asString(currentRow.getValue("ACTUAL_STATE"));
		this.eventGreen = asString(currentRow.getValue("EVENT_GREEN"));
		this.eventGreenParam = asString(currentRow.getValue("EVENTP_GREEN"));
		this.backLinkGreen = asString(currentRow.getValue("BACKLINK_GREEN"));
		this.eventRed = asString(currentRow.getValue("EVENT_RED"));
		this.eventRedParam = asString(currentRow.getValue("EVENTP_RED"));
		this.backLinkRed = asString(currentRow.getValue("BACKLINK_RED"));
		this.greenEqRed = asString(currentRow.getValue("GREEN_EQ_RED"));
		this.wait = asString(currentRow.getValue("WAIT"));
		this.startTimeStamp = asString(currentRow.getValue("STARTTIMESTAMP"));
		this.endTimeStamp = asString(currentRow.getValue("ENDTIMESTAMP"));
		this.jobCount = asString(currentRow.getValue("JOB_COUNT"));
	}

	public String getChainId() {
		return chainId;
	}

	public String getEventStart() {
		return eventStart;
	}

	public String getEventStartParam() {
		return eventStartParam;
	}

	public String getEventNumber() {
		return eventNumber;
	}

	public String getBackLinkStart() {
		return backLinkStart;
	}

	public String getType() {
		return type;
	}

	public String getVariant() {
		return variant;
	}

	public String getPredecessor() {
		return predecessor;
	}

	public String getInstance() {
		return instance;
	}

	public String getState() {
		return state;
	}

	public String getEventEnd() {
		return eventEnd;
	}

	public String getEventEndParam() {
		return eventEndParam;
	}

	public String getBackLinkEnd() {
		return backLinkEnd;
	}

	public String getActualState() {
		return actualState;
	}

	public String getEventGreen() {
		return eventGreen;
	}

	public String getEventGreenParam() {
		return eventGreenParam;
	}

	public String getBackLinkGreen() {
		return backLinkGreen;
	}

	public String getEventRed() {
		return eventRed;
	}

	public String getEventRedParam() {
		return eventRedParam;
	}

	public String getBackLinkRed() {
		return backLinkRed;
	}

	public String getGreenEqRed() {
		return greenEqRed;
	}

	public String getWait() {
		return wait;
	}

	public String getStartTimeStamp() {
		return startTimeStamp;
	}

	public String getEndTimeStamp() {
		return endTimeStamp;
	}

	public String getJobCount() {
		return jobCount;
	}

	@Override
	public String toString() {
		return "ProcessEntry [chainId=" + chainId + ", eventStart=" + eventStart + ", eventStartParam=" + eventStartParam + ", eventNumber=" + eventNumber
				+ ", backLinkStart=" + backLinkStart + ", type=" + type + ", variant=" + variant + ", predecessor=" + predecessor + ", instance=" + instance
				+ ", state=" + state + ", eventEnd=" + eventEnd + ", eventEndParam=" + eventEndParam + ", backLinkEnd=" + backLinkEnd + ", actualState="
				+ actualState + ", eventGreen=" + eventGreen + ", eventGreenParam=" + eventGreenParam + ", backLinkGreen=" + backLinkGreen + ", eventRed="
				+ eventRed + ", eventRedParam=" + eventRedParam + ", backLinkRed=" + backLinkRed + ", greenEqRed=" + greenEqRed + ", wait=" + wait
				+ ", startTimeStamp=" + startTimeStamp + ", endTimeStamp=" + endTimeStamp + ", jobCount=" + jobCount + "]";
	}

	/**
	 * Retorna true si los campos STATE o ACTUAL_STATE de la tabla RSPC_S_PROCESSLIST contiene error.
	 * 
	 * @return
	 */
	public boolean hasError() {
		return ProcessStatusCode.fromCode(actualState).isError() || ProcessStatusCode.fromCode(state).isError(); 
	}

}
