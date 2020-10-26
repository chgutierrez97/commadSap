package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.evt.EventRaiser;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Comando de prueba BAPI_XBP_EVENT_RAISE.
 * 
 * @see http://www.sapdatasheet.org/abap/func/bapi_xbp_event_raise.html
 * 
 * @author martin.zaragoza
 *
 */
public class RaiseEventCommand extends SapXmiCommand {

	private JobRunData jobData;
	private String eventId;

	public RaiseEventCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData, String eventId) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.eventId = eventId;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository sapRepository = repository();
		EventRaiser reader = new EventRaiser(sapRepository);
		SapBapiret2 raiseEvent = reader.raiseEvent(jobData.getExternalUsername(), eventId);
		return new SapCommandResult(raiseEvent);
	}
}
