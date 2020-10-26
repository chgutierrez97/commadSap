package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.AbstractSapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;

/**
 * Invoca a una funcion RFC enabled disponible desde AFE500
 * 
 * @author mzaragoz
 *
 */
public class CrystalGetuserlistCommand extends AbstractSapCommand {
	public CrystalGetuserlistCommand(SapRepository sapRepository) {
		super(sapRepository);
	}

	@Override
	public SapCommandResult execute() {
		SapFunction function = repository().getFunction("/CRYSTAL/GET_USER_LIST");

//		SapFunctionResult result = function.execute();
		function.execute();
		return SapCommandResult.emptyResult();
		// return new SapCommandResult(result.getOutTableParameter("USERACTGRP"));
	}

}
