package ast.sap.connector.xmi;

import ast.sap.connector.dst.SapDestination;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.xmi.exception.XmiLoginException;

/**
 * Sesion con subsistema XMI. Para poder realizar acciones de manipulacion de jobs (iniciar, obtener status, obtener log, etc.) es necesario inciar sesion con
 * XMI de forma tal que nuestras operaciones sobre los jobs queden registradas en logs de SAP.
 * 
 * @author mzaragoz
 *
 */
public class XmiSession {
	private final SapRepository sapRepository;
	private final XmiSessionData sessionData;

	/**
	 * Inicia una sesion con XMI. Ver {@link XmiSessionManager}.
	 * 
	 * @param sapRepository
	 *            - Repositorio de funciones de sap iniciado para operar con contexto mediante {@link SapDestination#openContext()}.
	 * @param xmiLoginData
	 *            - Datos de inicio de sesion con XMI.
	 * 
	 * @throws XmiLoginException
	 *             Si no es posible iniciar sesion con xmi.
	 * 
	 */
	public XmiSession(SapRepository sapRepository, XmiLoginData loginData) throws XmiLoginException {
		this.sapRepository = sapRepository;
		this.sessionData = XmiSessionManager.INSTANCE.login(sapRepository, loginData);
	}

	public SapBapiret2 logout() {
		return XmiSessionManager.INSTANCE.logout(sapRepository, sessionData);
	}

	public XmiSessionData getSessionData() {
		return sessionData;
	}
}
