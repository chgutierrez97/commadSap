package ast.sap.connector.main.args;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;

import ast.sap.connector.cmd.AvailableCommand;
import ast.sap.connector.job.JobData;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.variant.VariantKeyValuePair;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Datos de ejecucion del componente.
 * 
 * @author martin.zaragoza
 *
 */
public final class InputArgumentsData {
	/* PARAMETROS DE INICIO DE SESION CON SAP */
	private String user;
	private String password;
	private String host;
	private String clientNumber;
	private String systemNumber;

	/* PARAMETROS DE EJECUCION DE JOBS */
	private String jobName = "";
	private String jobId = "";
	private AvailableCommand command = AvailableCommand.HELP;

	private String language = "EN";

	private String execServer;

	private String eventId;	
		

	private String singleStep;
	private String singleVariant;
	private List<VariantKeyValuePair> variantKeyValuePairs = new ArrayList<>();

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getHost() {
		return host;
	}

	public String getJobName() {
		return jobName;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public String getSystemNumber() {
		return systemNumber;
	}

	public InputArgumentsData setUser(String user) {
		this.user = user;
		return this;
	}

	public InputArgumentsData setPassword(String password) {
		this.password = password;
		return this;
	}

	public InputArgumentsData setHost(String host) {
		this.host = host;
		return this;
	}

	public InputArgumentsData setJobName(String jobName) {
		this.jobName = jobName;
		return this;
	}

	public InputArgumentsData setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
		return this;
	}

	public InputArgumentsData setSystemNumber(String systemNumber) {
		this.systemNumber = systemNumber;
		return this;
	}

	public AvailableCommand getCommand() {
		return command;
	}

	public InputArgumentsData setCommand(AvailableCommand command) {
		this.command = command;
		return this;
	}

	public String getJobId() {
		return jobId;
	}

	public String getChain() {
		/* EL NOMBRE DE JOB Y NOMBRE/ID DE LA CADENA SE GUARDAN EN UN MISMO PARAMETRO */
		return jobName;
	}
	
	public String getChainLogId() {
		/* EL ID DE JOB Y ID DEL LOG DE UNA CADENA SE GUARDAN EN UN MISMO PARAMETRO */
		return jobId;
	}

	public InputArgumentsData setJobId(String jobId) {
		this.jobId = jobId;
		return this;
	}
	
	



	public String getLanguage() {
		return language;
	}

	public InputArgumentsData setLanguage(String language) {
		this.language = language;
		return this;
	}

	public String getExecServer() {
		return execServer;
	}

	public InputArgumentsData setExecServer(String execServer) {
		this.execServer = execServer;
		return this;
	}

	public String getEventId() {
		return eventId;
	}

	public InputArgumentsData setEventId(String eventId) {
		this.eventId = eventId;
		return this;
	}

	public String getSingleStep() {
		return singleStep;
	}

	public InputArgumentsData setSingleStep(String singleStep) {
		this.singleStep = singleStep;
		return this;
	}

	public String getSingleVariant() {
		return singleVariant;
	}

	public InputArgumentsData setSingleVariant(String singleVariant) {
		this.singleVariant = singleVariant;
		return this;
	}

	public List<VariantKeyValuePair> getVariantKeyValuePairs() {
		return variantKeyValuePairs;
	}

	public InputArgumentsData setVariantKeyValuePairs(List<VariantKeyValuePair> variantKeyValuePairs) {
		this.variantKeyValuePairs = Collections.unmodifiableList(variantKeyValuePairs);
		return this;
	}

	@Override
	public String toString() {
		return "InputArgumentsData [user=" + user + ", password=" + "****" + ", host=" + host + ", clientNumber="
				+ clientNumber + ", systemNumber=" + systemNumber + ", jobName=" + jobName + ", jobId=" + jobId + ", command=" + command + ", language="
				+ language + ", execServer=" + execServer + ", eventId=" + eventId + ", singleStep=" + singleStep + ", singleVariant=" + singleVariant
				+ ", variantKeyValuePairs=" + variantKeyValuePairs + "]";
	}

	/*
	 * TODO : VERIFICAR SI EL PARAMETRO EXECSERVER ES IGUAL AL HOST O SI DEBE INGRESARSE UN NOMBRE DE SERVER PROPIO DE SAP
	 */
	public JobRunData newJobRunData() {
		Optional<String> exSrv = Optional.fromNullable(execServer);
		return exSrv.isPresent() ? JobData.newJobRunData(jobName, user, jobId, exSrv.get()) : JobData.newJobTrackData(jobName, user, jobId);
	}

	public XmiLoginData newXmiLoginData() {
		return new XmiLoginData("AST", "SAP_CONNECTOR");
	}

	/**
	 * Verifica si el comando a ejecutar es de tipo HELP.
	 * 
	 * @return True si el comando solicitado a invocar es HELP.
	 */
	public boolean isHelp() {
		return AvailableCommand.HELP.equals(command);
	}

	/**
	 * Verifica si el comando a ejecutar es de tipo ENCRYPT_PASSWORD.
	 * 
	 * @return True si el comando a ejecutar es de tipo ENCRYPT_PASSWORD, false en caso contrario.
	 */
	public boolean isEncryptPassword() {
		return AvailableCommand.ENCRYPT_PASSWORD.equals(command);
	}
}
