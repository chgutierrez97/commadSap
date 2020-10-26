package ast.sap.connector.job;

public interface JobRunData extends JobTrackData {

	/**
	 * Parámetro correspondiente a EXECSERVER.
	 * 
	 * @see https://www.sapdatasheet.org/abap/tabl/bapixmjob-execserver.html
	 * 
	 * @return Parametro EXECSERVER.
	 */
	String getTargetServer();

}