package ast.sap.connector.main;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class OutputErrorTest {

	@Test
	public void testIsSuccess() {
		OutputError outputError = new OutputError(ErrorCode.SUCCESS);
		assertEquals(outputError.getCode(),outputError.getTrueCode());
		assertTrue(outputError.isSuccess());
	}
	
	@Test
	public void testIsSuccessUnfinishedChain() {
		OutputError outputError = new OutputError(ErrorCode.SUCCESS.code, "aaaaaa", ErrorCode.UNFINISHED_CHAIN.code);
		assertNotEquals(outputError.getCode(),outputError.getTrueCode());
		assertFalse(outputError.isSuccess());
	}
	
	@Test
	public void testIsSuccessUnfinishedChainJob() {
		OutputError outputError = new OutputError(ErrorCode.UNFINISHED_CHAIN_JOB);
		assertEquals(outputError.getCode(),outputError.getTrueCode());
		assertFalse(outputError.isSuccess());
	}
	
	
	@Test
	public void testIsSuccessCustomTrueCode() {
		int trueCode = 34;
		OutputError outputError = new OutputError(ErrorCode.CANNOT_RUN_JOB, trueCode);
		assertTrue(trueCode != ErrorCode.CANNOT_RUN_JOB.code);
		assertFalse(outputError.isSuccess());
	}
	
}
