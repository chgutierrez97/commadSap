package ast.sap.connector.dst.exception;

public final class FunctionNetworkErrorException extends RuntimeException {
	private static final long serialVersionUID = 5084476080021754855L;

	public FunctionNetworkErrorException(String message) {
		super(message);
	}

	public FunctionNetworkErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
