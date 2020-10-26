package ast.sap.connector.main;

import com.google.common.base.Strings;

public class OutputError {
	private int code;
	private int trueCode;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTrueCode() {
		return trueCode;
	}

	/**
	 * @param code
	 * @param message
	 * 
	 *            Crea un OutputError con el code y message indicado por parametro
	 */
	public OutputError(int code, String message) {
		this.code = code;
		this.trueCode = code;
		this.message = message;
	}

	/**
	 * @param errorLevel
	 * @param message
	 * @param code
	 * 
	 *            Crea un OutputError con el errorLevel, message y code indicados por parametro. Utilizarlo cuando se tenga que retornar un error level distinto
	 *            al codigo de error.
	 */
	public OutputError(int errorLevel, String message, int code) {
		this(errorLevel, message);
		this.trueCode = code;
	}

	/**
	 * @param errorCode
	 *            Codigo de error.
	 * @param e
	 *            Excepcion que provoco el error.
	 * 
	 *            Crea un OutputError y setea el mensaje de error a partir del errorCode o de la excepcion en caso que el primero sea nulo o vacio
	 */
	public OutputError(ErrorCode errorCode, Throwable e) {
		this.code = errorCode.code;
		this.trueCode = code;
		this.message = (Strings.isNullOrEmpty(errorCode.message) ? e.toString() : errorCode.message);
	}

	/**
	 * @param errorCode
	 * 
	 *            Crea un OutputError y setea el mensaje a partir del errorCode
	 */
	public OutputError(ErrorCode errorCode) {
		this.code = errorCode.code;
		this.message = errorCode.message;
		this.trueCode = code;
	}

	/**
	 * Crea un error de output a partir de un codigo de error y un mensaje.
	 * 
	 * @param errorCode
	 *            Codigo de error.
	 * @param message
	 *            Mensaje
	 */
	public OutputError(ErrorCode errorCode, String message) {
		this.code = errorCode.code;
		this.message = message;
		this.trueCode = code;
	}

	public OutputError(ErrorCode errorCode, int trueCode) {
		this.code = errorCode.code;
		this.message = errorCode.message;
		this.trueCode = trueCode;
	}

	@Override
	public String toString() {
		return "OutputError [code=" + code + ", trueCode=" + trueCode + ", message=" + message + "]";
	}

	/**
	 * Retorna true si el codigo de error y el true code son exitosos.
	 * 
	 * @return true si el codigo de error y el true code son exitosos, false en caso contrario.
	 */
	public boolean isSuccess() {
		return this.code == ErrorCode.SUCCESS.code && this.trueCode == ErrorCode.SUCCESS.code;
	}
}
