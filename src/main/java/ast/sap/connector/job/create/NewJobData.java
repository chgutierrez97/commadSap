package ast.sap.connector.job.create;

import ast.sap.connector.func.SapBapiret2;

/**
 * Informacion del nuevo job creado.
 * 
 * @author martin.zaragoza
 *
 */
public class NewJobData {
	private final String jobCount;
	private final SapBapiret2 ret;

	public NewJobData(String jobCount, SapBapiret2 ret) {
		this.jobCount = jobCount;
		this.ret = ret;
	}

	public String getJobCount() {
		return jobCount;
	}

	public SapBapiret2 getRet() {
		return ret;
	}

	@Override
	public String toString() {
		return "NewJobData [jobCount=" + jobCount + ", ret=" + ret + "]";
	}

	public boolean hasError() {
		return ret.hasError();
	}
}
