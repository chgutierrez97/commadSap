package ast.sap.connector.job.run;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;

public class SmartJobRunnerTest {
	String jobName = "SOME_JOB";
	String externalUsername = "mzaragoz";
	String jobId = "123456";
	
	@Test
	public void testRunJobWithEvent() {
		String eventId = "TEST_BASIS";

		/* INICIO DE MOCK ---------------------------------------------------------------------------------- */

		SapStruct readBapiRetMock = mock(SapStruct.class);
		when(readBapiRetMock.getValue("TYPE")).thenReturn("");

		SapStruct eventRetMock = mock(SapStruct.class);
		when(eventRetMock.getValue("TYPE")).thenReturn("");

		SapStruct jobHeadMock = mock(SapStruct.class);
		when(jobHeadMock.getValue("EVENTID")).thenReturn(eventId);

		SapFunctionResult readFunctionResultMock = mock(SapFunctionResult.class);
		when(readFunctionResultMock.getStructure("RETURN")).thenReturn(readBapiRetMock);
		when(readFunctionResultMock.getStructure("JOBHEAD")).thenReturn(jobHeadMock);

		SapFunctionResult eventResultMock = mock(SapFunctionResult.class);
		when(eventResultMock.getStructure("RETURN")).thenReturn(eventRetMock);

		SapFunction readFunctionMock = mock(SapFunction.class);
		when(readFunctionMock.execute()).thenReturn(readFunctionResultMock);
		when(readFunctionMock.setInParameter(anyString(), any())).thenReturn(readFunctionMock);

		SapFunction eventFunctionMock = mock(SapFunction.class);
		when(eventFunctionMock.execute()).thenReturn(eventResultMock);
		when(eventFunctionMock.setInParameter(anyString(), any())).thenReturn(eventFunctionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_READ")).thenReturn(readFunctionMock);
		when(repoMock.getFunction("BAPI_XBP_EVENT_RAISE")).thenReturn(eventFunctionMock);

		/* FIN DE MOCK ---------------------------------------------------------------------------------- */

		SmartJobRunner smartJobRunner = new SmartJobRunner(repoMock);
		JobRunData jobData = JobData.newJobRunData(jobName, externalUsername, jobId);

		SapBapiret2 ret = smartJobRunner.runJob(jobData);
		assertFalse(ret.hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_READ");
		verify(readFunctionMock, times(1)).execute();
		verify(readFunctionMock, times(1)).setInParameter(eq("JOBNAME"), any());
		verify(readFunctionMock, times(1)).setInParameter(eq("JOBCOUNT"), any());
		verify(readFunctionMock, times(1)).setInParameter(eq("EXTERNAL_USER_NAME"), any());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_EVENT_RAISE");
		verify(eventFunctionMock, times(1)).execute();
		verify(eventFunctionMock, times(1)).setInParameter("EVENTID", eventId);
		verify(eventFunctionMock, times(1)).setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());
		verify(eventResultMock, times(1)).getStructure("RETURN");
	}
	
	@Test
	public void testRunJobAsap() {
		/* INICIO DE MOCK ---------------------------------------------------------------------------------- */

		SapStruct readBapiRetMock = mock(SapStruct.class);
		when(readBapiRetMock.getValue("TYPE")).thenReturn("");

		SapStruct runRetMock = mock(SapStruct.class);
		when(runRetMock.getValue("TYPE")).thenReturn("");
		when(runRetMock.getValue("MESSAGE")).thenReturn("");

		SapStruct jobHeadMock = mock(SapStruct.class);
		when(jobHeadMock.getValue("EVENTID")).thenReturn("");

		SapFunctionResult readFunctionResultMock = mock(SapFunctionResult.class);
		when(readFunctionResultMock.getStructure("RETURN")).thenReturn(readBapiRetMock);
		when(readFunctionResultMock.getStructure("JOBHEAD")).thenReturn(jobHeadMock);

		SapFunctionResult runResultMock = mock(SapFunctionResult.class);
		when(runResultMock.getStructure("RETURN")).thenReturn(runRetMock);

		SapFunction readFunctionMock = mock(SapFunction.class);
		when(readFunctionMock.execute()).thenReturn(readFunctionResultMock);
		when(readFunctionMock.setInParameter(anyString(), any())).thenReturn(readFunctionMock);

		SapFunction runFunctionMock = mock(SapFunction.class);
		when(runFunctionMock.execute()).thenReturn(runResultMock);
		when(runFunctionMock.setInParameter(anyString(), any())).thenReturn(runFunctionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_READ")).thenReturn(readFunctionMock);
		when(repoMock.getFunction("BAPI_XBP_JOB_START_ASAP")).thenReturn(runFunctionMock);

		/* FIN DE MOCK ---------------------------------------------------------------------------------- */

		SmartJobRunner smartJobRunner = new SmartJobRunner(repoMock);
		JobRunData jobData = JobData.newJobRunData(jobName, externalUsername, jobId);

		SapBapiret2 ret = smartJobRunner.runJob(jobData);
		assertFalse(ret.hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_READ");
		verify(readFunctionMock, times(1)).execute();
		verify(readFunctionMock, times(1)).setInParameter("JOBNAME", jobName);
		verify(readFunctionMock, times(1)).setInParameter("JOBCOUNT", jobId);
		verify(readFunctionMock, times(1)).setInParameter("EXTERNAL_USER_NAME", externalUsername);

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_START_ASAP");
		verify(runFunctionMock, times(1)).execute();
		verify(runFunctionMock, times(1)).setInParameter("JOBNAME", jobName);
		verify(runFunctionMock, times(1)).setInParameter("JOBCOUNT", jobId);
		verify(runFunctionMock, times(1)).setInParameter("EXTERNAL_USER_NAME", externalUsername);
	}

}
