package ast.sap.connector.dst.exception;

public final class FunctionGetFailException extends RuntimeException {
	private static final long serialVersionUID = 167935136995907050L;

	public FunctionGetFailException(String message) {
		super(message);
	}

	public FunctionGetFailException(String message, Throwable cause) {
		super(message, cause);
	}
}
