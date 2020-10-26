package ast.sap.connector.cmd.impl;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.processes.ChainProcessBundle;
import ast.sap.connector.chain.processes.ChainProcessesReader;
import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

public class GetChainProcessesCommand extends AbstractSapCommand {

	private ChainData chainData;

	public GetChainProcessesCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, ChainData chainData) {
		super(sapRepository);
		this.chainData = chainData;
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {
		ChainProcessesReader chainProcReader = new ChainProcessesReader(repository());
		ChainProcessBundle chainProcesses = chainProcReader.readProcesses(chainData);
		return new SapCommandResult(chainProcesses);
	}

}
