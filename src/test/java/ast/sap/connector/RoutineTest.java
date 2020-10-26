package ast.sap.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.conn.jco.JCoException;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.logs.ChainLog;
import ast.sap.connector.chain.logs.ChainLogReader;
import ast.sap.connector.chain.logs.ChainLogReaderInput;
import ast.sap.connector.chain.processes.ChainProcessBundle;
import ast.sap.connector.chain.processes.ChainProcessesReader;
import ast.sap.connector.chain.processes.ProcessEntry;
import ast.sap.connector.chain.start.ChainStarter;
import ast.sap.connector.chain.status.ChainStatus;
import ast.sap.connector.chain.status.ChainStatusReader;
import ast.sap.connector.chain.status.ChainStatusReaderInput;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.impl.MonitorChainCommand;
import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapDestination;
import ast.sap.connector.dst.SapDestinationFactory;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobCreateData;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.JobTrackData;
import ast.sap.connector.job.create.JobCreator;
import ast.sap.connector.job.create.NewJobData;
import ast.sap.connector.job.create.StepVariantPair;
import ast.sap.connector.job.log.JobLog;
import ast.sap.connector.job.log.JoblogReadData;
import ast.sap.connector.job.log.JoblogReader;
import ast.sap.connector.job.log.LogEntry;
import ast.sap.connector.job.run.AsapJobRunner;
import ast.sap.connector.job.track.JobMonitor;
import ast.sap.connector.job.track.JobStatus;
import ast.sap.connector.job.track.JobTracker;
import ast.sap.connector.job.variant.ChangeVariantData;
import ast.sap.connector.job.variant.Variant;
import ast.sap.connector.job.variant.VariantChanger;
import ast.sap.connector.job.variant.VariantData;
import ast.sap.connector.job.variant.VariantKeyValuePair;
import ast.sap.connector.job.variant.VariantReader;
import ast.sap.connector.job.variant.Varinfo;
import ast.sap.connector.xmi.XmiLoginData;
import ast.sap.connector.xmi.XmiSession;

/**
 * Tests de rutina a realizar para verificar el impacto de los cambios. Estos tests dependen del servidor sap sobre el cual se haran pruebas.
 * 
 * @author martin.zaragoza
 *
 */
@Ignore
public class RoutineTest {
	private static final String CHAIN = "Z_CHAIN_S_EVENT";
	private static final String JOB_NAME = "TEST_JOB_TRACKER";
	private static String username;
	private static SapRepository repository;
	private static XmiSession xmiSession;

	@BeforeClass
	public static void before() throws JCoException, RepositoryGetFailException, FileNotFoundException, IOException {
		SapDestination destination = SapDestinationFactory.INSTANCE.getDestination("testDestination");
		destination.getAttributes();
		repository = destination.openContext();
		XmiLoginData loginData = new XmiLoginData();
		xmiSession = new XmiSession(repository, loginData);

		Configuration.loadConnectorConfig("connectorsap.test.properties");
		username = Configuration.getUsername();
	}

	@Test
	public void testRunJob() {
		JobCreator jobDummy = new JobCreator(repository);
		String jobName = "TEST_JOB_RUNNER";
		String externalUsername = username;
		JobCreateData jobCreateData = JobData.newJobCreateData(jobName, externalUsername);
		NewJobData newJob = jobDummy.createJob(jobCreateData, Collections.singletonList(new StepVariantPair("SHOWCOLO")));
		String jobId = newJob.getJobCount();
		JobRunData jobRunData = JobData.newJobRunData(jobName, externalUsername, jobId);
		AsapJobRunner runner = new AsapJobRunner(repository);
		SapBapiret2 bapiRet2 = runner.runJob(jobRunData);
		Assert.assertFalse(bapiRet2.hasError());
	}

	@Test
	public void testRunNonExistingJobId() {
		JobRunData jobRunData = JobData.newJobRunData("TEST_JOB_RUNNER", "mazaragoz", "1111111");
		AsapJobRunner runner = new AsapJobRunner(repository);
		SapBapiret2 bapiRet2 = runner.runJob(jobRunData);
		System.out.println("testRunNonExistingJobId - Message: " + bapiRet2.getMessage());
		assertTrue(bapiRet2.hasError());
	}

