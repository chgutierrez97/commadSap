package ast.sap.connector.chain.status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ChainStatusCodeTest {

	@Test
	public void test() {
		assertEquals(ChainStatusCode.U, ChainStatusCode.fromCode(""));
		assertEquals(ChainStatusCode.U, ChainStatusCode.fromCode("asdadasdadsd"));

		assertEquals(ChainStatusCode.F, ChainStatusCode.fromCode("F"));
	}
	
	@Test
	public void testIsFinished() {
		assertTrue(ChainStatusCode.fromCode("F").hasFinished());
		assertTrue(ChainStatusCode.fromCode("G").hasFinished());
		assertFalse(ChainStatusCode.fromCode("P").hasFinished());
		assertTrue(ChainStatusCode.fromCode("X").hasFinished());
		assertFalse(ChainStatusCode.fromCode("X").hasFinishedSuccessfully());
		
	}

}
