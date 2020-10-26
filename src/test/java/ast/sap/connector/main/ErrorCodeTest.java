package ast.sap.connector.main;

import org.junit.Assert;
import org.junit.Test;

public class ErrorCodeTest {

	@Test 
	public void testFromCode() {
		int code = 50;
		ErrorCode error = ErrorCode.getError(code);
		Assert.assertEquals(code, error.code);
	}
	
	
	@Test
	public void testFromCodeWithUnknownCode() {
		int code = 20000;
		ErrorCode error = ErrorCode.getError(code);
		Assert.assertNotEquals(code, error.code);
	}
	
	
	@Test
	public void testFromName(){
		String key = "INSUFFICIENT_CREDENTIALS";
		ErrorCode error = ErrorCode.getError(key);
		Assert.assertEquals(ErrorCode.INSUFFICIENT_CREDENTIALS.code, error.code);
	}

}
