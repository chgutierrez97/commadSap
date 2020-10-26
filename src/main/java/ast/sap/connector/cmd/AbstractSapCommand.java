package ast.sap.connector.cmd;

import ast.sap.connector.dst.SapRepository;

/**
 * Define un comando de sap que requiere de un repositorio de funciones de sap para funcionar.
 * 
 * @author martin
 *
 */
public abstract class AbstractSapCommand implements SapCommand {
	private final SapRepository sapRepository;

	public AbstractSapCommand(SapRepository sapRepository) {
		this.sapRepository = sapRepository;
	}

	protected SapRepository repository() {
		return sapRepository;
	}
}