package ast.sap.connector.cmd.impl;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.start.ChainStarter;
import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

public class StartChainCommand extends AbstractSapCommand {
	private String chain;

	public StartChainCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chain) {
		super(sapRepository);
		this.chain = chain;
	}

	@Override
	public SapCommandResult execute() throws RspcExecuteException {

		 ChainStarter chainStarter = new ChainStarter(repository());
		 ChainData chainData = chainStarter.startChain(chain);
		 return new SapCommandResult(chainData);
	}

}
