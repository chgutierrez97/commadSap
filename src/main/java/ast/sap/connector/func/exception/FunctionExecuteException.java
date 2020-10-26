package ast.sap.connector.func.exception;

public final class FunctionExecuteException extends RuntimeException {
	private static final long serialVersionUID = 6774919672109212766L;

	public FunctionExecuteException(String message) {
		super(message);
	}

	public FunctionExecuteException(String message, Throwable cause) {
		super(message, cause);
	}
}
