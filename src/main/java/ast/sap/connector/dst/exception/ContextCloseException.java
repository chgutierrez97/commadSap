package ast.sap.connector.dst.exception;

public final class ContextCloseException extends RuntimeException {
	private static final long serialVersionUID = -5206432038012686100L;

	public ContextCloseException(String message, Throwable cause) {
		super(message, cause);
	}
}
