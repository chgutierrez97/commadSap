package ast.sap.connector.job.variant;

import java.util.Collection;

public interface ChangeVariantData extends ReadVariantData {

	/**
	 * Obtiene los pares clave->valor para la modificacion de las variantes.
	 * 
	 * @return pares clave->valor para la modificacion de las variantes.
	 */
	public abstract Collection<VariantKeyValuePair> getVariantValuePairs();

}