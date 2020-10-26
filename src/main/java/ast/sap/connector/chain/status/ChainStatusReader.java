package ast.sap.connector.chain.status;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lector de estado de una cadena.
 *
 * @author martin.zaragoza
 * @see https://www.sapdatasheet.org/abap/func/rspc_api_chain_get_status.html
 */
public class ChainStatusReader {
	public static final Logger LOGGER = LoggerFactory.getLogger(ChainStatusReader.class);

	private final SapRepository repository;

	public ChainStatusReader(SapRepository repository) {
		this.repository = repository;
	}

	/**
	 * Lee el estado de una cadena.
	 *
	 * @param input Informacion de la cadena a leer.
	 * @return Estado de la cadena.
	 * @throws FunctionGetFailException      En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException      En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException Si ocurrio un error en la red al ejecutar la funcion.
	 * @see https://www.sapdatasheet.org/abap/func/rspc_api_chain_get_status.html
	 */
	public ChainStatus readChainStatus(ChainStatusReaderInput input) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		SapFunction function = repository.getFunction("RSPC_API_CHAIN_GET_STATUS")
				.setInParameter("I_CHAIN", input.getChainId())
				.setInParameter("I_LOGID", input.getLogId());

		Optional<Boolean> dontPoll = input.getDontPoll();
		Optional<Boolean> dontUpdate = input.getDontUpdate();

		// TODO : estos valores son booleanos. verificar si acaso deberian ser strings
		if (dontPoll.isPresent()) { function.setInParameter("I_DONT_POLL", dontPoll.get()); }
		if (dontUpdate.isPresent()) { function.setInParameter("I_DONT_UPDATE", dontUpdate.get()); }

		SapFunctionResult result = function.execute();
		return fromResult(result);
	}

	private ChainStatus fromResult(SapFunctionResult result) {
		String statusCode = (String) result.getOutParameterValue("E_STATUS");
		String manualAbort = (String) result.getOutParameterValue("E_MANUAL_ABORT");
		String readableStatus = (String) result.getOutParameterValue("E_MESSAGE");

		return new ChainStatus(ChainStatusCode.fromCode(statusCode), manualAbort, readableStatus);
	}

	public static ChainStatusReader build(SapRepository sapRepository) {
		return new ChainStatusReader(sapRepository);
	}
}
