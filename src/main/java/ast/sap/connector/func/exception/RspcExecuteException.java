package ast.sap.connector.func.exception;

import com.sap.conn.jco.AbapException;

/**
 * @author franco.milanese
 *
 * Captura las Excepciones devueltas por las funciones RSPC
 */
public final class RspcExecuteException extends RuntimeException {
	private static final long serialVersionUID = 6149034274642672142L;

	private AbapException abapException;

	public RspcExecuteException(String message, AbapException abapException) {
		super(message, abapException);
		this.abapException = abapException;
	}

	public AbapException getAbapException() {
		return abapException;
	}

	@Override
	public String toString() {
		return "RspcExecuteException [abapException=" + abapException + "]";
	}
}
