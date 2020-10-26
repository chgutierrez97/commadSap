package ast.sap.connector.cmd.impl;

import ast.sap.connector.chain.status.ChainStatusReader;
import ast.sap.connector.chain.status.ChainStatusReaderInput;
import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;

public class GetChainStatusCommand extends AbstractSapCommand {
	private final ChainStatusReaderInput input;

	public GetChainStatusCommand(SapRepository sapRepository, ChainStatusReaderInput input) {
		super(sapRepository);
		this.input = input;
	}

	@Override
	public SapCommandResult execute() {
		SapRepository repository = repository();
		ChainStatusReader chainStatusReader = new ChainStatusReader(repository);
		chainStatusReader.readChainStatus(input);

		
		
		return null;
	}
}