	@Test
	public void testGetStatus() throws InterruptedException {
		JobCreator jobCreator = new JobCreator(repository);
		JobCreateData jobData = JobData.newJobCreateData(JOB_NAME, username);
		NewJobData dummyJob = jobCreator.createJob(jobData, Collections.singletonList(new StepVariantPair("SHOWCOLO")));

		String jobCount = dummyJob.getJobCount();
		JobRunData jobRunData = JobData.newJobRunData(JOB_NAME, username, jobCount);

		AsapJobRunner runner = new AsapJobRunner(repository);
		SapBapiret2 runRet = runner.runJob(jobRunData);
		assertFalse(runRet.hasError());

		JobTrackData jobTrackData = JobData.newJobTrackData(JOB_NAME, username, jobCount);
		JobTracker jobTracker = new JobTracker(repository);
		System.out.println("Status before sleep: " + jobTracker.getStatus(jobTrackData));
		Thread.sleep(1000);

		JobStatus jobStatus = jobTracker.getStatus(jobTrackData);
		System.out.println("Status after sleep: " + jobTracker.getStatus(jobTrackData));
		assertTrue(jobStatus.getStatusCode().hasFinished());
	}

	@Test
	public void testJobMonitor() throws InterruptedException {
		JobCreator jobDummy = new JobCreator(repository);
		String jobName = "TEST_JOB_MONITOR";
		String externalUsername = username;
		JobCreateData jobCreateData = JobData.newJobCreateData(jobName, externalUsername);
		NewJobData newJob = jobDummy.createJob(jobCreateData, Collections.singletonList(new StepVariantPair("ZTEST_WAIT_5_SECONDS")));

		String jobId = newJob.getJobCount();
		JobRunData jobRunData = JobData.newJobRunData(jobName, externalUsername, jobId);
		AsapJobRunner runner = new AsapJobRunner(repository);
		SapBapiret2 bapiRet2 = runner.runJob(jobRunData);
		assertFalse(bapiRet2.hasError());

		Thread.sleep(1000);
		JobTrackData jobTrackData = JobData.newJobTrackData(jobName, username, jobId);
		JobTracker jobTracker = new JobTracker(repository);
		assertTrue(jobTracker.getStatus(jobTrackData).getStatusCode().isRunning());

		Thread.sleep(5000);
		assertTrue(jobTracker.getStatus(jobTrackData).getStatusCode().hasFinished());
	}

	@Test
	public void testReadLog() throws InterruptedException {
		JobCreator jobCreator = new JobCreator(repository);
		String jobName = "TEST_READ_LOG";
		JobCreateData jobData = JobData.newJobCreateData(jobName, username);
		NewJobData dummyJob = jobCreator.createJob(jobData, Collections.singletonList(new StepVariantPair("SHOWCOLO")));

		String jobCount = dummyJob.getJobCount();
		JobRunData jobRunData = JobData.newJobRunData(jobName, username, jobCount);

		AsapJobRunner runner = new AsapJobRunner(repository);
		SapBapiret2 runRet = runner.runJob(jobRunData);
		assertFalse(runRet.hasError());

		Thread.sleep(1000);
		JoblogReader joblogReader = new JoblogReader(repository);
		JoblogReadData jobLogReadData = new JoblogReadData(jobName, jobCount, username);

		JobLog jobLog = joblogReader.readLog(jobLogReadData);
		assertFalse(jobLog.getReturnStruct().hasError());
		List<LogEntry> logEntries = jobLog.getLogEntries();
		System.out.println("Log entries: " + logEntries);
		assertTrue(logEntries.size() == 3);
	}

