package ast.sap.connector.cmd.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.dst.SapDestination;
import ast.sap.connector.dst.SapDestinationFactory;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * @deprecated Se debe reescribir antes de ejecutar...
 * 
 * @author martin.zaragoza
 *
 */
@Ignore
public class MonitorJobCommandTest {
	private static final String DESTINATION_NAME = "testDestination";

	@Test
	public void testConcurrentJobs() throws RepositoryGetFailException, InterruptedException {
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				monitorConcurrent1();
			}
		});
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				monitorConcurrent2();
			}
		});
		
		thread1.start();
		thread2.start();
		
		System.out.println("ESPERANDO A LOS THREADS...");
		
		thread1.join();
		thread2.join();
		
		System.out.println("FIN DE PRUEBA.");
	}

	private static void monitorConcurrent1() {
		SapDestination destination = SapDestinationFactory.INSTANCE.getDestination(DESTINATION_NAME);
		try {
			SapRepository repository = destination.openContext();

			XmiLoginData xmiLoginData = new XmiLoginData();
			String jobName = "CONCURRENT_1";
			String externalUsername = "mzaragoz";
			String jobId = "12190400";
			JobRunData jobData = JobData.newJobRunData(jobName, externalUsername, jobId);
			MonitorJobCommand command = new MonitorJobCommand(repository, xmiLoginData, jobData);

			SapCommandResult result = command.execute();

			assertTrue(result.getLogEntries().isPresent());

			fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				destination.closeContext();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void monitorConcurrent2() {
		SapDestination destination = SapDestinationFactory.INSTANCE.getDestination(DESTINATION_NAME);
		try {

			SapRepository repository = destination.openContext();

			XmiLoginData xmiLoginData = new XmiLoginData();
			String jobName = "CONCURRENT_2";
			String externalUsername = "mzaragoz";
			String jobId = "12192500";
			JobRunData jobData = JobData.newJobRunData(jobName, externalUsername, jobId);
			MonitorJobCommand command = new MonitorJobCommand(repository, xmiLoginData, jobData);

			SapCommandResult result = command.execute();

			assertTrue(result.getLogEntries().isPresent());

			fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				destination.closeContext();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
