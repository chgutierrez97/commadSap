package ast.sap.connector.job.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;

public class JobHeadReaderTest {

	@Test
	public void testReadJob() {
		String eventId = "TEST_BASIS";

		/* INICIO DE MOCK -------------------------------------------------------------------- */

		SapStruct bapiRetMock = mock(SapStruct.class);
		when(bapiRetMock.getValue("TYPE")).thenReturn("");

		SapStruct jobHeadMock = mock(SapStruct.class);
		when(jobHeadMock.getValue("EVENTID")).thenReturn(eventId);

		SapFunctionResult functionResultMock = mock(SapFunctionResult.class);
		when(functionResultMock.getStructure("RETURN")).thenReturn(bapiRetMock);
		when(functionResultMock.getStructure("JOBHEAD")).thenReturn(jobHeadMock);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(functionResultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_READ")).thenReturn(functionMock);

		/* FIN DE MOCK -------------------------------------------------------------------- */

		JobHeadReader jobHeadReader = new JobHeadReader(repoMock);
		JobRunData jobData = JobData.newJobRunData("SOME_JOB", "mzaragoz", "123456");
		JobHead jobHead = jobHeadReader.readJob(jobData);
		Bp20job bp20job = jobHead.getBp20job();
		SapBapiret2 ret = jobHead.getRet();

		assertFalse(ret.hasError());
		assertEquals(eventId, bp20job.getEventId());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_READ");
		verify(functionMock, times(1)).execute();
		verify(functionMock, times(1)).setInParameter(eq("JOBNAME"), any());
		verify(functionMock, times(1)).setInParameter(eq("JOBCOUNT"), any());
		verify(functionMock, times(1)).setInParameter(eq("EXTERNAL_USER_NAME"), any());
	}

}
