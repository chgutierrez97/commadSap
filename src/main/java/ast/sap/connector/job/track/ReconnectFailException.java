package ast.sap.connector.job.track;

public final class ReconnectFailException extends RuntimeException {
	private static final long serialVersionUID = -1303559438643681937L;

	public ReconnectFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReconnectFailException(String message) {
		super(message);
	}
}
