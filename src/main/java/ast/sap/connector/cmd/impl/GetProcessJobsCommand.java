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

public class GetProcessJobsCommand extends AbstractSapCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetProcessJobsCommand.class);
	private String chain;
	private String type;
	private String variant;
	private String event;
	private String eventParam;

	public GetProcessJobsCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chain, String type, String variant, String event, String eventParam) {
		super(sapRepository);
		this.chain = chain;
		this.type = type;
		this.variant = variant;
		this.event = event;
		this.eventParam = eventParam;
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException{
		SapFunction function = repository().getFunction("RSPC_API_PROCESS_GET_JOBS")
				.setInParameter("I_CHAIN", chain)
				.setInParameter("I_TYPE", type)
				.setInParameter("I_VARIANT", variant)
				.setInParameter("I_EVENT", event)
				.setInParameter("I_EVENTP", eventParam);
		SapFunctionResult execute = function.execute();
		LOGGER.debug(execute.getOutTableParameter("E_T_JOBLIST").toString());
		return SapCommandResult.emptyResult();
	}

}
