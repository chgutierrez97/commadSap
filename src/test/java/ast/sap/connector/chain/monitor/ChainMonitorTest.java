package ast.sap.connector.chain.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import com.sap.conn.jco.JCoException;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.status.ChainStatusCode;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.dst.exception.NonexistentFunctionException;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;

public class ChainMonitorTest {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	@Test
	public void testMonitorChainWithReconnect() throws FunctionGetFailException, NonexistentFunctionException, ParseException, JCoException {
		SapFunction getChainStatusFunction = mockGetChainStatusWithException();
		SapFunction getChainProcessesFunction = mockSimpleProcessesReader();
		SapFunction getStatusFunction = mockGetStatusFunction();
		SapFunction getJobLogFunction = mockGetJobLogFunction();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_STATUS")).thenReturn(getChainStatusFunction);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_PROCESSES")).thenReturn(getChainProcessesFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(getStatusFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		/* FIN DE MOCKS ------------------------------------------------------------------------------------------------------------------------ */

		ChainMonitor chainMonitor = new ChainMonitor(sapRepository);
		chainMonitor.doSleep = false;
		chainMonitor.doReconnect = false;
		String chainName = "Z_CHAIN_S_EVENT";
		String logId = "123456";
		String username = "mzaragoz";

		ChainMonitor spy = spy(chainMonitor);

		ChainFullStatus chainFullStatus = spy.monitorChain(new ChainData(logId, chainName), username);

		assertTrue(chainFullStatus.getChainStatus().getStatus().hasFinished());
		verify(spy, times(2)).reconnect();
	}

	@Test
	public void testMonitorChain() throws ParseException {
		SapFunction getChainStatusFunction = mockGetChainStatus();
		SapFunction getChainProcessesFunction = mockSimpleProcessesReader();
		SapFunction getStatusFunction = mockGetStatusFunction();
		SapFunction getJobLogFunction = mockGetJobLogFunction();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_STATUS")).thenReturn(getChainStatusFunction);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_PROCESSES")).thenReturn(getChainProcessesFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(getStatusFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		/* FIN DE MOCKS ------------------------------------------------------------------------------------------------------------------------ */

		ChainMonitor chainMonitor = new ChainMonitor(sapRepository);
		chainMonitor.doSleep = false;
		String chainName = "Z_CHAIN_S_EVENT";
		String logId = "123456";
		String username = "mzaragoz";
		ChainFullStatus chainFullStatus = chainMonitor.monitorChain(new ChainData(logId, chainName), username);

		System.out.println(chainFullStatus);
		assertEquals(ChainStatusCode.G, chainFullStatus.getChainStatus().getStatus());
		assertEquals(2, chainFullStatus.getProcessLogPairs().size());
		assertEquals(2, chainFullStatus.getProcessLogPairs().get(0).getJobLog().getLogEntries().size());
		assertEquals(3, chainFullStatus.getProcessLogPairs().get(1).getJobLog().getLogEntries().size());

	}

	@Test
	public void testMonitorChainWithNestedChains() throws ParseException {
		SapFunction getChainStatusFunction = mockGetChainStatus();
		SapFunction getChainProcessesFunction = mockSimpleProcessesReader();
		SapFunction getChildChainProcessesFunction = mockNestedChainProcessesReader();
		SapFunction getStatusFunction = mockGetStatusFunction();
		SapFunction getJobLogFunction = mockGetJobLogFunction();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_STATUS")).thenReturn(getChainStatusFunction);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_PROCESSES")).thenReturn(getChildChainProcessesFunction, getChildChainProcessesFunction,
				getChainProcessesFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(getStatusFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		/* FIN DE MOCKS ------------------------------------------------------------------------------------------------------------------------ */

		ChainMonitor chainMonitor = new ChainMonitor(sapRepository);
		chainMonitor.doSleep = false;
		String chainName = "Z_CHAIN_S_EVENT";
		String logId = "123456";
		String username = "mzaragoz";
		ChainFullStatus chainFullStatus = chainMonitor.monitorChain(new ChainData(logId, chainName), username);

		System.out.println(chainFullStatus);
		assertEquals(ChainStatusCode.G, chainFullStatus.getChainStatus().getStatus());
		assertEquals(7, chainFullStatus.getProcessLogPairs().size());
		assertEquals(2, chainFullStatus.getProcessLogPairs().get(0).getJobLog().getLogEntries().size());
		assertEquals(3, chainFullStatus.getProcessLogPairs().get(1).getJobLog().getLogEntries().size());
		assertEquals(6, chainFullStatus.getProcessLogPairs().get(2).getJobLog().getLogEntries().size());
		assertEquals(7, chainFullStatus.getProcessLogPairs().get(3).getJobLog().getLogEntries().size());
		assertEquals(5, chainFullStatus.getProcessLogPairs().get(4).getJobLog().getLogEntries().size());
		assertEquals(4, chainFullStatus.getProcessLogPairs().get(5).getJobLog().getLogEntries().size());
		assertEquals(9, chainFullStatus.getProcessLogPairs().get(6).getJobLog().getLogEntries().size());

	}

	private String tt(int i) {
		return "Texto de prueba " + i;
	}

	private String randStringNo() {
		return "" + ((int) (Math.random() * 1000));
	}

	private SapFunction mockGetJobLogFunction() throws ParseException {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");

		OutTableRow outRow = mock(OutTableRow.class);
		when(outRow.getValue("TEXT")).thenReturn(tt(1), tt(2), tt(3), tt(4), tt(5), tt(6), tt(7), tt(8), tt(9), tt(10), tt(11), tt(12), tt(13), tt(14));
		when(outRow.getValue("ENTERDATE")).thenReturn(dateFormat.parse("21/03/1991"), dateFormat.parse("05/12/1964"), dateFormat.parse("18/09/1960"));
		when(outRow.getValue("ENTERTIME")).thenReturn(timeFormat.parse("12:37:48"), timeFormat.parse("12:38:40"), timeFormat.parse("12:40:11"));
		when(outRow.getValue("MSGID")).thenReturn(randStringNo(), randStringNo(), randStringNo(), randStringNo(), randStringNo(), randStringNo(),
				randStringNo(), randStringNo(), randStringNo(), randStringNo());
		when(outRow.getValue("MSGNO")).thenReturn(randStringNo(), randStringNo(), randStringNo(), randStringNo(), randStringNo(), randStringNo());
		when(outRow.getValue("MSGTYPE")).thenReturn("S", "S", "S", "S", "A", "S", "S", "S", "S", "S", "S");

		OutTableParam outTable = mock(OutTableParam.class);
		when(outTable.currentRow()).thenReturn(outRow);
		when(outTable.getRowCount()).thenReturn(2, 3, 6, 7, 5, 4, 9);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);
		when(result.getOutTableParameter("JOB_PROTOCOL_NEW")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockGetStatusFunction() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutParameterValue("STATUS")).thenReturn("S").thenReturn("R").thenReturn("R").thenReturn("F");
		when(result.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockGetChainStatus() {
		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutParameterValue("E_STATUS")).thenReturn("G");
		when(result.getOutParameterValue("E_MANUAL_ABORT")).thenReturn("");
		when(result.getOutParameterValue("E_MESSAGE")).thenReturn("");

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockSimpleProcessesReader() throws ParseException {
		OutTableRow outRow = mock(OutTableRow.class);
		when(outRow.getValue("CHAIN_ID")).thenReturn(tt(1), tt(2));
		when(outRow.getValue("EVENT_START")).thenReturn("000", "000");
		when(outRow.getValue("EVENTP_START")).thenReturn("000", "000");
		when(outRow.getValue("EVENTNO_START")).thenReturn("000", "000");
		when(outRow.getValue("BACKLINK_START")).thenReturn("000", "000");
		when(outRow.getValue("TYPE")).thenReturn("TRIGGER", "ABAP");
		when(outRow.getValue("VARIANTE")).thenReturn("Z_CHAIN_S_EVENT_VAR", "Z_CHAIN_S_EVENT_PROG");
		when(outRow.getValue("PREDECESSOR")).thenReturn("000", "000");
		when(outRow.getValue("INSTANCE")).thenReturn("5BD22E0FNMOCACDQHXNUDZGU2", "5BD22GCXG7B4Z8BOA4DLGL2PM");
		when(outRow.getValue("STATE")).thenReturn("000", "000");
		when(outRow.getValue("EVENT_END")).thenReturn("000", "000");
		when(outRow.getValue("EVENTP_END")).thenReturn("000", "000");
		when(outRow.getValue("BACKLINK_END")).thenReturn("000", "000");
		when(outRow.getValue("ACTUAL_STATE")).thenReturn("000", "000");
		when(outRow.getValue("EVENT_GREEN")).thenReturn("000", "000");
		when(outRow.getValue("EVENTP_GREEN")).thenReturn("000", "000");
		when(outRow.getValue("BACKLINK_GREEN")).thenReturn("000", "000");
		when(outRow.getValue("EVENT_RED")).thenReturn("000", "000");
		when(outRow.getValue("EVENTP_RED")).thenReturn("000", "000");
		when(outRow.getValue("BACKLINK_RED")).thenReturn("000", "000");
		when(outRow.getValue("GREEN_EQ_RED")).thenReturn("000", "000");
		when(outRow.getValue("WAIT")).thenReturn("000", "000");
		when(outRow.getValue("STARTTIMESTAMP")).thenReturn("000", "000");
		when(outRow.getValue("ENDTIMESTAMP")).thenReturn("000", "000");
		when(outRow.getValue("JOB_COUNT")).thenReturn("27502800", "27502900");

		OutTableParam outTable = mock(OutTableParam.class);
		when(outTable.currentRow()).thenReturn(outRow, outRow);
		when(outTable.getRowCount()).thenReturn(2);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutTableParameter("E_T_PROCESSLIST")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockNestedChainProcessesReader() throws ParseException {
		OutTableRow outRow = mock(OutTableRow.class);
		when(outRow.getValue("CHAIN_ID")).thenReturn(tt(1), tt(2));
		when(outRow.getValue("EVENT_START")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENTP_START")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENTNO_START")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("BACKLINK_START")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("TYPE")).thenReturn("TRIGGER", "CHAIN", "TRIGGER", "CHAIN", "ABAP");
		when(outRow.getValue("VARIANTE")).thenReturn("Z_CHAIN_IN_CHAIN_EVENT_VAR", "Z_CHAIN_S_EVENT", "Z_CHAIN_S_EVENT_VAR", "Z_CHAIN_S_EVENT", "Z_PROG_ABAP");
		when(outRow.getValue("PREDECESSOR")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("INSTANCE")).thenReturn("5BP5BLTT9TF700P486581X111", "5BOUSCOS8CKN78FYZ3E3QH222", "5BP5BLTT9TF700P486581X333",
				"5BP5BLTT9TF700P486581X444", "5BP5BLTT9TF700P486581X555");
		when(outRow.getValue("STATE")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENT_END")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENTP_END")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("BACKLINK_END")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("ACTUAL_STATE")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENT_GREEN")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENTP_GREEN")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("BACKLINK_GREEN")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENT_RED")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("EVENTP_RED")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("BACKLINK_RED")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("GREEN_EQ_RED")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("WAIT")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("STARTTIMESTAMP")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("ENDTIMESTAMP")).thenReturn("000", "000", "000", "000", "000");
		when(outRow.getValue("JOB_COUNT")).thenReturn("17300800", "17302100", "17302300", "17302500", "17302800");

		OutTableParam outTable = mock(OutTableParam.class);
		when(outTable.currentRow()).thenReturn(outRow, outRow, outRow, outRow, outRow);
		when(outTable.getRowCount()).thenReturn(2, 3);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutTableParameter("E_T_PROCESSLIST")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockGetChainStatusWithException() throws ParseException {
		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutParameterValue("E_STATUS")).thenThrow(new FunctionNetworkErrorException("ERROR AL OBTENER EL ESTADO DE LA CADENA")).thenReturn("G");
		when(result.getOutParameterValue("E_MANUAL_ABORT")).thenReturn("").thenReturn("");
		when(result.getOutParameterValue("E_MESSAGE")).thenReturn("").thenReturn("");

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenThrow(new FunctionNetworkErrorException("ERROR AL EJECUTAR LA FUNCION")).thenReturn(result);

		return function;
	}

}
