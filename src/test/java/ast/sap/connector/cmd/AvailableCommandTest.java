package ast.sap.connector.cmd;

import org.junit.Test;

public class AvailableCommandTest {

	@Test(expected = IllegalArgumentException.class)
	public void test() {
		AvailableCommand.valueOf("NONEXISTENT_COMMAND");
	}

}
