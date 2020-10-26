package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.read.JobHead;
import ast.sap.connector.job.read.JobHeadReader;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Commando de lectura de Job
 * 
 * @see http://www.sapdatasheet.org/abap/func/bapi_xbp_job_read.html
 * 
 * @author franco.milanese
 *
 */
public class ReadJobCommand extends SapXmiCommand {
	private JobRunData jobData;

	public ReadJobCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	/*
	 * LA FUNCION BAPI_XBP_JOB_READ, OBTIENE UNA ESTRUCTURA DE TIPO BP20JOB EN
	 * EL PARAMETRO JOBHEAD. ESTA ESTRUCTURA TIENE UN CAMPO LLAMADO EVENTID EL
	 * CUAL TIENE EL NOMBRE DEL EVENTO NECESARIO PARA DISPARAR EL JOB (EN CASO
	 * QUE EL MISMO ESTE PROGRAMADO PARA INICIAR ANTE LA OCURRENCIA DE UN
	 * EVENTO).
	 */
	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();
		JobHeadReader reader = new JobHeadReader(repository);
		JobHead jobRead = reader.readJob(jobData);
		return new SapCommandResult(jobRead);
	}

}
