package ast.sap.connector.chain.processes;

import com.google.common.base.Optional;

/**
 * Codigos de estados de procesos
 * 
 * @author franco.milanese
 *
 */
public enum ProcessStatusCode {
	R("Ended with errors"),
	G("Successfully completed"),
	F("Completed"),
	A("Active"),
	X("Canceled"),
	P("Planned"),
	S("Skipped at restart"),
	Q("Released"),
	Y("Ready"),
	U("Undefined"),
	J("Ended with Error (for example, subsequent job missing)");

	private final String label;

	private ProcessStatusCode(String lbl) {
		this.label = lbl;
	}

	/**
	 * Obtiene la instancia a partir del codigo devuelto en la tabla RSPC_S_PROCESSLIST
	 * 
	 * @param code
	 * @return
	 */
	public static ProcessStatusCode fromCode(String code) {
		try {
			return ProcessStatusCode.valueOf(Optional.fromNullable(code).or(""));
		} catch (IllegalArgumentException e) {
			return ProcessStatusCode.U;
		}
	}

	public String getLabel() {
		return label;
	}

	public boolean isError() {
		return this.equals(R) || this.equals(X) || this.equals(J); 
				
	}
	

}
