package ast.sap.connector.chain.processes;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProcessStatusCodeTest {

	@Test
	public void testFromCode() {
		ProcessStatusCode processCode1 = ProcessStatusCode.fromCode("S");
		ProcessStatusCode processCode2 = ProcessStatusCode.fromCode("W");
		ProcessStatusCode processCode3 = ProcessStatusCode.fromCode(" ");
		ProcessStatusCode processCode4 = ProcessStatusCode.fromCode("");
		assertEquals("Skipped at restart", processCode1.getLabel());
		assertEquals("Undefined", processCode2.getLabel());
		assertEquals("Undefined", processCode3.getLabel());
		assertEquals("Undefined", processCode4.getLabel());
	}

	@Test
	public void testIsError() {
		ProcessStatusCode endedWithError = ProcessStatusCode.R;
		ProcessStatusCode successful = ProcessStatusCode.G;
		ProcessStatusCode completed = ProcessStatusCode.F;
		ProcessStatusCode active = ProcessStatusCode.A;
		ProcessStatusCode cancelled = ProcessStatusCode.X;
		ProcessStatusCode planned = ProcessStatusCode.P;
		ProcessStatusCode skipped = ProcessStatusCode.S;
		ProcessStatusCode released = ProcessStatusCode.Q;
		ProcessStatusCode ready = ProcessStatusCode.Y;
		ProcessStatusCode undefined = ProcessStatusCode.U;
		ProcessStatusCode endedWithErrorJob = ProcessStatusCode.J;
		assertTrue(cancelled.isError());
		assertTrue(endedWithError.isError());
		assertTrue(endedWithErrorJob.isError());
		assertFalse(successful.isError());
		assertFalse(completed.isError());
		assertFalse(active.isError());
		assertFalse(planned.isError());
		assertFalse(skipped.isError());
		assertFalse(released.isError());
		assertFalse(ready.isError());
		assertFalse(undefined.isError());
		
	}
}
