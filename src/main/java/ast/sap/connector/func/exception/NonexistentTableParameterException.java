package ast.sap.connector.func.exception;

public final class NonexistentTableParameterException extends RuntimeException {
	private static final long serialVersionUID = 5451335497432429798L;

	public NonexistentTableParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
