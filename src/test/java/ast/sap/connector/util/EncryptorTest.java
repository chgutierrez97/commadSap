package ast.sap.connector.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptorTest {
	private static final Encryptor ENCRYPTOR = Encryptor.INSTANCE;

	@Test
	public void testEncrypt() {
		{
			String key = "MQCKeyDF";
			String id = "3434567";
			String plainText = "Agosto2017";

			String encString = ENCRYPTOR.encrypt(key, id, plainText);
			String decString = ENCRYPTOR.decrypt(key, id, encString);

			assertEquals(plainText, decString);
		}
		
		{
			String plainText = "Agosto2017";

			String encString = ENCRYPTOR.encrypt(plainText);
			String decString = ENCRYPTOR.decrypt(encString);

			assertEquals(plainText, decString);
		}
	}
}
