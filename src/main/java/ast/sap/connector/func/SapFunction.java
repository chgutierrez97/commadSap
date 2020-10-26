package ast.sap.connector.func;

import com.google.common.base.Preconditions;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.ConversionException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRuntimeException;
import com.sap.conn.jco.JCoTable;

import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.func.exception.ImportParameterSetErrorException;
import ast.sap.connector.func.exception.NonexistentStructParameterException;
import ast.sap.connector.func.exception.NonexistentTableParameterException;
import ast.sap.connector.func.exception.RspcExecuteException;

/**
 * Funcion ejecutable de sap.
 * 
 * @author martin.zaragoza
 *
 */
public class SapFunction {
	private final JCoFunction jCoFunction;
	private final JCoDestination jCoDestination;

	public SapFunction(JCoFunction jCoFunction, JCoDestination jCoDestination) {
		this.jCoFunction = jCoFunction;
		this.jCoDestination = jCoDestination;
	}

	/**
	 * Establece el valor de un parametro de entrada regular.
	 * 
	 * @param key
	 *            - Nombre del parametro.
	 * @param value
	 *            - Valor del parametro.
	 * @return this.
	 * @throws ImportParameterSetErrorException
	 *             Si el parametro no existe o si el valor asignado no es del tipo correcto.
	 */
	public SapFunction setInParameter(String key, Object value) throws ImportParameterSetErrorException {
		Preconditions.checkNotNull(key, "La clave del parametro no puede ser nula");
		Preconditions.checkNotNull(value, "El valor del parametro %s no puede ser nulo", key);

		try {
			jCoFunction.getImportParameterList().setValue(key, value);
		} catch (ConversionException e) {
			throw new ImportParameterSetErrorException(
					String.format("El tipo del parametro de tipo IMPORT %s no es correcto en la funcion %s", key, jCoFunction.getName()), e);
		} catch (JCoRuntimeException e) {
			throw new ImportParameterSetErrorException(
					String.format("Ocurrio un error al establecer el valor del parametro de tipo IMPORT %s de la funcion %s", key, jCoFunction.getName()), e);
		}
		return this;
	}

	/**
	 * Establece un argumento de tipo tabla a la funcion a ejecutar.
	 * 
	 * @param tableName
	 *            - Nombre del argumento.
	 * @return Plantilla para la construccion del argumento de tipo tabla.
	 * @throws NonexistentTableParameterException
	 *             Si no existe un campo de tipo tabla con el nombre asignado.
	 */
	public InTableParam setInTableParameter(String tableName) throws NonexistentTableParameterException {
		Preconditions.checkNotNull(tableName, "El nombre del campo tabla no puede ser nulo");

		try {
			JCoTable jcoTable = jCoFunction.getTableParameterList().getTable(tableName);
			return new TableParam(jcoTable);
		} catch (ConversionException e) {
			throw new NonexistentTableParameterException(
					String.format("El parametro %s no es de tipo tabla en la funcion %s", tableName, jCoFunction.getName()), e);
		} catch (JCoRuntimeException e) {
			throw new NonexistentTableParameterException(String.format("No existe el parametro %s en la funcion %s", tableName, jCoFunction.getName()), e);
		}
	}

	/**
	 * Establece un parametro de entrada de tipo estructura en la funcion a ejecutar.
	 * 
	 * @param structName
	 *            - Nombre del parametro de tipo estructura.
	 * @return Parametro de tipo estructura.
	 * @throws NonexistentStructParameterException
	 *             Si no existe un campo de tipo struct con el nombre asignado.
	 */
	public SapStruct getInStructParameter(String structName) {
		Preconditions.checkNotNull(structName, "El nombre de la estructura no puede ser nulo");

		try {
			return new SapStruct(jCoFunction.getImportParameterList().getStructure(structName));
		} catch (ConversionException e) {
			throw new NonexistentStructParameterException(
					String.format("El parametro %s no es de tipo struct en la funcion %s", structName, jCoFunction.getName()), e);
		} catch (JCoRuntimeException e) {
			throw new NonexistentStructParameterException(String.format("No existe el parametro %s en la funcion %s", structName, jCoFunction.getName()), e);
		}
	}

	/**
	 * Ejecuta la funcion contra el servidor sap.
	 * 
	 * @return Resultado de la ejecucion.
	 * @throws FunctionExecuteException
	 *             Si ocurrio un error al ejecutar la funcion.
	 * @throws RspcExecuteException
	 *             Si ocurrio un error al ejecutar una funcion de RSPC.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public SapFunctionResult execute() throws FunctionExecuteException, RspcExecuteException, FunctionNetworkErrorException {
		try {
			jCoFunction.execute(jCoDestination);
			return new SapFunctionResult(jCoFunction);
		} catch (AbapException e) {
			throw new RspcExecuteException(
					String.format("Ocurrio un error al ejecutar la funcion %s del destino", jCoFunction.getName()), e);
		} catch (JCoException e) {
			if (e.getGroup() == JCoException.JCO_ERROR_COMMUNICATION)
				throw new FunctionNetworkErrorException(
						String.format("Ocurrio un error en la red al ejecutar la funcion %s del destino %s", jCoFunction.getName(), jCoDestination.toString()),
						e);
			else throw new FunctionExecuteException(
					String.format("Ocurrio un error al ejecutar la funcion %s del destino %s", jCoFunction.getName(), jCoDestination.toString()), e);
		}
	}
}
