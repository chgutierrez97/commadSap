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

public class GetProcessLogCommand extends AbstractSapCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetProcessLogCommand.class);
	private String logId;
	private String type;
	private String variant;
	private Optional<String> jobId;
	
	public GetProcessLogCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String logId, String type, String variant, String jobId) {
		super(sapRepository);
		this.logId = logId;
		this.type = type;
		this.variant = variant;
		this.jobId = Optional.fromNullable(jobId);
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {
		SapFunction function = repository().getFunction("RSPC_API_PROCESS_GET_LOG")
				.setInParameter("I_LOGID", logId)
				.setInParameter("I_TYPE", type)
				.setInParameter("I_VARIANT", variant);
		if (jobId.isPresent()) {
			function.setInParameter("I_JOBCOUNT", jobId.get());
		}
		SapFunctionResult execute = function.execute();
		LOGGER.debug(execute.getOutParameterValue("E_JOBNAME").toString());
		LOGGER.debug(execute.getOutParameterValue("E_JOBCOUNT").toString());
		LOGGER.debug(execute.getOutParameterValue("E_STATUS").toString());
		LOGGER.debug(execute.getOutTableParameter("E_T_PROCESS_LOG").toString());
		LOGGER.debug(execute.getOutTableParameter("E_T_JOB_LOG").toString());
		return SapCommandResult.emptyResult();
				
	}


}
