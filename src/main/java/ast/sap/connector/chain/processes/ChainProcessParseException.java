package ast.sap.connector.chain.processes;

public final class ChainProcessParseException extends RuntimeException {

	private static final long serialVersionUID = 7690387243628461655L;

	public ChainProcessParseException(String message, Throwable e) {
		super(message, e);
	}

}
