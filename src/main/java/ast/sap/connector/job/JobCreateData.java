package ast.sap.connector.job;

public interface JobCreateData {

	/**
	 * Nombre del job. "Background job name".
	 * 
	 * @see https://www.sapdatasheet.org/abap/tabl/bapixmjob-jobname.html
	 * 
	 * @return Nombre del job.
	 */
	String getJobName();

	/**
	 * Parametro EXTUSER. "XMI logging: Name of user in external management tool"
	 * 
	 * @see https://www.sapdatasheet.org/abap/tabl/bapixmlogr-extuser.html
	 * 
	 * @return "Name of user in external management tool".
	 */
	String getExternalUsername();

}