package ast.sap.connector.cmd.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.chain.logs.ChainLog;
import ast.sap.connector.chain.logs.ChainLogReader;
import ast.sap.connector.chain.logs.ChainLogReaderInput;
import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;

public class GetChainLogCommand extends AbstractSapCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetChainLogCommand.class);
	private ChainLogReaderInput chainLogReaderInput;

	public GetChainLogCommand(SapRepository sapRepository, ChainLogReaderInput chainLogReaderInput) {
		super(sapRepository);
		this.chainLogReaderInput = chainLogReaderInput;
	}

	@Override
	public SapCommandResult execute() {
		ChainLogReader chainLogReader = new ChainLogReader(repository());
		ChainLog chainLog = chainLogReader.readChainLog(chainLogReaderInput);
		return new SapCommandResult(chainLog);
	}
}
