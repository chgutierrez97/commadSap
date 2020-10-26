package ast.sap.connector.chain.error;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.func.exception.RspcExecuteException;

public class ChainErrorReader {
	private final SapRepository repository;

	public ChainErrorReader(SapRepository repository) { this.repository = repository; }

	/**
	 * Obtiene los errores de una Process Chain de SAP
	 *
	 * @param chainData - Datos de la cadena
	 * @return Datos de errores de la cadena
	 * @throws RspcExecuteException          - Excepcion RSPC, propia de la ejecucion de la funcion.
	 * @throws FunctionGetFailException      - En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException      - En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException - Si ocurrio un error en la red al ejecutar la funcion.
	 * @see https://www.sapdatasheet.org/abap/func/rspc_api_chain_get_errors.html
	 */
	public ChainErrorLog startChain(ChainData chainData)
			throws RspcExecuteException, FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		SapFunction function = repository.getFunction("RSPC_API_CHAIN_GET_ERRORS")
				.setInParameter("I_CHAIN", chainData.getChain())
				.setInParameter("I_LOGID", chainData.getLogId());

		SapFunctionResult result = function.execute();

		OutTableParam logDetailsTable = result.getOutTableParameter("E_T_LOG_DETAILS");
		return new ChainErrorLog(logDetailsTable);
	}

}
