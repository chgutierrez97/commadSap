package ast.sap.connector.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Optional;

import ast.sap.connector.chain.processes.ProcessEntry;
import ast.sap.connector.chain.processes.ProcessLogPair;
import ast.sap.connector.chain.status.ChainStatusCode;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.track.JobFullStatus;
import ast.sap.connector.job.track.JobStatus;
import ast.sap.connector.job.track.JobStatusCode;

public class OutputParserTest {

	@Test
	public void testParseOutputBapiRet() {
		SapBapiret2 bapiret2 = mock(SapBapiret2.class);
		when(bapiret2.getNumber()).thenReturn(123);
		when(bapiret2.getMessage()).thenReturn("PRUEBA OUTPUT PARSER");

		SapCommandResult sapCommandResult = new SapCommandResult(bapiret2);
		OutputError parsedOutput = OutputParser.INSTANCE.parseOutput(sapCommandResult);
		System.out.println("Mensaje: " + parsedOutput.getMessage());
		System.out.println("Codigo de error: " + parsedOutput.getCode() + " ; " + parsedOutput.getTrueCode());
		assertEquals(bapiret2.getMessage(), parsedOutput.getMessage());
		assertEquals(bapiret2.getNumber(), parsedOutput.getCode());

	}

	@Test
	public void testParseOutputStatus() {
		JobFullStatus jobFullStatus = mock(JobFullStatus.class);
		JobStatus jobStatus = mock(JobStatus.class);
		JobLog jobLog = mock(JobLog.class);

		when(jobStatus.getStatusCode()).thenReturn(JobStatusCode.Z);
		when(jobFullStatus.getJobStatus()).thenReturn(jobStatus);
		when(jobFullStatus.getJobLog()).thenReturn(jobLog);

		SapCommandResult sapCommandResult = new SapCommandResult(jobFullStatus);
		OutputError parsedOutput = OutputParser.INSTANCE.parseOutput(sapCommandResult);
		System.out.println("Mensaje: " + parsedOutput.getMessage());
		System.out.println("Codigo de error: " + parsedOutput.getCode() + " ; " + parsedOutput.getTrueCode());
		assertEquals(ErrorCode.UNFINISHED_JOB.code, parsedOutput.getCode());
		assertEquals(ErrorCode.UNFINISHED_JOB.message, parsedOutput.getMessage());
	}

	@Test
	public void testParseOutputMessage() {
		SapCommandResult sapCommandResult = new SapCommandResult("PRUEBA");
		OutputError parsedOutput = OutputParser.INSTANCE.parseOutput(sapCommandResult);
		System.out.println("Mensaje: " + parsedOutput.getMessage());
		System.out.println("Codigo de error: " + parsedOutput.getCode() + " ; " + parsedOutput.getTrueCode());
		assertEquals(ErrorCode.UNKNOWN.code, parsedOutput.getCode());
	}

	@Test
	public void testParseOutputChainFinishedWithError() {
		SapCommandResult sapCommandResult = mock(SapCommandResult.class);
		String str = null;
		when(sapCommandResult.getChainLogId()).thenReturn(Optional.fromNullable(str));
		when(sapCommandResult.getChainStatus()).thenReturn(Optional.fromNullable(ChainStatusCode.R));

		OutputError parsedOutput = OutputParser.INSTANCE.parseOutput(sapCommandResult);

		System.out.println("Mensaje: " + parsedOutput.getMessage());
		System.out.println("Codigo de error: " + parsedOutput.getCode() + " ; " + parsedOutput.getTrueCode());

		assertEquals(ErrorCode.SUCCESS.code, parsedOutput.getCode());
		assertEquals(ErrorCode.UNFINISHED_CHAIN.code, parsedOutput.getTrueCode());
		// assertEquals(ErrorCode.UNFINISHED_CHAIN.message, parsedOutput.getMessage());
		assertTrue(parsedOutput.getMessage().startsWith("Procesos de cadena con error"));
	}

	@Test
	public void testParseOutputChainJobIncompleteId() {
		SapCommandResult sapCommandResult = mock(SapCommandResult.class);
		String str = null;
		JobStatus jobStatus = new JobStatus("X", mock(SapBapiret2.class));
		JobFullStatus jobFullStatus = new JobFullStatus(mock(JobLog.class), jobStatus);
		ProcessLogPair processLogPair = new ProcessLogPair(mock(ProcessEntry.class), jobFullStatus);
		
		List<ProcessLogPair> processLogPairList = Collections.singletonList(processLogPair);

		when(sapCommandResult.getChainLogId()).thenReturn(Optional.fromNullable(str));
		when(sapCommandResult.getChainStatus()).thenReturn(Optional.fromNullable(ChainStatusCode.G));
		when(sapCommandResult.getProcessLogPairs()).thenReturn(Optional.fromNullable(processLogPairList));

		OutputError parsedOutput = OutputParser.INSTANCE.parseOutput(sapCommandResult);

		assertEquals(ErrorCode.SUCCESS.code, parsedOutput.getCode());
		assertEquals(ErrorCode.UNFINISHED_CHAIN_JOB.code, parsedOutput.getTrueCode());
		assertTrue(parsedOutput.getMessage().startsWith(ErrorCode.UNFINISHED_CHAIN_JOB.message));
		System.out.println(parsedOutput.toString());

	}

}
