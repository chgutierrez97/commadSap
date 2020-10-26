package ast.sap.connector.cmd.impl;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.monitor.ChainFullStatus;
import ast.sap.connector.chain.monitor.ChainMonitor;
import ast.sap.connector.chain.start.ChainStarter;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Comando para el monitoreo de cadenas de SAP
 * 
 * @author franco.milanese
 *
 */
public class MonitorChainCommand extends SapXmiCommand {

	private String chainId;
	private String username;

	/**
	 * @param sapRepository
	 *            - Repositorio de funciones SAP.
	 * @param xmiLoginData
	 *            - Datos para iniciar sesion con XMI.
	 * @param chainId
	 *            - Nombre de la cadena a monitorear en el servidor SAP.
	 * @param username
	 *            - Usuario que ejecuta el comando.
	 */
	public MonitorChainCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, String chainId, String username) {
		super(sapRepository, xmiLoginData);
		this.chainId = chainId;
		this.username = username;
	}

	@Override
	protected SapCommandResult perform() throws RspcExecuteException {

		ChainStarter chainStarter = new ChainStarter(repository());
		ChainData chainData = chainStarter.startChain(chainId);
		ChainMonitor chainMonitor = new ChainMonitor(repository());

		ChainFullStatus chainFullStatus = chainMonitor.monitorChain(chainData, username);

		return new SapCommandResult(chainFullStatus);
	}
}
