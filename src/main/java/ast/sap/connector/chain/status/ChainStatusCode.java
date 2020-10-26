package ast.sap.connector.chain.status;

import com.google.common.base.Optional;

/**
 * Codigo de estado de cadenas.
 * 
 * @see https://www.sapdatasheet.org/abap/doma/rspc_state.html
 * 
 * @author martin.zaragoza
 *
 */
public enum ChainStatusCode {
	R("Ended with errors"),
	G("Successfully completed"),
	F("Completed"),
	A("Active"),
	X("Canceled"),
	P("Planned"),
	S("Skipped at restart"),
	Q("Released"),
	Y("Ready"),
	J("Ended with Error (for example, subsequent job missing)"),
	U("Unknown");

	public final String label;

	private ChainStatusCode(String label) {
		this.label = label;
	}

	/**
	 * Obtiene un codigo de estado de cadena a partir de un String.
	 * 
	 * @param code
	 *            Codigo de estado.
	 * @return Nuevo codigo de estado de cadena si acaso existe, {@link ChainStatusCode#U} en caso de ser un codigo desconocido.
	 */
	public static ChainStatusCode fromCode(String code) {
		try {
			return ChainStatusCode.valueOf(Optional.fromNullable(code).or(""));
		} catch (IllegalArgumentException e) {
			return U;
		}
	}

	@Override
	public String toString() {
		return String.format("%s - %s", this.name(), this.label);
	}

	/**
	 * Retorna true si la cadena finalizo su ejecucion independientemente de su estado final.
	 * 
	 * @return true si la cadena finalizo su ejecucion independientemente de su estado final, false en caso contrario.
	 */
	public boolean hasFinished() {
		return this.hasFinishedSuccessfully() || this.equals(R) || this.equals(J) || this.equals(X);
	}

	/**
	 * Retorna true si la cadena finalizo correctamente.
	 * 
	 * @return true si la cadena finalizo correctamente, false en caso contrario.
	 */
	public boolean hasFinishedSuccessfully() {
		return this.equals(G) || this.equals(F);
	}

	public boolean notFinished() {
		return !hasFinished();
	}

}
