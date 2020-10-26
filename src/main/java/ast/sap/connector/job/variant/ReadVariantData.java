package ast.sap.connector.job.variant;

import com.google.common.base.Optional;

public interface ReadVariantData {

	public abstract String getProgram();

	public abstract Optional<String> getVariant();

	public abstract String getExternalUsername();

}