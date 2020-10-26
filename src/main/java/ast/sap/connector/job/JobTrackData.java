package ast.sap.connector.job;

public interface JobTrackData extends JobCreateData {
	/**
	 * Parametro JOBCOUNT. "Job ID"
	 * 
	 * @see https://www.sapdatasheet.org/abap/tabl/bapixmjob-jobcount.html
	 * 
	 * @return Job ID.
	 */
	String getJobId();

}