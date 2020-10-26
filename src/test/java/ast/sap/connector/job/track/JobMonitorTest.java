package ast.sap.connector.job.track;

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

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.dst.exception.NonexistentFunctionException;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.job.JobData;

public class JobMonitorTest {
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private SapFunction mockGetStatusFunction() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");

		SapFunctionResult result = mock(SapFunctionResult.class);
		// when(result.getOutParameterValue("STATUS")).thenReturn("S", "R", "R", "F");
		when(result.getOutParameterValue("STATUS")).thenReturn("S").thenReturn("R").thenReturn("R").thenReturn("F");
		when(result.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
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
		when(outTable.getRowCount()).thenReturn(1, 2, 3);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);
		when(result.getOutTableParameter("JOB_PROTOCOL_NEW")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	@Test
	public void testMonitorJob() throws FunctionGetFailException, NonexistentFunctionException, ParseException {
		SapFunction getStatusFunction = mockGetStatusFunction();
		SapFunction getJobLogFunction = mockGetJobLogFunction();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(getStatusFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		/* FIN DE MOCKS ------------------------------------------------------------------------------------------------------------------------ */

		JobMonitor jobMonitor = new JobMonitor(sapRepository);
		jobMonitor.doSleep = false;
		String jobName = "MY_JOB";
		String externalUsername = "mzaragoz";
		String jobId = "123456";
		boolean printContinuously = true;
		JobFullStatus jobFullStatus = jobMonitor.monitorJob(JobData.newJobTrackData(jobName, externalUsername, jobId), printContinuously);

		assertTrue(jobFullStatus.getJobStatus().getStatusCode().hasFinished());
		assertEquals(3, jobFullStatus.getJobLog().getLogEntries().size());

	}

	private SapFunction mockGetStatusFunctionWithException() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutParameterValue("STATUS"))
				.thenReturn("S")
				.thenThrow(new FunctionNetworkErrorException("Error al obtener la funcion"))
				.thenReturn("R")
				.thenThrow(new FunctionNetworkErrorException("Error al obtener la funcion"))
				.thenReturn("R")
				.thenReturn("F");
		when(result.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	private SapFunction mockGetJobLogFunctionWithException() throws ParseException {
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
		when(outTable.getRowCount()).thenReturn(1, 2, 3);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);
		when(result.getOutTableParameter("JOB_PROTOCOL_NEW")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute())
				.thenReturn(result)
				.thenThrow(new FunctionNetworkErrorException("Error al obtener la funcion"))
				.thenReturn(result);

		return function;
	}

	@Test
	public void testMonitorJobWithReconnect() throws FunctionGetFailException, NonexistentFunctionException, ParseException {
		SapFunction getStatusFunction = mockGetStatusFunctionWithException();
		SapFunction getJobLogFunction = mockGetJobLogFunctionWithException();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(getStatusFunction);
		when(sapRepository.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		/* FIN DE MOCKS ------------------------------------------------------------------------------------------------------------------------ */

		JobMonitor jobMonitor = new JobMonitor(sapRepository);
		jobMonitor.doSleep = false;
		jobMonitor.doReconnect = false;

		JobMonitor spy = spy(jobMonitor);

		String jobName = "MY_JOB";
		String externalUsername = "mzaragoz";
		String jobId = "123456";
		boolean printContinuously = true;
		JobFullStatus jobFullStatus = spy.monitorJob(JobData.newJobTrackData(jobName, externalUsername, jobId), printContinuously);

		assertTrue(jobFullStatus.getJobStatus().getStatusCode().hasFinished());
		assertEquals(3, jobFullStatus.getJobLog().getLogEntries().size());

		verify(spy, times(3)).reconnect();
	}
}
