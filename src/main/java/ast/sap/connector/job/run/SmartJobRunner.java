package ast.sap.connector.job.run;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.evt.EventRaiser;
import ast.sap.connector.job.read.Bp20job;
import ast.sap.connector.job.read.JobHeadReader;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Corre un job detectando si acaso el mismo debe dispararse manualmente o a partir de un evento.
 * 
 * @author martin
 *
 */
public class SmartJobRunner implements JobRunner {

	private SapRepository sapRepository;

	public SmartJobRunner(SapRepository sapRepository) {
		Preconditions.checkNotNull(sapRepository, "El repositorio de funciones no puede ser nulo");
		this.sapRepository = sapRepository;
	}

	@Override
	public SapBapiret2 runJob(JobRunData jobData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		JobHeadReader jobHeadReader = new JobHeadReader(sapRepository);
		Bp20job jobHead = jobHeadReader.readJob(jobData).getBp20job();
		String eventId = jobHead.getEventId();
		
		if (Strings.isNullOrEmpty(eventId)) {
			AsapJobRunner jobRunner = new AsapJobRunner(sapRepository);
			return jobRunner.runJob(jobData);
		} else {
			EventRaiser eventRaiser = new EventRaiser(sapRepository);
			return eventRaiser.raiseEvent(jobData.getExternalUsername(), eventId);
		}
	}
}
