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

public class GetChainProcessLogCommand extends AbstractSapCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetChainProcessLogCommand.class);
	private String logId;
	private String type;
	private String variant;
	private String jobCount;

	public GetChainProcessLogCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String logId, String type, String variant, String jobCount) {
		super(sapRepository);
		this.logId = logId;
		this.type = type;
		this.variant = variant;
		this.jobCount = jobCount;
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {
		SapFunction function = repository().getFunction("RSPC_API_PROCESS_GET_LOG")
				.setInParameter("I_LOGID", logId)
				.setInParameter("I_TYPE", type)
				.setInParameter("I_VARIANT", variant)
				.setInParameter("I_JOBCOUNT", jobCount);
		SapFunctionResult result = function.execute();
		LOGGER.debug(result.getOutTableParameter("E_T_JOB_LOG").toString());
		return SapCommandResult.emptyResult();
	}

}
