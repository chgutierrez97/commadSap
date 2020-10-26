package ast.sap.connector.xmi;

public class XmiSessionData {
	private final Object sessionId;
	private final String xmiInterface;

	public XmiSessionData(Object sessionId, String xmiInterface) {
		this.sessionId = sessionId;
		this.xmiInterface = xmiInterface;
	}

	public Object getSessionId() {
		return sessionId;
	}

	public String getXmiInterface() {
		return xmiInterface;
	}
}
