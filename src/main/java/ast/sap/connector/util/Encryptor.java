package ast.sap.connector.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidades de encriptacion. La logica se copio del modulo de encriptacion de probatch.
 * 
 * @author martin.zaragoza
 * 
 */
public enum Encryptor {
	INSTANCE;

	private static final String DEFAULT_KEY = "MPBKeyED";
	private static final String DEFAULT_ID = "3434996";

	private static final String ENCODING = "UTF-8";
	Logger logger = LoggerFactory.getLogger(Encryptor.class);

	/**
	 * Encripta un string.
	 * 
	 * @param key
	 *            - Clave a usar para encriptar texto.
	 * @param id
	 *            - Texto a concatenar para encriptar.
	 * @param plainText
	 *            - Texto plano a encriptar.
	 * @return Texto encriptado.
	 */
	public String encrypt(String key, String id, String plainText) {
		try {
			byte[] plainTextBytes = plainText.getBytes(ENCODING);
			String plainTextBytesString = byte2hex(plainTextBytes);

			byte[] encBytes = xorstr(key, id, plainTextBytesString);
			String encString = byte2hex(encBytes);

			return encString;
		} catch (UnsupportedEncodingException e) {
			logger.error("Error al encodear la clave con " + ENCODING, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Desencripta un string.
	 * 
	 * @param key
	 *            - Clave a usar para desencriptar texto.
	 * @param id
	 *            - Texto a concatenar para desencriptar.
	 * @param encString
	 *            - Texto encriptado a desencriptar.
	 * @return Texto desencriptado.
	 */
	public String decrypt(String key, String id, String encString) {
		try {
			byte[] decBytes = xorstr(key, id, encString);
			return new String(decBytes, ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error("Error al desencriptar la clave con " + ENCODING, e);
			throw new RuntimeException(e);
		}
	}

	public String encrypt(String plainText) {
		return encrypt(DEFAULT_KEY, DEFAULT_ID, plainText);
	}

	public String decrypt(String encString) {
		return decrypt(DEFAULT_KEY, DEFAULT_ID, encString);
	}

	/**
	 * Xor para desencriptar o encriptar clave.
	 * 
	 * @param key
	 *            - Clave en arch de config.
	 * @param id
	 *            - id a concatenar.
	 * @param arg
	 *            - Texto encriptado o a encriptar.
	 * @return
	 */
	byte[] xorstr(String key, String id, String arg) {
		byte[] res = null;
		byte[] decryptArg = hex2Byte(arg);
		byte messageDigest[];
		byte[] s;

		String key_id = key + id;

		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(key_id.getBytes());

			messageDigest = algorithm.digest();

			int la = decryptArg.length;
			int lk = messageDigest.length;

			int tamS = (messageDigest.length * (la / lk)) + (la % lk);
			s = new byte[tamS];

			int base = 0;
			for (int i = 0; i < (la / lk); i++) {
				base = messageDigest.length * i;
				for (int j = 0; j < messageDigest.length; j++)
					s[base + j] = messageDigest[j];

			}

			base = tamS - (la % lk);
			for (int j = 0; j < (la % lk); j++)
				s[base + j] = messageDigest[j];

			int minimo = Math.min(decryptArg.length, s.length);

			res = new byte[minimo];

			for (int i = 0; i < minimo; i++) {
				res[i] = (byte) (decryptArg[i] ^ s[i]);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			System.exit(5);
		}

		return res;
	}

	/**
	 * Convierte un arreglo de bytes en un String con caracteres hexadecimales.
	 * 
	 * @param b
	 *            - Arreglo de bytes a parsear.
	 * @return String con caracteres hexadecimales.
	 */
	String byte2hex(byte[] b) {
		// String Buffer can be used instead
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1) {
				hs = hs + "%" + "0" + stmp;
			} else {
				hs = hs + "%" + stmp;
			}

			if (n < b.length - 1) {
				hs = hs + "";
			}
		}

		return hs;
	}

	byte[] hex2Byte(String str) {
		String str1 = str.replaceAll("%", "");
		byte[] bytes = new byte[str1.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str1.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

}
