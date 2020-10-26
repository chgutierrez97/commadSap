package ast.sap.connector.func;

/**
 * Fila de un parametro de tipo tabla de entrada.
 * 
 * @author martin.zaragoza
 *
 */
public interface InTableRow {
	/**
	 * Establece el valor de un campo/celda de la fila.
	 * 
	 * @param column
	 *            - Nombre de columna.
	 * @param value
	 *            - Valor del campo.
	 * @return this.
	 */
	InTableRow setValue(String column, Object value);
}
