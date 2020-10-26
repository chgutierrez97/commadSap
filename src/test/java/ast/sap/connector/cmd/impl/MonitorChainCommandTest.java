package ast.sap.connector.cmd.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import ast.sap.connector.chain.processes.ProcessLogPair;
import ast.sap.connector.chain.status.ChainStatusCode;
import ast.sap.connector.chain.status.ChainStatusReaderInput;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.job.track.JobStatusCode;
import ast.sap.connector.xmi.XmiLoginData;

public class MonitorChainCommandTest {
	String logId = "1234567890";
	String chainId = "SOME_CHAIN";

	String processtype1 = "TRIGGER";
	String processcount1 = "123456";

	String processtype2 = "ABAP";
	String processcount2 = "789123";

	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private String tt(int i) {
		return "Texto de prueba " + i;
	}

	private String randStringNo() {
		return "" + ((int) (Math.random() * 1000));
	}

	private SapFunction mockGetJobLogFunction() throws ParseException {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");

		/*
		 * text = entry.getValue("TEXT").toString(); Date date = (Date) entry.getValue("ENTERDATE"); Date time = (Date) entry.getValue("ENTERTIME");
		 */
		OutTableRow outRow = mock(OutTableRow.class);
		when(outRow.getValue("TEXT")).thenReturn(tt(1), tt(2), tt(3), tt(4), tt(5), tt(6), tt(7), tt(8), tt(9), tt(10), tt(11), tt(12), tt(13), tt(14));
		when(outRow.getValue("ENTERDATE")).thenReturn(dateFormat.parse("21/03/1991"), dateFormat.parse("05/12/1964"), dateFormat.parse("18/09/1960"));
		when(outRow.getValue("ENTERTIME")).thenReturn(timeFormat.parse("12:37:48"), timeFormat.parse("12:38:40"), timeFormat.parse("12:40:11"));
		when(outRow.getValue("MSGID")).thenReturn("000", "000", "000", "000", "000", "000", "000", "000", "000", "000");
		when(outRow.getValue("MSGNO")).thenReturn(randStringNo(), randStringNo(), randStringNo(), randStringNo(), randStringNo(), randStringNo());

		OutTableParam outTable = mock(OutTableParam.class);
		when(outTable.currentRow()).thenReturn(outRow);
		when(outTable.getRowCount()).thenReturn(3);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);
		when(result.getOutTableParameter("JOB_PROTOCOL_NEW")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockJobStatusGet(String status) {
		SapStruct retMock = mock(SapStruct.class);
		when(retMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getStructure("RETURN")).thenReturn(retMock);
		when(resultMock.getOutParameterValue("STATUS")).thenReturn(status);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(resultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);

		return functionMock;
	}

	private SapFunction mockChainGetProcesses() {
		OutTableRow row1 = mock(OutTableRow.class);
		when(row1.getValue("TYPE")).thenReturn(processtype1);
		when(row1.getValue("JOB_COUNT")).thenReturn(processcount1);

		OutTableRow row2 = mock(OutTableRow.class);
		when(row2.getValue("TYPE")).thenReturn(processtype2);
		when(row2.getValue("JOB_COUNT")).thenReturn(processcount2);

		OutTableParam table = mock(OutTableParam.class);
		when(table.currentRow()).thenReturn(row1, row2);
		when(table.getRowCount()).thenReturn(2);
		when(table.nextRow()).thenReturn(row2);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutTableParameter("E_T_PROCESSLIST")).thenReturn(table);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockChainGetStatusMock(String chainStatusStrCode) {
		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getOutParameterValue("E_STATUS")).thenReturn(chainStatusStrCode);
		when(resultMock.getOutParameterValue("E_MANUAL_ABORT")).thenReturn("");
		when(resultMock.getOutParameterValue("E_MESSAGE")).thenReturn("");

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenReturn(resultMock);

		return functionMock;
	}

	private SapFunction mockChainStart() {
		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getOutParameterValue("E_LOGID")).thenReturn(logId);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenReturn(resultMock);

		return functionMock;
	}

	@Test
	public void testPerform() throws ParseException {
		SapRepository repoMock = mock(SapRepository.class);

		SapFunction chainStartMock = mockChainStart();
		when(repoMock.getFunction("RSPC_API_CHAIN_START")).thenReturn(chainStartMock);

		SapFunction chainGetStatusMock1 = mockChainGetStatusMock("Y");
		SapFunction chainGetStatusMock2 = mockChainGetStatusMock("A");
		SapFunction chainGetStatusMock3 = mockChainGetStatusMock("G");
		when(repoMock.getFunction("RSPC_API_CHAIN_GET_STATUS")).thenReturn(chainGetStatusMock1, chainGetStatusMock2, chainGetStatusMock3);

		SapFunction chainGetProcessesMock = mockChainGetProcesses();
		when(repoMock.getFunction("RSPC_API_CHAIN_GET_PROCESSES")).thenReturn(chainGetProcessesMock);

		SapFunction jobStatusGetMock1 = mockJobStatusGet("F");
		SapFunction jobStatusGetMock2 = mockJobStatusGet("A");
		when(repoMock.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(jobStatusGetMock1, jobStatusGetMock2);

		SapFunction joblogReadMock1 = mockGetJobLogFunction();
		SapFunction joblogReadMock2 = mockGetJobLogFunction();
		when(repoMock.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(joblogReadMock1, joblogReadMock2);

		XmiLoginData xmiLoginData = new XmiLoginData();
		String username = "mzaragoz";
		MonitorChainCommand monitorChainCommand = new MonitorChainCommand(repoMock, xmiLoginData, chainId, username);

		/* FIN DE MOCK ---------------------------------------------------------------------------------------------------------------------------- */

		SapCommandResult result = monitorChainCommand.perform();
		assertTrue(result.getChainStatus().isPresent());
		assertEquals(ChainStatusCode.G, result.getChainStatus().get());
		
		assertTrue(result.getProcessLogPairs().isPresent());
		assertEquals(2, result.getProcessLogPairs().get().size());
		
		ProcessLogPair processLogPair1 = result.getProcessLogPairs().get().get(0);
		assertEquals(JobStatusCode.F, processLogPair1.getJobFullStatus().getJobStatus().getStatusCode());
		
		ProcessLogPair processLogPair2 = result.getProcessLogPairs().get().get(1);
		assertEquals(JobStatusCode.A, processLogPair2.getJobFullStatus().getJobStatus().getStatusCode());
	}

	public static void main(String[] args) {
		System.out.println(new ChainStatusReaderInput("pepe", "1234").equals(new ChainStatusReaderInput("pepe", "1234")));
	}
}
