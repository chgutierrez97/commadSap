package ast.sap.connector.util;

public class InvalidConnectionDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8030231662992280706L;

	public InvalidConnectionDataException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidConnectionDataException(String arg0) {
		super(arg0);
	}

	public InvalidConnectionDataException(Throwable arg0) {
		super(arg0);
	}

}
