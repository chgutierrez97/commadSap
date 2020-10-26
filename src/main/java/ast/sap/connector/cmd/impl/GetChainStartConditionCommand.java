package ast.sap.connector.cmd.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.chain.ChainTriggerData;
import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

public class GetChainStartConditionCommand extends AbstractSapCommand {

	public static final Logger LOGGER = LoggerFactory.getLogger(GetChainStartConditionCommand.class);
	private String chain;

	public GetChainStartConditionCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chain) {
		super(sapRepository);
		this.chain = chain;
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {
		SapFunction function = repository().getFunction("RSPC_API_CHAIN_GET_STARTCOND")
				.setInParameter("I_CHAIN", chain);
		SapFunctionResult execute = function.execute();
		// TODO: esta tabla devuelve los datos necesarios para setear la start condition, ademas de otros innecesarios
		SapStruct trigger = execute.getStructure("E_S_TRIGGER");
		ChainTriggerData triggerData = new ChainTriggerData(trigger);
		LOGGER.debug(triggerData.toString());
		String meta = trigger.getValue("META");
		LOGGER.debug("META: " + meta);
		return SapCommandResult.emptyResult();
	}

}
