package ast.sap.connector.job.read;

import ast.sap.connector.func.SapBapiret2;

public class JobHead {
	private final SapBapiret2 ret;
	private final Bp20job bp20job;

	public JobHead(SapBapiret2 ret, Bp20job bp20job) {
		this.ret = ret;
		this.bp20job = bp20job;
	}

	public SapBapiret2 getRet() {
		return ret;
	}

	public Bp20job getBp20job() {
		return bp20job;
	}
}
