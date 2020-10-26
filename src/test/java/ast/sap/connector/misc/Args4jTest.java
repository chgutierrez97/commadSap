package ast.sap.connector.misc;

import org.junit.Assert;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Args4jTest {
	
	@Test
	public void testArgs4jparser() throws CmdLineException {
		String[] args = {
				"-u",
				"mzaragoza",
				"-c",
				"RUN_JOB"
		};
		
		Args4jParser args4jParser = new Args4jParser(args);
		CmdLineParser parser = new CmdLineParser(args4jParser);
		
		parser.parseArgument(args);

		Assert.assertEquals(args[1], args4jParser.getUser());
		Assert.assertEquals(args[3], args4jParser.getCommand());
	}
}
