package ast.sap.connector.dst;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRepository;

import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.dst.exception.NonexistentFunctionException;
import ast.sap.connector.func.SapFunction;

/**
 * Representa un repositorio de funciones de sap.
 * 
 * @author martin.zaragoza
 *
 */
public class SapRepository {
	private JCoRepository jcoRepository;
	private JCoDestination jcoDestination;

	SapRepository(JCoRepository jCoRepository, JCoDestination jcoDestination) {
		this.jcoRepository = jCoRepository;
		this.jcoDestination = jcoDestination;
	}

	/**
	 * Obtiene una funcion ejecutable de sap.
	 * 
	 * @param functionName
	 *            - Nombre de la funcion.
	 * @return Funcion ejecutable.
	 * @throws FunctionGetFailException
	 *             Si ocurrio un error al obtener o correr la funcion.
	 * @throws NonexistentFunctionException
	 *             Si la funcion requerida no existe.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public SapFunction getFunction(String functionName) throws FunctionGetFailException, NonexistentFunctionException, FunctionNetworkErrorException {
		try {
			JCoFunctionTemplate jCoFunctionTemplate = jcoRepository.getFunctionTemplate(functionName);
			if (jCoFunctionTemplate == null) { throw new NonexistentFunctionException(
					String.format("La funcion %s no existe en el destino %s", functionName, jcoDestination.toString())); }
			JCoFunction jcoFunction = jCoFunctionTemplate.getFunction();
			if (jcoFunction == null) { throw new NonexistentFunctionException(
					String.format("La funcion %s no existe en el destino %s", functionName, jcoDestination.toString())); }
			return new SapFunction(jcoFunction, jcoDestination);
		} catch (JCoException e) {
			if (e.getGroup() == JCoException.JCO_ERROR_COMMUNICATION)
				throw new FunctionNetworkErrorException(
						String.format("Ocurrio un error en la red al ejecutar la funcion %s del destino %s", functionName, jcoDestination.toString()),
						e);
			throw new FunctionGetFailException(
					String.format("Ocurrio un error al obtener la funcion %s del destino %s", functionName, jcoDestination.toString()),
					e);
		}
	}
}
