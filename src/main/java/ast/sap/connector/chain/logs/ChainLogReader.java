package ast.sap.connector.chain.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;

public class ChainLogReader {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChainLogReader.class);
	
	private final SapRepository repository;

	public ChainLogReader(SapRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * @see https://www.sapdatasheet.org/abap/func/rspc_api_chain_get_log.html 
	 * 
	 * @param chainLogReaderInput - Datos de la cadena cuyo log obtener
	 * @return log de la cadena
	 */
	public ChainLog readChainLog(ChainLogReaderInput chainLogReaderInput) {
		SapFunction function = repository.getFunction("RSPC_API_CHAIN_GET_LOG")
				.setInParameter("I_CHAIN", chainLogReaderInput.getChainId())
				.setInParameter("I_LOGID", chainLogReaderInput.getLogId());
		if (chainLogReaderInput.getDontPoll().isPresent()) function.setInParameter("I_DONT_POOL", chainLogReaderInput.getDontPoll().get());
		SapFunctionResult execute = function.execute();
		LOGGER.debug(execute.getOutTableParameter("E_T_LOG").toString());
		LOGGER.debug(execute.getOutTableParameter("E_T_LOG_DETAILS").toString());
		return new ChainLog(execute.getOutTableParameter("E_T_LOG"));
		
	}
}
