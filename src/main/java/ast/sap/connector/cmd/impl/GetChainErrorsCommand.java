package ast.sap.connector.cmd.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

public class GetChainErrorsCommand extends AbstractSapCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetChainErrorsCommand.class);
	private String chainName;
	private String logId;

	public GetChainErrorsCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chainName, String logId) {
		super(sapRepository);
		this.chainName = chainName;
		this.logId = logId;
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException{
		SapFunction function = repository().getFunction("RSPC_API_CHAIN_GET_ERRORS")
				.setInParameter("I_CHAIN", chainName)
				.setInParameter("I_LOGID", logId);
		SapFunctionResult execute = function.execute();
		LOGGER.debug(execute.getOutTableParameter("E_T_LOG").toString());
		LOGGER.debug(execute.getOutTableParameter("E_T_LOG_DETAILS").toString());
		
		return SapCommandResult.emptyResult();
	}


}
