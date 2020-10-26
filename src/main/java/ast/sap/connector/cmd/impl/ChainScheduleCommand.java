package ast.sap.connector.cmd.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

public class ChainScheduleCommand extends AbstractSapCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChainScheduleCommand.class);
	private String chain;
	private Optional<String> periodic;

	public ChainScheduleCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chain, String periodic) {
		super(sapRepository);
		this.chain = chain;
		this.periodic = Optional.fromNullable(periodic);
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {
		SapFunction function = repository().getFunction("RSPC_API_CHAIN_SCHEDULE")
				.setInParameter("I_CHAIN", chain);

		if (periodic.isPresent()) {
			function.setInParameter("I_PERIODIC", periodic);
		}
		SapFunctionResult execute = function.execute();
		LOGGER.debug(execute.getOutTableParameter("E_T_CONFLICTS").toString());
		return SapCommandResult.emptyResult();
	}

}
