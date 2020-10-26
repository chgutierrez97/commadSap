package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;

public class UserGetDetailCommand extends AbstractSapCommand {
	private final String username;

	public UserGetDetailCommand(SapRepository sapRepository, String username) {
		super(sapRepository);
		this.username = username;
	}

	@Override
	public SapCommandResult execute() {
		SapFunction function = repository().getFunction("BAPI_USER_GET_DETAIL")
				.setInParameter("USERNAME", username);

		SapFunctionResult result = function.execute();
		String snc = result.getStructure("SNC").getValue("PNAME").toString();

		return new SapCommandResult(snc);
	}
}
