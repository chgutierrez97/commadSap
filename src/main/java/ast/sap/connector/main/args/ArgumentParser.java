package ast.sap.connector.main.args;

/**
 * Parsea un argumento de entrada.
 */
public interface ArgumentParser {
	/**
	 * Determina si el argumento coincide con el patron establecido.
	 *
	 * @param arg
	 *            - Argumento a analizar.
	 * @return True si el argumento matchea el formato de patron establecido.
	 */
	boolean matches(String arg);

	/**
	 * Obtiene el valor del argumento.
	 *
	 * @param arg
	 *            - Argumento completo.
	 * @return Valor del argumento.
	 */
	String getArgValue(String arg);

	/**
	 * Establece el valor de un argumento
	 * 
	 * @param inputArgumentsData
	 *            - Componente de argumentos de programa.
	 * @param arg
	 *            - Argumento con valor. Ej "-cRUN_JOB".
	 * @return Componente de argumentos modificado.
	 */
	InputArgumentsData setArgValue(InputArgumentsData inputArgumentsData, String arg);
}
