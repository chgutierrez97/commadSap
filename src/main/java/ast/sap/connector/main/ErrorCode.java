package ast.sap.connector.main;

import ast.sap.connector.job.track.JobStatusCode;

/**
 * TODO : LOS CODIGOS DE ERROR EN LINUX VAN DEL 0 AL 255. SE DEBEN MODIFICAR ESTOS CODIGOS.
 * 
 * @author franco.milanese
 *
 *         Contiene los codigos de error
 */
public enum ErrorCode {
	/**
	 * 0 - Ejecucion exitosa del conector
	 */
	SUCCESS(0, ""),

	/**
	 * 1 - Error al iniciar sesion de sap u obtener el respositorio del destino
	 */
	REPOSITORY_GET_FAIL(1, "Error al iniciar sesion de sap u obtener el respositorio del destino."),

	/**
	 * 10 - Error al realizar una operacion de rspc
	 */
	RSPC_ERROR(10, "Ocurrio un error al ejecutar modulo de RSPC (manipulacion de process chains)"),

	/**
	 * 11 - Error cuando la cadena no finalizo correctamente.
	 */
	UNFINISHED_CHAIN(11, "La cadena no finalizo correctamente."),
	
	/**
	 * 12 - Error cuando algun job de la cadena no finalizo correctamente.
	 */
	UNFINISHED_CHAIN_JOB(12, "Algun job de cadena no finalizo correctamente."),
	
	/**
	 * 20 - Este error ocurre cuando no es posible cargar la bilbioteca nativa de jco.
	 */
	JCO_LIBRARY_ERROR(20, "Ocurrio un error al obtener la biblioteca nativa de jco (verificar java.library.path)"),

	/**
	 * 30 - Este error ocurre cuando no es posible cargar la bilbioteca nativa de jco.
	 */
	SAP_SESSION_ERROR(30, "Ocurrio un error al iniciar sesion con sap (credenciales incorrectas / servidor inalcanzable)"),

	/**
	 * 40 - BAPI_XMI_LOGON - Error al iniciar sesion con XMI
	 */
	XMI_LOGIN_EXCEPTION(40, "Error al iniciar sesion con XMI."),

	/**
	 * 50 - Error al proveer credenciales insuficientes para iniciar sesion con SAP.
	 */
	INSUFFICIENT_CREDENTIALS(50, "Las credenciales de conexion son insuficientes."),

	RECONNECT_FAIL(58,"No es posible reestablecer la conexion con sap"),

	/**
	 * 59 - Error al modificar uno o varios valores de campos de una variante.
	 */
	VARIANT_FIELD_CHANGE_ERROR(59, "Ocurrio un error al modificar el campo de una variante"),
	
	/**
	 * 60 - Error que ocurre cuando el job no finalizo exitosamente (con estado {@link JobStatusCode#F}.
	 */
	UNFINISHED_JOB(60, "El job no finalizo correctamente."),

	JCO_ERROR_CONVERSION(61, "Fallo una conversion entre dos representaciones de un parametro, estructura o tabla."),
	JCO_ERROR_FUNCTION_NOT_FOUND(62, "Una funcion o una de sus estructuras no ha podido ser obtenida del repositorio."),
	JCO_ERROR_ILLEGAL_TID(63, "Se ha detectado un Id de transaccion invalida. Tiene una longitud mayor a 24 o contiene caracteres invalidos."),
	JCO_ERROR_UNSUPPORTED_CODEPAGE(64, "El codepage del sistema Sap o el local, no es soportado por Jco."),
	JCO_ERROR_ABAP_EXCEPTION(65, "Una excepcion ha sido lanzada por un RFM."),
	JCO_ERROR_FIELD_NOT_FOUND(66, "Una referencia a un parametro, estructura o tabla no existe."),
	JCO_ERROR_NOT_SUPPORTED(67, "La caracteristica no es soportada por la actual version de Jco."),
	JCO_ERROR_SERVER_STARTUP(68, "Fallo el inicio del servidor Jco."),
	JCO_ERROR_XML_PARSER(69, "Error de parseo debido a un documento XML invalido."),

	JCO_GENERAL_ERROR(70, "Ocurrio un error durante la ejecucion del job"),
	NONEXISTENT_REPORT(71, "El programa/reporte indicado no existe"),
	CANNOT_RUN_JOB(72, "No es posible correr el job (probablemente el estado del mismo no sea Scheduled o Released)"),
	CANNOT_FIND_JOB(73, "No es posible encontrar el job indicado"),
	NONEXISTENT_VARIANT(74, "La variante especificada no existe o no corresponde con el reporte indicado"),
	NONEXISTENT_LOG(0, "No existe un log"),
	

	/**
	 * 255 - UNKNOWN - Error no desconocido
	 */
	UNKNOWN(255, "Error desconocido o no configurado.");

	// DEPRECADOS ------------------------------------------------------------------------------------------------------------------------------

	public final int code;
	public final String message;

	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @param code
	 * @return Error a partir del codigo recibido
	 */
	public static ErrorCode getError(int code) {
		ErrorCode[] errorCodes = ErrorCode.values();
		for (ErrorCode errorCode : errorCodes) {
			if (errorCode.code == code) return errorCode;
		}
		return UNKNOWN;
	}

	/**
	 * @param code
	 * @return Error a partir del nombre de la excepcion
	 */
	public static ErrorCode getError(String key) {
		ErrorCode[] errorCodes = ErrorCode.values();
		for (ErrorCode errorCode : errorCodes) {
			if (errorCode.name().equals(key)) return errorCode;
		}
		return UNKNOWN;
	}
}
