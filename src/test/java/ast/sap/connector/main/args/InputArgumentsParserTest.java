package ast.sap.connector.main.args;

import static org.junit.Assert.*;

import org.junit.Test;

import ast.sap.connector.cmd.AvailableCommand;

public class InputArgumentsParserTest {
	@Test
	public void testParseFull() {
		String[] args = {
				"-n", "500",
				"-c", "RUN_JOB",
				"-h", "127.0.0.1",
				"-j", "XBP_TEST_1",
				"-p", "Julio2017",
				"-s", "01",
				"-u", "martin.zaragoza",
				"-i", "14201001",
				"-x", "SERVER",
				"-l", "ES",
				"-t", "SOME_STEP",
				"-v", "SOME_VARIANT" };

		InputArgumentsData inputArgs = InputArgumentsParser.INSTANCE.parse(args);

		assertEquals("500", inputArgs.getClientNumber());
		assertEquals(AvailableCommand.RUN_JOB, inputArgs.getCommand());
		assertEquals("127.0.0.1", inputArgs.getHost());
		assertEquals("XBP_TEST_1", inputArgs.getJobName());
		assertEquals("Julio2017", inputArgs.getPassword());
		assertEquals("01", inputArgs.getSystemNumber());
		assertEquals("martin.zaragoza", inputArgs.getUser());
		assertEquals("14201001", inputArgs.getJobId());
		assertEquals("SERVER", inputArgs.getExecServer());
		assertEquals("ES", inputArgs.getLanguage());
		assertEquals("SOME_STEP", inputArgs.getSingleStep());
		assertEquals("SOME_VARIANT", inputArgs.getSingleVariant());
	}

	@Test()
	public void testParseWrongCommandAndGetHelp() {
		String[] noCommandArgs = {
				"-n", "500",
				"-c", "NONEXISTENT_COMMAND",
				"-h", "127.0.0.1",
				"-j", "XBP_TEST_1",
				"-p", "Julio2017",
				"-s", "01",
				"-u", "martin.zaragoza",
				"-i", "14201001",
				"-x", "SERVER",
				"-l", "ES",
				"-t", "SOME_STEP",
				"-v", "SOME_VARIANT" };

		InputArgumentsData inputArgs = InputArgumentsParser.INSTANCE.parse(noCommandArgs);
		assertTrue(inputArgs.getCommand().equals(InputArgumentsParser.DEFAULT_COMMAND));
	}
	
	@Test()
	public void testParseNoCommandAndGetHelp() {
		String[] noCommandArgs = {
				"-n", "500",
				"-h", "127.0.0.1",
				"-j", "XBP_TEST_1",
				"-p", "Julio2017",
				"-s", "01",
				"-u", "martin.zaragoza",
				"-i", "14201001",
				"-x", "SERVER",
				"-l", "ES",
				"-t", "SOME_STEP",
				"-v", "SOME_VARIANT" };

		InputArgumentsData inputArgs = InputArgumentsParser.INSTANCE.parse(noCommandArgs);
		assertTrue(inputArgs.getCommand().equals(InputArgumentsParser.DEFAULT_COMMAND));
	}

	@Test(expected = InputArgsParseException.class)
	public void testParseFailMissingVariantFieldValue() {
		String[] noCommandArgs = {
				"-n", "500",
				"-h", "127.0.0.1",
				"-c", "CREATE_MONITOR_JOB",
				"-j", "XBP_TEST_1",
				"-p", "Julio2017",
				"-s", "01",
				"-u", "martin.zaragoza",
				"-i", "14201001",
				"-t", "SOME_STEP",
				"-v", "SOME_VARIANT",
				"-vk1", "P_FECHA" };

		InputArgumentsParser.INSTANCE.parse(noCommandArgs);
	}

	@Test(expected = InputArgsParseException.class)
	public void testParseFailMissingVariantFieldKey() {
		String[] noCommandArgs = {
				"-n", "500",
				"-h", "127.0.0.1",
				"-c", "CREATE_MONITOR_JOB",
				"-j", "XBP_TEST_1",
				"-p", "Julio2017",
				"-s", "01",
				"-u", "martin.zaragoza",
				"-i", "14201001",
				"-t", "SOME_STEP",
				"-v", "SOME_VARIANT",
				"-vv1", "21/03/1991" };

		InputArgumentsParser.INSTANCE.parse(noCommandArgs);
	}

	@Test
	public void testParseWithVariantFields() {
		String[] args = {
				"-n", "500",
				"-c", "RUN_JOB",
				"-h", "127.0.0.1",
				"-j", "XBP_TEST_1",
				"-p", "Julio2017",
				"-s", "01",
				"-u", "martin.zaragoza",
				"-i", "14201001",
				"-x", "SERVER",
				"-l", "ES",
				"-t", "SOME_STEP",
				"-v", "SOME_VARIANT",
				"-vk1", "P_FECHA",
				"-vv1", "21/03/1991" };

		InputArgumentsParser.INSTANCE.parse(args);
	}

