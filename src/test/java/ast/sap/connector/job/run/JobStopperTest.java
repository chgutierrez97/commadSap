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

public class JobStopperTest {
	String jobName = "SOME_JOB";
	String externalUsername = "mzaragoz";
	String jobId = "123456";

	@Test
	public void testStopJob() {
		/* INICIO DE MOCK -------------------------------------------------------------------- */

		SapFunction runFunctionMock = mockRunFunction();
		SapFunction stopFunctionMock = mockStopFunction();

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_JOB_START_ASAP")).thenReturn(runFunctionMock);
		when(repoMock.getFunction("BAPI_XBP_JOB_ABORT")).thenReturn(stopFunctionMock);

		/* FIN DE MOCK -------------------------------------------------------------------- */

		AsapJobRunner jobRunner = new AsapJobRunner(repoMock);
		JobRunData jobData = JobData.newJobRunData(jobName, externalUsername, jobId);
		SapBapiret2 runRet = jobRunner.runJob(jobData);
		assertFalse(runRet.hasError());
		
		JobStopper jobStopper = new JobStopper(repoMock);
		SapBapiret2 ret = jobStopper.stopJob(jobData);
		assertFalse(ret.hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_START_ASAP");
		verify(repoMock, times(1)).getFunction("BAPI_XBP_JOB_ABORT");
		
		verify(runFunctionMock, times(1)).execute();
		verify(runFunctionMock, times(1)).setInParameter(eq("JOBNAME"), any());
		verify(runFunctionMock, times(1)).setInParameter(eq("JOBCOUNT"), any());
		verify(runFunctionMock, times(1)).setInParameter(eq("EXTERNAL_USER_NAME"), any());
		verify(runFunctionMock, times(0)).setInParameter(eq("TARGET_SERVER"), any());
		
		verify(stopFunctionMock, times(1)).execute();
		verify(stopFunctionMock, times(1)).setInParameter(eq("JOBNAME"), any());
		verify(stopFunctionMock, times(1)).setInParameter(eq("JOBCOUNT"), any());
		verify(stopFunctionMock, times(1)).setInParameter(eq("EXTERNAL_USER_NAME"), any());
	}

	private SapFunction mockStopFunction() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");
		when(ret.getValue("MESSAGE")).thenReturn("");

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter("JOBNAME", jobName)).thenReturn(function);
		when(function.setInParameter("JOBCOUNT", jobId)).thenReturn(function);
		when(function.setInParameter("EXTERNAL_USER_NAME", externalUsername)).thenReturn(function);
		when(function.execute()).thenReturn(result);
		
		return function;
	}

	public SapFunction mockRunFunction() {
		SapStruct retMock = mock(SapStruct.class);
		when(retMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getStructure("RETURN")).thenReturn(retMock);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(resultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		return functionMock;
	}

}
