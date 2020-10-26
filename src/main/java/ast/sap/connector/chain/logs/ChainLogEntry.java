package ast.sap.connector.chain.logs;

import ast.sap.connector.func.OutTableRow;

/**
 * @author franco.milanese
 *
 * Clase correspondiente a la tabla de log RSPC_S_MSG
 * 
 * @see https://www.sapdatasheet.org/abap/tabl/rspc_s_msg.html	
 */
public class ChainLogEntry {

	private final String msgId;
	private final Integer msgNumber;
	private final String msgType;
	private final String msgV1;
	private final String msgV2;
	private final String msgV3;
	private final String msgV4;

	public ChainLogEntry(OutTableRow entry) {

		this.msgId = entry.getValue("MSGID").toString();
		this.msgNumber = Integer.parseInt(entry.getValue("MSGNO").toString());
		this.msgType = entry.getValue("MSGTY").toString();
		this.msgV1 = entry.getValue("MSGV1").toString();
		this.msgV2 = entry.getValue("MSGV2").toString();
		this.msgV3 = entry.getValue("MSGV3").toString();
		this.msgV4 = entry.getValue("MSGV4").toString();

	}

	@Override
	public String toString() {
		return getPrettyString();
	}

	/**
	 * Obtiene una entrada de log como un String formateado para facilitar su lectura.
	 * 
	 * @return entrada de log como String formateado.
	 */
	public String getPrettyString() {
		StringBuilder out = new StringBuilder();
		out.append(msgId);
		out.append(" - ");
		out.append(msgNumber);
		out.append(" - ");
		out.append(msgType);
		out.append(" - ");
		out.append(msgV1);
		out.append(" - ");
		out.append(msgV2);
		out.append(" - ");
		out.append(msgV3);
		out.append(" - ");
		out.append(msgV4);
		return out.toString();
	}
	
}
