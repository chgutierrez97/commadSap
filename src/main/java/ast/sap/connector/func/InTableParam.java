package ast.sap.connector.func;

/**
 * Representa un parametro de tipo tabla de ENTRADA (parametro de una funcion sap a invocar).
 * La tabla es un componente navegable que permite agregar filas y establecer los valores de las celdas.
 * 
 * @author martin.zaragoza
 *
 */
public interface InTableParam {

	/**
	 * Agrega una fila al parametro tabla.
	 * 
	 * @return this.
	 */
	InTableRow appendRow();
}