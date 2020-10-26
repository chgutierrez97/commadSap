package ast.sap.connector.job.run;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;

public class AsapJobRunnerTest {

	@Test
	public void testRunExistingJob() {
		/* INICIO DE MOCK -------------------------------------------------------------------- */
		
		SapStruct retMock = mock(SapStruct.class);
		when(retMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getStructure("RETURN")).thenReturn(retMock);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(resultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_START_ASAP")).thenReturn(functionMock);
		
		/* FIN DE MOCK -------------------------------------------------------------------- */

		AsapJobRunner jobRunner = new AsapJobRunner(repoMock);
		JobRunData jobData = JobData.newJobRunData("SOME_JOB", "mzaragoz", "123456");
		SapBapiret2 runRet = jobRunner.runJob(jobData);
		assertFalse(runRet.hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_START_ASAP");
		verify(functionMock, times(1)).execute();
		verify(functionMock, times(1)).setInParameter(eq("JOBNAME"), any());
		verify(functionMock, times(1)).setInParameter(eq("JOBCOUNT"), any());
		verify(functionMock, times(1)).setInParameter(eq("EXTERNAL_USER_NAME"), any());
		verify(functionMock, times(0)).setInParameter(eq("TARGET_SERVER"), any());
	}

	@Test
	public void testRunNonexistentJob() {
		SapStruct mockBapiRet = mock(SapStruct.class);
		when(mockBapiRet.getValue("TYPE")).thenReturn("E");

		SapFunctionResult mockFunctionResult = mock(SapFunctionResult.class);
		when(mockFunctionResult.getStructure("RETURN")).thenReturn(mockBapiRet);

		SapFunction mockFunction = mock(SapFunction.class);
		when(mockFunction.execute()).thenReturn(mockFunctionResult);
		when(mockFunction.setInParameter(anyString(), any())).thenReturn(mockFunction);

		SapRepository mockRepo = mock(SapRepository.class);
		when(mockRepo.getFunction("BAPI_XBP_JOB_START_ASAP")).thenReturn(mockFunction);

		AsapJobRunner jobRunner = new AsapJobRunner(mockRepo);
		JobRunData jobData = JobData.newJobRunData("SOME_JOB", "mzaragoz", "123456");
		SapBapiret2 runRet = jobRunner.runJob(jobData);
		assertTrue(runRet.hasError());
	}

}
