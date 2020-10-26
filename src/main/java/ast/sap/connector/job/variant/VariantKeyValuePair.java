package ast.sap.connector.job.variant;

import ast.sap.connector.util.KeyValuePair;

/**
 * Representa un par clave-valor asignable al campo de una variante.
 * 
 * @author martin.zaragoza
 *
 */
public class VariantKeyValuePair extends KeyValuePair<String, String> {
	public VariantKeyValuePair(String key, String value) {
		super(key, value);
	}
}
