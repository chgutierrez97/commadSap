package ast.sap.connector.xmi;

import ast.sap.connector.dst.SapDestination;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.xmi.exception.XmiLoginException;

/**
 * Manejador de sesiones XMI. Para poder realizar acciones de manipulacion de jobs (iniciar, obtener status, obtener log, etc.) es necesario inciar sesion con
 * XMI de forma tal que nuestras operaciones sobre los jobs queden registradas en logs de SAP.
 * 
 * @author mzaragoz
 *
 */
public enum XmiSessionManager {
	INSTANCE;

	XmiLoginData lastLoginData = new XmiLoginData();

	/**
	 * Inicia sesion con el subsistema XMI.
	 * 
	 * @see https://www.sapdatasheet.org/abap/func/bapi_xmi_logon.html
	 * 
	 * @param sapRepository
	 *            - Repositorio de funciones de sap iniciado para operar con contexto mediante {@link SapDestination#openContext()}.
	 * @param xmiLoginData
	 *            - Datos de inicio de sesion con XMI.
	 * @return Datos de sesion de XMI.
	 * @throws XmiLoginException
	 *             Si no se pudo inciar sesion con xmi.
	 */
	public XmiSessionData login(SapRepository sapRepository, XmiLoginData xmiLoginData) throws XmiLoginException {
		SapFunction function = sapRepository.getFunction("BAPI_XMI_LOGON")
				.setInParameter("EXTCOMPANY", xmiLoginData.getCompany())
				.setInParameter("EXTPRODUCT", xmiLoginData.getProduct())
				.setInParameter("INTERFACE", xmiLoginData.getXmiInterface())
				.setInParameter("VERSION", xmiLoginData.getVersion());

		try {
			SapFunctionResult result = function.execute();
			Object sessionId = result.getOutParameterValue("SESSIONID");
			this.lastLoginData = xmiLoginData;
			return new XmiSessionData(sessionId, xmiLoginData.getXmiInterface());
		} catch (Exception e) {
			throw new XmiLoginException("Ocurrio un error al iniciar sesion XMI", e);
		}
	}

	/**
	 * Vuelve a iniciar sesion con los datos de xmi de la ultima sesion establecida.
	 * 
	 * @param sapRepository
	 *            - Repositorio de funciones de sap iniciado para operar con contexto mediante {@link SapDestination#openContext()}.
	 * @return Datos de sesion de XMI.
	 * @throws XmiLoginException
	 *             Si no se pudo inciar sesion con xmi.
	 */
	public XmiSessionData reLogin(SapRepository sapRepository) throws XmiLoginException {
		return this.login(sapRepository, lastLoginData);
	}

	/**
	 * Cierra la sesion de XMI.
	 * 
	 * @see https://www.sapdatasheet.org/abap/func/bapi_xmi_logoff.html
	 * 
	 * @param sapRepository
	 *            - Repositorio con acceso a funciones de sap.
	 * @param xmiSessionData
	 *            - Datos de sesion abierta.
	 * @return Datos de retorno.
	 */
	public SapBapiret2 logout(SapRepository sapRepository, XmiSessionData xmiSessionData) {
		SapFunction function = sapRepository.getFunction("BAPI_XMI_LOGOFF")
				.setInParameter("INTERFACE", xmiSessionData.getXmiInterface());

		SapFunctionResult result = function.execute();

		SapStruct ret = result.getStructure("RETURN");
		return new SapBapiret2(ret);
	}
}
