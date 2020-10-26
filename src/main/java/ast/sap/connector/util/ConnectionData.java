package ast.sap.connector.util;

import com.google.common.base.Preconditions;

/**
 * Datos de conexion para la creacion de archivos de destino de JCO3 con sap.
 * 
 * @see DestinationConfigBuilder
 * 
 * @author martin.zaragoza
 *
 */
public final class ConnectionData {
	private final String sapClient;
	private final String userId;
	private final String password;
	private final String language;
	private final String host;
	private final String systemNumber;

	public String getSapClient() {
		return sapClient;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getLanguage() {
		return language;
	}

	public String getHost() {
		return host;
	}

	public String getSystemNumber() {
		return systemNumber;
	}

	/**
	 * Construye un nuevo set de datos de conexion con SAP.
	 * 
	 * @param sapClient
	 *            - Numero de cliente (ej: 500)
	 * @param userId
	 *            - Nombre de usuario (ej: mzaragoz)
	 * @param password
	 *            - Password
	 * @param language
	 *            - Lenguaje del sistema (ej: "EN" para ingles)
	 * @param host
	 *            - Ip del server sap o nombre del host (ej: saphanatest)
	 * @param systemNumber
	 *            - Codigo de numero de sistema o numero de instancia (ej: 01)
	 */
	public ConnectionData(String sapClient, String userId, String password, String language, String host, String systemNumber) {
		this.sapClient = sapClient;
		this.userId = userId;
		this.password = password;
		this.language = language;
		this.host = host;
		this.systemNumber = systemNumber;
	}

	/**
	 * Valida la informacion de conexion. Lanza una excepcion en caso que encontrar datos invalidos.
	 * 
	 * @throws InvalidConnectionDataException
	 *             En caso que la informacion sea invalida o insuficiente.
	 */
	public void validate() throws InvalidConnectionDataException {
		try {
			Preconditions.checkNotNull(language, "No se indico un idioma");
			Preconditions.checkNotNull(userId, "No se indico un username");
			Preconditions.checkNotNull(sapClient, "No se indico un numero de cliente sap");
			Preconditions.checkNotNull(host, "No se indico el host/ip del server");
			Preconditions.checkNotNull(systemNumber, "No se indico el numero del sistema");
		} catch (Exception e) {
			throw new InvalidConnectionDataException("La informacion de conexion es incompleta", e);
		}
	}
}
