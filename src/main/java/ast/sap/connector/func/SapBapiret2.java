package ast.sap.connector.func;

import com.google.common.base.Optional;

/**
 * Estructura de datos bastante comun en sap.
 * 
 * @see http://www.se80.co.uk/saptables/b/bapi/bapiret2.htm
 * 
 * @author martin.zaragoza
 *
 */
public class SapBapiret2 {
	private final String type;
	private final String id;
	private final Integer number;
	private final String message;
	private final String logNo;
	private final Integer logMsgNo;
	private final String messagev1;
	private final String messagev2;
	private final String messagev3;
	private final String messagev4;
	private final String parameter;
	private final Integer row;
	private final String field;
	private final String system;
	

	public SapBapiret2(SapStruct sapStruct) {
		type = (String) sapStruct.getValue("TYPE");
		id = (String) sapStruct.getValue("ID");
		number = Integer.valueOf(Optional.fromNullable(sapStruct.getValue("NUMBER")).or("-1").toString());
		message = (String) sapStruct.getValue("MESSAGE");
		logNo = (String) sapStruct.getValue("LOG_NO");
		logMsgNo = Integer.valueOf(Optional.fromNullable(sapStruct.getValue("LOG_MSG_NO")).or("-1").toString());
		messagev1 = (String) sapStruct.getValue("MESSAGE_V1");
		messagev2 = (String) sapStruct.getValue("MESSAGE_V2");
		messagev3 = (String) sapStruct.getValue("MESSAGE_V3");
		messagev4 = (String) sapStruct.getValue("MESSAGE_V4");
		parameter = (String) sapStruct.getValue("PARAMETER");
		row = Integer.valueOf(Optional.fromNullable(sapStruct.getValue("ROW")).or("-1").toString());
		field = (String) sapStruct.getValue("FIELD");
		system = (String) sapStruct.getValue("SYSTEM");
	}
	

	public SapBapiret2(String type, String id, Integer number, String message, String logNo, Integer logMsgNo,
			String messagev1, String messagev2, String messagev3, String messagev4, String parameter, Integer row,
			String field, String system) {
		super();
		this.type = type;
		this.id = id;
		this.number = number;
		this.message = message;
		this.logNo = logNo;
		this.logMsgNo = logMsgNo;
		this.messagev1 = messagev1;
		this.messagev2 = messagev2;
		this.messagev3 = messagev3;
		this.messagev4 = messagev4;
		this.parameter = parameter;
		this.row = row;
		this.field = field;
		this.system = system;
	}









	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	public String getMessage() {
		return message;
	}

	public String getLogNo() {
		return logNo;
	}

	public int getLogMsgNo() {
		return logMsgNo;
	}

	public String getMessagev1() {
		return messagev1;
	}

	public String getMessagev2() {
		return messagev2;
	}

	public String getMessagev3() {
		return messagev3;
	}

	public String getMessagev4() {
		return messagev4;
	}

	public String getParameter() {
		return parameter;
	}

	public int getRow() {
		return row;
	}

	public String getField() {
		return field;
	}

	public String getSystem() {
		return system;
	}

	@Override
	public String toString() {
		return "SapBapiret2 [type=" + type + ", id=" + id + ", number=" + number + ", message=" + message + ", logNo="
				+ logNo + ", logMsgNo=" + logMsgNo + ", messagev1=" + messagev1 + ", messagev2=" + messagev2
				+ ", messagev3=" + messagev3 + ", messagev4=" + messagev4 + ", parameter=" + parameter + ", row=" + row
				+ ", field=" + field + ", system=" + system + "]";
	}

	public boolean hasError() {
		return "E".equals(this.getType());
	}
}
