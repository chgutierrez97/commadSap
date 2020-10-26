package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.util.Encryptor;

/**
 * Comando para encriptar un password de texto plano usando el algoritmo por defecto del conector sap.
 * 
 * @author martin.zaragoza
 *
 */
public class EncryptPasswordCommand implements SapCommand {
	private final String plainText;

	public EncryptPasswordCommand(String plainText) {
		this.plainText = plainText;
	}

	@Override
	public SapCommandResult execute() {
		return new SapCommandResult(Encryptor.INSTANCE.encrypt(plainText));
	}
}
