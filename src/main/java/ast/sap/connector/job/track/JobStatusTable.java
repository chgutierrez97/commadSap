package ast.sap.connector.job.track;


import ast.sap.connector.func.SapBapiret2;

public class JobStatusTable {
	
	private final JobStatusCode statusCode;
	private final String  returnTable;
	private final Integer codError;
	
	public JobStatusTable(String statusCode, String returnTable, Integer coddError) {
		super();
		this.statusCode = JobStatusCode.fromCode(statusCode);
		this.returnTable = returnTable;
		this.codError = coddError;
	}

	/**
	 * @return the statusCode
	 */
	public JobStatusCode getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the returnTable
	 */
	public String getReturnTable() {
		return returnTable;
	}


	/**
	 * @return the codError
	 */
	public Integer getCodError() {
		return codError;
	}

	@Override
	public String toString() {
		return "jobStatusTable [statusCode=" + statusCode + ", returnTable=" + returnTable + ", codError=" + codError
				+ ", getStatusCode()=" + getStatusCode() + ", getReturnTable()=" + getReturnTable() + ", getCodError()="
				+ getCodError() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
