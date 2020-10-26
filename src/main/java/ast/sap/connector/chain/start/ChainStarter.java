package ast.sap.connector.chain.start;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.func.exception.RspcExecuteException;

/**
 * @author franco.milanese
 *
 */
public class ChainStarter {

	private final SapRepository repository;

	public ChainStarter(SapRepository repository) {
		this.repository = repository;
	}

	/**
	 * Ejecuta una Process Chain de SAP
	 * 
	 * @see http://www.sapdatasheet.org/abap/func/rspc_api_chain_start.html
	 * 
	 * @param chain
	 * 		 - Nombre de la cadena a ejecutar
	 * @return ChainData 
	 * 		 - LogId de una process chain dentro de la estructura ChainData
	 * @throws RspcExecuteException
	 *       - Excepcion RSPC, propia de la ejecucion de la funcion.
	 * @throws FunctionGetFailException
	 *       - En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *       - En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *       - Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public ChainData startChain(String chain) throws RspcExecuteException, FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {

		SapFunction function = repository.getFunction("RSPC_API_CHAIN_START")
				.setInParameter("I_CHAIN", chain);
		SapFunctionResult result = function.execute();
		String logId = result.getOutParameterValue("E_LOGID").toString();
		return new ChainData(logId, chain);
	}
}