package ast.sap.connector.job.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;
import ast.sap.connector.job.JobCreateData;
import ast.sap.connector.job.JobData;

public class JobCreatorTest {
	Object jobCount = "123456";
	String jobName = "MY_JOB";
	String username = "mzaragoz";
	
	String program = "MY_REPORT";
	String variant = "MY_VARIANT";
	String program2 = "MY_REPORT_2";
	String variant2 = "MY_VARIANT_2";
	String program3 = "MY_REPORT_3";
	String variant3 = "MY_VARIANT_3";

	@Test
	public void testCreateJobWithSingleStep() {
		SapRepository repository = mockRepository();

		/* FIN DE MOCKS ------------------------------------------------------------------------------- */

		JobCreator jobCreator = new JobCreator(repository);
		JobCreateData jobData = JobData.newJobCreateData(jobName, username);
		StepVariantPair singleStep = new StepVariantPair(program, variant);
		NewJobData newJobData = jobCreator.createJob(jobData, singleStep);

		assertFalse(newJobData.getRet().hasError());
		assertEquals(jobCount, newJobData.getJobCount());
	}
	
	@Test
	public void testCreateJobJobCreateDataListOfStepVariantPair() {
		SapRepository repository = mockRepository();

		/* FIN DE MOCKS ------------------------------------------------------------------------------- */

		JobCreator jobCreator = new JobCreator(repository);
		JobCreateData jobData = JobData.newJobCreateData(jobName, username);
		List<StepVariantPair> steps = Arrays.asList(new StepVariantPair[] {
				new StepVariantPair(program, variant),
				new StepVariantPair(program2, variant2),
				new StepVariantPair(program3, variant3),
		});
		NewJobData newJobData = jobCreator.createJob(jobData, steps);

		assertFalse(newJobData.getRet().hasError());
		assertEquals(jobCount, newJobData.getJobCount());
	}

	private SapRepository mockRepository() {
		SapFunction jobOpenFunction = mockJobOpenFunction();
		SapFunction addStepFunction = mockAddStepFunction();
		SapFunction jobCloseFunction = mockJobCloseFunction();

		SapRepository repository = mock(SapRepository.class);
		when(repository.getFunction("BAPI_XBP_JOB_OPEN")).thenReturn(jobOpenFunction);
		when(repository.getFunction("BAPI_XBP_JOB_ADD_ABAP_STEP")).thenReturn(addStepFunction);
		when(repository.getFunction("BAPI_XBP_JOB_CLOSE")).thenReturn(jobCloseFunction);
		return repository;
	}

	private SapFunction mockJobCloseFunction() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("MESSAGE")).thenReturn("");
		when(ret.getValue("TYPE")).thenReturn("");

		SapFunctionResult res = mock(SapFunctionResult.class);
		when(res.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter("JOBNAME", jobName)).thenReturn(function);
		when(function.setInParameter("JOBCOUNT", jobCount)).thenReturn(function);
		when(function.setInParameter("EXTERNAL_USER_NAME", username)).thenReturn(function);
		when(function.execute()).thenReturn(res);

		return function;
	}

	private SapFunction mockAddStepFunction() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("MESSAGE")).thenReturn("");
		when(ret.getValue("TYPE")).thenReturn("");

		SapFunctionResult res = mock(SapFunctionResult.class);
		when(res.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(res);

		return function;
	}

	private SapFunction mockJobOpenFunction() {
		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("MESSAGE")).thenReturn("");
		when(ret.getValue("TYPE")).thenReturn("");

		SapFunctionResult res = mock(SapFunctionResult.class);
		when(res.getStructure("RETURN")).thenReturn(ret);
		when(res.getOutParameterValue("JOBCOUNT")).thenReturn(jobCount);

		SapFunction jobOpenFunction = mock(SapFunction.class);
		when(jobOpenFunction.setInParameter("JOBNAME", jobName)).thenReturn(jobOpenFunction);
		when(jobOpenFunction.setInParameter("EXTERNAL_USER_NAME", "mzaragoz")).thenReturn(jobOpenFunction);
		when(jobOpenFunction.execute()).thenReturn(res);
		return jobOpenFunction;
	}
}
