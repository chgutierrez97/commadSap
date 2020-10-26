package ast.sap.connector.job.variant;

import com.google.common.base.Optional;

import ast.sap.connector.func.SapBapiret2;

public class Varinfo {
	private SapBapiret2 ret;
	private Optional<Variant> variant = Optional.absent();

	public Varinfo(SapBapiret2 ret, Variant variant) {
		this.ret = ret;
		this.variant = Optional.fromNullable(variant);
	}

	public Varinfo(SapBapiret2 ret) {
		this.ret = ret;
	}

	public SapBapiret2 getRet() {
		return ret;
	}

	public Optional<Variant> getVariant() {
		return variant;
	}

	@Override
	public String toString() {
		return "Varinfo [ret=" + ret + ", variant=" + variant + "]";
	}
}
