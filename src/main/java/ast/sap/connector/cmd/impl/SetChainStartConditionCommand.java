package ast.sap.connector.cmd.impl;

import com.google.common.base.Optional;

import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

public class SetChainStartConditionCommand extends AbstractSapCommand {

	private String chain;
	// TODO: que tipo es este campo
	private Optional<String> startCondition;

	private Optional<String> externalApi;

	public SetChainStartConditionCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chain, String externalApi) {
		super(sapRepository);
		this.chain = chain;
		this.externalApi = Optional.fromNullable(externalApi);
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {
		SapFunction function = repository().getFunction("RSPC_API_CHAIN_SET_STARTCOND")
				.setInParameter("I_CHAIN", chain);

		SapFunctionResult execute = function.execute();
		return SapCommandResult.emptyResult();
	}

}
