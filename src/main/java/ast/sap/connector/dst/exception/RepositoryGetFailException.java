package ast.sap.connector.dst.exception;

public final class RepositoryGetFailException extends Exception {
	private static final long serialVersionUID = 7410291102048241774L;

	public RepositoryGetFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryGetFailException(String message) {
		super(message);
	}

}
