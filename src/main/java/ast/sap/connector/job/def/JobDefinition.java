package ast.sap.connector.job.def;

import ast.sap.connector.func.SapBapiret2;

public class JobDefinition {

	private final SapBapiret2 sapBapiret2;
	private final BapiXmJob bapiXmJob;

	public JobDefinition(SapBapiret2 sapBapiret2, BapiXmJob bapiXmJob) {
		this.sapBapiret2 = sapBapiret2;
		this.bapiXmJob = bapiXmJob;
	}

	public SapBapiret2 getSapBapiret2() {
		return sapBapiret2;
	}

	public BapiXmJob getBapiXmJob() {
		return bapiXmJob;
	}

}
