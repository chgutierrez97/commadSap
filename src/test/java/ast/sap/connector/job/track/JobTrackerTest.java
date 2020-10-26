package ast.sap.connector.job.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.log.LogEntry;

public class JobTrackerTest {
	 @Test
	public void testGetStatus() throws InterruptedException {
		String status = "F";

		/* INICIO DE MOCK -------------------------------------------------------------------- */

		SapStruct retMock = mock(SapStruct.class);
		when(retMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getStructure("RETURN")).thenReturn(retMock);
		when(resultMock.getOutParameterValue("STATUS")).thenReturn(status);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(resultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(functionMock);

		/* FIN DE MOCK -------------------------------------------------------------------- */

		JobTracker jobTracker = new JobTracker(repoMock);
		JobRunData jobData = JobData.newJobRunData("SOME_JOB", "mzaragoz", "123456");
		JobStatus jobStatus = jobTracker.getStatus(jobData);

		assertEquals(JobStatusCode.F, jobStatus.getStatusCode());
		assertFalse(jobStatus.getReturnStruct().hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_STATUS_GET");
		verify(functionMock, times(1)).execute();
		verify(functionMock, times(1)).setInParameter("JOBNAME", jobData.getJobName());
		verify(functionMock, times(1)).setInParameter("JOBCOUNT", jobData.getJobId());
		verify(functionMock, times(1)).setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());
		verify(resultMock, times(1)).getOutParameterValue("STATUS");
		verify(resultMock, times(1)).getStructure("RETURN");
	}
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	
	private String tt(int i) {
		return "Texto de prueba " + i;
	}

	
	private SapFunction mockGetJobLogFunction() throws ParseException {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");

		/*
		 * text = entry.getValue("TEXT").toString(); Date date = (Date) entry.getValue("ENTERDATE"); Date time = (Date) entry.getValue("ENTERTIME");
		 */
		OutTableRow outRow = mock(OutTableRow.class);
		when(outRow.getValue("TEXT")).thenReturn(tt(1),tt(2),tt(3),tt(4),tt(5),tt(6),tt(7),tt(8),tt(9),tt(10),tt(11),tt(12),tt(13),tt(14));
		when(outRow.getValue("ENTERDATE")).thenReturn(dateFormat.parse("21/03/1991"), dateFormat.parse("05/12/1964"), dateFormat.parse("18/09/1960"));
		when(outRow.getValue("ENTERTIME")).thenReturn(timeFormat.parse("12:37:48"), timeFormat.parse("12:38:40"), timeFormat.parse("12:40:11"));

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
	public void testGetFullStatus() throws InterruptedException, ParseException {
		String status = "F";

		/* INICIO DE MOCK -------------------------------------------------------------------- */

		SapStruct retMock = mock(SapStruct.class);
		when(retMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getStructure("RETURN")).thenReturn(retMock);
		when(resultMock.getOutParameterValue("STATUS")).thenReturn(status);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(resultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_STATUS_GET")).thenReturn(functionMock);
		SapFunction getJobLogFunction = mockGetJobLogFunction();
		when(repoMock.getFunction("BAPI_XBP_JOB_JOBLOG_READ")).thenReturn(getJobLogFunction);

		/* FIN DE MOCK -------------------------------------------------------------------- */

		JobTracker jobTracker = new JobTracker(repoMock);
		JobRunData jobData = JobData.newJobRunData("SOME_JOB", "mzaragoz", "123456");
		JobFullStatus jobFullStatus = jobTracker.getFullStatus(jobData);

		assertEquals(JobStatusCode.F, jobFullStatus.getJobStatus().getStatusCode());
		assertFalse(jobFullStatus.getJobStatus().getReturnStruct().hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_STATUS_GET");
		verify(functionMock, times(1)).execute();
		verify(functionMock, times(1)).setInParameter("JOBNAME", jobData.getJobName());
		verify(functionMock, times(1)).setInParameter("JOBCOUNT", jobData.getJobId());
		verify(functionMock, times(1)).setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());
		verify(resultMock, times(1)).getOutParameterValue("STATUS");
		verify(resultMock, times(1)).getStructure("RETURN");
		
		List<LogEntry> logEntries = jobFullStatus.getJobLog().getLogEntries();
		assertEquals(3,logEntries.size());
		assertFalse(jobFullStatus.getJobLog().getReturnStruct().hasError());
	}
}
