package ast.sap.connector.chain.error;

import ast.sap.connector.func.OutTableRow;

public class ChainErrorLogEntry {
	private String chainId;
	private String logId;
	private String type;
	private String variant;
	private String instance;
	private String jobCount;
	private String msgId;
	private int msgNo;
	private String msgType;
	private String msgv1;
	private String msgv2;
	private String msgv3;
	private String msgv4;

	public ChainErrorLogEntry(OutTableRow chainErrorLogRow) {
		this.chainId = (String) chainErrorLogRow.getValue("CHAIN_ID");
		this.logId = (String) chainErrorLogRow.getValue("LOG_ID");
		this.type = (String) chainErrorLogRow.getValue("TYPE");
		/* Por alguna razon este parametro se llama VARIANTE y no VARIANT*/
		this.variant = (String) chainErrorLogRow.getValue("VARIANTE");
		this.instance = (String) chainErrorLogRow.getValue("INSTANCE");
		this.jobCount = (String) chainErrorLogRow.getValue("JOB_COUNT");
		this.msgId = (String) chainErrorLogRow.getValue("MSGID");
		this.msgNo = Integer.valueOf(chainErrorLogRow.getValue("MSGNO").toString());
		this.msgType = (String) chainErrorLogRow.getValue("MSGTY");
		this.msgv1 = (String) chainErrorLogRow.getValue("MSGV1");
		this.msgv2 = (String) chainErrorLogRow.getValue("MSGV2");
		this.msgv3 = (String) chainErrorLogRow.getValue("MSGV3");
		this.msgv4 = (String) chainErrorLogRow.getValue("MSGV4");
	}

	public String getChainId() {
		return chainId;
	}

	public String getLogId() {
		return logId;
	}

	public String getType() {
		return type;
	}

	public String getVariant() {
		return variant;
	}

	public String getInstance() {
		return instance;
	}

	public String getJobCount() {
		return jobCount;
	}

	public String getMsgId() {
		return msgId;
	}

	public int getMsgNo() {
		return msgNo;
	}

	public String getMsgType() {
		return msgType;
	}

	public String getMsgv1() {
		return msgv1;
	}

	public String getMsgv2() {
		return msgv2;
	}

	public String getMsgv3() {
		return msgv3;
	}

	public String getMsgv4() {
		return msgv4;
	}

	// TODO : ESTA BIEN VERIFICAR CONTRA EL MSGTYPE? ESTA BIEN QUE USEMOS LOS VALORES A Y E O QUE USEMOS msgv4 ?
	public boolean hasError() {
		return "A".equalsIgnoreCase(msgType) ||
				"E".equalsIgnoreCase(msgType) ||
				"Canceled".equalsIgnoreCase(msgv4);
	}
}
