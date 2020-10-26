package ast.sap.connector.main.args;

public final class InputArgsParseException extends RuntimeException {
	public InputArgsParseException(String message) {
		super(message);
	}

	public InputArgsParseException(Throwable cause) {
		super(cause);
	}

	public InputArgsParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
