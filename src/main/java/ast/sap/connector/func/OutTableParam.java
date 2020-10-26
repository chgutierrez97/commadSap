package ast.sap.connector.func;

/**
 * Representa un parametro de tipo tabla de SALIDA (tabla resultado de una ejecucion de funcion de sap).
 * 
 * @author martin.zaragoza
 *
 */
public interface OutTableParam {

	/**
	 * Navega a la siguiente fila del resultset de la funcion.
	 * 
	 * @return La siguiente fila.
	 */
	OutTableRow nextRow();

	/**
	 * Obtiene la fila actual.
	 * 
	 * @return fila actual.
	 */
	OutTableRow currentRow();

	/**
	 * Obtiene la cantidad de filas del resultset.
	 * 
	 * @return cantidad de filas del resultset.
	 */
	int getRowCount();

	/**
	 * True si el resultset esta vacio, false en caso contrario.
	 * 
	 * @return True si el resultset esta vacio, false en caso contrario.
	 */
	boolean isEmpty();
}