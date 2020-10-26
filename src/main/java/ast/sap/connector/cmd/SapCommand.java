package ast.sap.connector.cmd;

/**
 * Comando de sap.
 * 
 * @author martin.zaragoza
 *
 */
public interface SapCommand {

	/**
	 * Ejecucion de comando de sap.
	 * 
	 * @return Resultado de la ejecucion.
	 */
	SapCommandResult execute();

}