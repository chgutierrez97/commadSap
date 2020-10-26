package ast.sap.connector.func.exception;

public final class NonexistentStructParameterException extends RuntimeException {
	private static final long serialVersionUID = 7547640284721655445L;

	public NonexistentStructParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
