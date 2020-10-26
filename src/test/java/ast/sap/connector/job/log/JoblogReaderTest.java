package ast.sap.connector.job.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;

public class JoblogReaderTest {
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
		when(outRow.getValue("MSGTYPE")).thenReturn("S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S");

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

	@Test
	public void testReadLog() throws ParseException {
		SapFunction getJobLogFunction = mockGetJobLogFunction();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		JoblogReader joblogReader = new JoblogReader(sapRepository);
		String jobName = "MY_JOB";
		String jobId = "123456";
		String externalUsername = "mzaragoz";
		JoblogReadData jobData = new JoblogReadData(jobName, jobId, externalUsername);
		JobLog jobLog = joblogReader.readLog(jobData);

		List<LogEntry> logEntries = jobLog.getLogEntries();
		assertEquals(3, logEntries.size());
		assertFalse(jobLog.getReturnStruct().hasError());
	}
}
