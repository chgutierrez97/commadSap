package ast.sap.connector.misc;

import static org.junit.Assert.*;

import org.junit.Test;

public class VarArgsTest {

	@Test
	public void test() {
		assertTrue(func(true));
		assertFalse(func());
	}

	private boolean func(boolean...printContinuously) {
		return printContinuously.length > 0 && printContinuously[0];
	}
}
