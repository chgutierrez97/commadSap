package ast.sap.connector.dst.exception;

public final class NonexistentFunctionException extends RuntimeException {
	private static final long serialVersionUID = 963299260626471538L;

	public NonexistentFunctionException(String message) {
		super(message);
	}

	public NonexistentFunctionException(String message, Throwable cause) {
		super(message, cause);
	}

}
