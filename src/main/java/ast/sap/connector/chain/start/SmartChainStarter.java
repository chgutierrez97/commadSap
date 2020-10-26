package ast.sap.connector.chain.start;

import com.google.common.base.Strings;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.job.evt.EventRaiser;

/**
 * @author franco.milanese
 *
 */
public class SmartChainStarter {
	private final SapRepository repository;

	public SmartChainStarter(SapRepository repository) {
		this.repository = repository;
	}

	/**
	 * Ejecuta una Process Chain de SAP
	 * 
	 * @see http://www.sapdatasheet.org/abap/func/rspc_api_chain_start.html
	 * 
	 * @param chain
	 * 
	 * @return ChainData
	 * 			- LogId de una process chain dentro de la estructura ChainData
	 * 
	 * @throws RspcExecuteException
	 */
	public ChainData startChain(String chain, String externalUsername) throws RspcExecuteException{
		SapFunction recentRunFunction = repository.getFunction("RSPC_API_GET_RECENT_RUNS");
		SapFunctionResult recentRunResult = recentRunFunction.execute();
		
		
		SapFunction function = repository.getFunction("RSPC_API_CHAIN_GET_STARTCOND")
				.setInParameter("I_CHAIN", chain);
		
		SapFunctionResult result = function.execute();
		
		SapStruct triggerStruct = result.getStructure("E_S_TRIGGER");
		String eventId = triggerStruct.getValue("EVENTID");
		if(Strings.isNullOrEmpty(eventId)) {
			return new ChainStarter(repository).startChain(chain);
		}
		
		EventRaiser eventRaiser = new EventRaiser(repository);
		eventRaiser.raiseEvent(externalUsername, eventId);

		// TODO COMO OBTENGO EL LOG_ID LUEGO
		
		return new ChainData("", chain);
	}
}