	@Test(expected = InputArgsParseException.class)
	public void testParseFailWrongClientNumber() {
		String[] args = {
				"-n", "ASD",
				"-c", "RUN_JOB" };

		InputArgumentsParser.INSTANCE.parse(args);
	}

	@Test(expected = InputArgsParseException.class)
	public void testParseRunJobCommandWithMissingJobNameAndFailAtValidation() {
		String[] args = {
				"-c", "RUN_JOB",
				"-i", "14201001",
		};
		InputArgumentsParser.INSTANCE.parse(args);
	}

	@Test(expected = InputArgsParseException.class)
	public void testParseRunJobCommandWithMissingJobIdAndFailAtValidation() {
		String[] args = {
				"-c", "RUN_JOB",
				"-j", "MY_JOB,"
		};
		InputArgumentsParser.INSTANCE.parse(args);
	}

	@Test
	public void testParseCreateRunJobCommandJustFine() {
		String[] args = {
				"-c", AvailableCommand.CREATE_RUN_JOB.toString(),
				"-j", "MY_JOB",
				"-t", "MY_STEP"
		};
		InputArgumentsData inputargs = InputArgumentsParser.INSTANCE.parse(args);
		assertEquals(args[3], inputargs.getJobName());
		assertEquals(AvailableCommand.CREATE_RUN_JOB, inputargs.getCommand());
		assertEquals(args[5], inputargs.getSingleStep());
	}
	
	@Test
	public void testParseCreateMonitorJobCommandJustFine() {
		String[] args = {
				"-c", AvailableCommand.CREATE_MONITOR_JOB.toString(),
				"-j", "MY_JOB",
				"-t", "MY_STEP"
		};
		InputArgumentsData inputargs = InputArgumentsParser.INSTANCE.parse(args);
		assertEquals(args[3], inputargs.getJobName());
		assertEquals(AvailableCommand.CREATE_MONITOR_JOB, inputargs.getCommand());
		assertEquals(args[5], inputargs.getSingleStep());
		
		assertTrue(inputargs.getVariantKeyValuePairs().isEmpty());
	}
	
	@Test
	public void testParseCreateMonitorJobCommandWithVariantFieldsJustFine() {
		String[] args = {
				"-c", AvailableCommand.CREATE_MONITOR_JOB.toString(),
				"-j", "MY_JOB",
				"-t", "MY_STEP",
				"-v", "MY_VARIANT",
				"-vk1", "P_FECHA",
				"-vv1", "21/03/1991"
		};
		InputArgumentsData inputargs = InputArgumentsParser.INSTANCE.parse(args);
		assertEquals(args[3], inputargs.getJobName());
		assertEquals(AvailableCommand.CREATE_MONITOR_JOB, inputargs.getCommand());
		assertEquals(args[5], inputargs.getSingleStep());
		
		assertEquals(args[7], inputargs.getSingleVariant());
		assertEquals(args[9], inputargs.getVariantKeyValuePairs().get(0).key);
		assertEquals(args[11], inputargs.getVariantKeyValuePairs().get(0).value);
		
		assertEquals(1, inputargs.getVariantKeyValuePairs().size());
	}
	
	@Test
	public void testParseCreateMonitorJobCommandWithMoreVariantFieldsJustFine() {
		String[] args = {
				"-c", AvailableCommand.CREATE_MONITOR_JOB.toString(),
				"-j", "MY_JOB",
				"-t", "MY_STEP",
				"-v", "MY_VARIANT",
				"-vk1", "P_FECHA",
				"-vv1", "21/03/1991",
				"-vk2", "P_NUMBER",
				"-vv2", "12345"
		};
		InputArgumentsData inputargs = InputArgumentsParser.INSTANCE.parse(args);
		assertEquals(args[3], inputargs.getJobName());
		assertEquals(AvailableCommand.CREATE_MONITOR_JOB, inputargs.getCommand());
		assertEquals(args[5], inputargs.getSingleStep());
		
		assertEquals(args[7], inputargs.getSingleVariant());
		assertEquals(args[9], inputargs.getVariantKeyValuePairs().get(0).key);
		assertEquals(args[11], inputargs.getVariantKeyValuePairs().get(0).value);
		
		assertEquals(args[13], inputargs.getVariantKeyValuePairs().get(1).key);
		assertEquals(args[15], inputargs.getVariantKeyValuePairs().get(1).value);
		
		assertEquals(2, inputargs.getVariantKeyValuePairs().size());
	}
}
