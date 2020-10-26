package ast.sap.connector.func;

import com.sap.conn.jco.JCoStructure;

public class SapStruct {
	private final JCoStructure jCoStructure;

	public SapStruct(JCoStructure structure) {
		this.jCoStructure = structure;
	}

	public SapStruct getStructure(String fieldName) {
		return new SapStruct(jCoStructure.getStructure(fieldName));
	}

	@SuppressWarnings("unchecked")
	public <E> E getValue(String fieldName) {
		return (E) jCoStructure.getValue(fieldName);
	}

	public SapStruct setValue(String fieldName, Object value) {
		jCoStructure.setValue(fieldName, value);
		return this;
	}
}