	@Test
	public void testChangeAllVariantFields() throws ParseException {
		System.out.println("testChangeVariant----------------------------------------------------------------------------------------");
		String externalUsername = username;

		Collection<VariantKeyValuePair> variantValuePairs = new ArrayList<>();
		String date = "05/12/1964";
		String number = "1000000";
		String text = "CASI PARECES NORMAL";
		variantValuePairs.add(new VariantKeyValuePair("P_FECHA", date));
		variantValuePairs.add(new VariantKeyValuePair("P_NUMER", number));
		variantValuePairs.add(new VariantKeyValuePair("P_TEXTO", text));

		String program = "ZTEST_3_CAMPOS";
		String variantName = "PRUEBA_VARIANT";

		VariantChanger variantChanger = new VariantChanger(repository);
		ChangeVariantData changeVariantData = VariantData.newChangeVariantData(program, variantName, externalUsername, variantValuePairs);
		SapBapiret2 changeVariantRet = variantChanger.changeVariant(changeVariantData);
		assertFalse(changeVariantRet.hasError());

		VariantReader variantReader = new VariantReader(repository);
		Varinfo varinfo = variantReader.readVariant(changeVariantData);
		assertFalse(varinfo.getRet().hasError());

		assertTrue(varinfo.getVariant().isPresent());
		Variant variant = varinfo.getVariant().get();
		assertEquals(Configuration.getSapOutDateFormat().format(Configuration.getArgsInDateFormat().parse(date)), variant.getVariantEntries().get(0).getPlow());
		assertEquals(number, variant.getVariantEntries().get(1).getPlow());
		assertEquals(text, variant.getVariantEntries().get(2).getPlow());

		System.out.println("varinfo: " + varinfo);
	}

	@Test
	public void testChaninMonitorJobs() {
		System.out.println("ChaninMonitorJobs--------------------------------------------------------------------------------");

		ChainStarter chainStarter = new ChainStarter(repository);
		ChainData chainData = chainStarter.startChain(CHAIN);
		ChainStatusReader chainStatusReader = new ChainStatusReader(repository);
		ChainStatus chainStatus = null;
		do {
			chainStatus = chainStatusReader.readChainStatus(new ChainStatusReaderInput(chainData.getChain(), chainData.getLogId()));
		} while (chainStatus.getStatus().notFinished());

		ChainProcessesReader processesReader = new ChainProcessesReader(repository);
		ChainProcessBundle chainProcessBundle = processesReader.readProcesses(chainData);
		JobMonitor monitor = new JobMonitor(repository);
		List<ProcessEntry> chainProcesses = chainProcessBundle.getProcesses();

		for (ProcessEntry entry : chainProcesses) {
			System.out.println("BI_PROCESS_" + entry.getType() + "---------------------------------------------------");
			JobTrackData jobData = JobData.newJobTrackData("BI_PROCESS_" + entry.getType(), username, entry.getJobCount());
			monitor.monitorJob(jobData, true);
		}
	}

	@Test
	public void testChainMonitorJobs() {
		System.out.println("ChainMonitorJobs--------------------------------------------------------------------------------");
		MonitorChainCommand monitorChainCommand = new MonitorChainCommand(repository, new XmiLoginData(), CHAIN, username);
		SapCommandResult chainExecution = monitorChainCommand.execute();
		assertTrue(chainExecution.getChainStatus().get().hasFinishedSuccessfully());
		assertTrue(chainExecution.getProcessLogPairs().isPresent());
	}

	@Test
	public void testReadChainLog() {
		System.out.println("ChainReadLog--------------------------------------------------------------------------------");

		ChainStarter chainStarter = new ChainStarter(repository);
		/* Manda a ejecutar la cadena indicada */
		ChainData chainData = chainStarter.startChain(CHAIN);

		ChainLogReader chainLogReader = new ChainLogReader(repository);
		ChainLogReaderInput chainLogReaderInput = new ChainLogReaderInput(chainData);
		/* Obtiene el log de la ejecucion */
		ChainLog chainLog = chainLogReader.readChainLog(chainLogReaderInput);
		
		System.out.println(chainLog.toString());
		
		Assert.assertNotNull(chainLog);

	}

	
	@Test
	public void testGetFunction() throws RepositoryGetFailException, JCoException {
		String DESTINATION_NAME = "testDestination";
		SapDestination sapDestination = null;
		try {
			sapDestination = SapDestinationFactory.INSTANCE.getDestination(DESTINATION_NAME);
			sapDestination.getAttributes();

			SapRepository repository = sapDestination.openContext();
			String functionName = "BAPI_XBP_JOB_START_ASAP";
			repository.getFunction(functionName);
		} finally {
			sapDestination.closeContext();
		}
	}
	
	@Test
	public void testStatelessRepository() throws RepositoryGetFailException {
		final String DESTINATION_NAME = "testDestination";
		SapDestination sapDestination = SapDestinationFactory.INSTANCE.getDestination(DESTINATION_NAME);
		sapDestination.statelessRepository();
	}
	
	

	@AfterClass
	public static void after() {
		xmiSession.logout();
	}
}
