package ast.sap.connector.func;

/**
 * Fila de una tabla de salida.
 * 
 * @author martin
 *
 */
public interface OutTableRow {

	/**
	 * Obtiene el valor de una celda.
	 * 
	 * @param colName
	 *            - Nombre de la columna del valor a obtener.
	 * @return valor de la columna de la fila.
	 */
	Object getValue(String colName);

}