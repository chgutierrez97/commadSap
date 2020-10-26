package ast.sap.connector.func;

import com.google.common.base.Preconditions;
import com.sap.conn.jco.ConversionException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRuntimeException;
import com.sap.conn.jco.JCoTable;

import ast.sap.connector.func.exception.NonexistentTableParameterException;

public class SapFunctionResult {
	private final JCoFunction jCoFunction;

	public SapFunctionResult(JCoFunction jCoFunction) {
		this.jCoFunction = jCoFunction;
	}

	public Object getOutParameterValue(String paramKey) {
		return jCoFunction.getExportParameterList().getValue(paramKey);
	}

	public SapStruct getStructure(String fieldName) {
		return new SapStruct(jCoFunction.getExportParameterList().getStructure(fieldName));
	}

	public OutTableParam getOutTableParameter(String tableName) throws NonexistentTableParameterException {
		Preconditions.checkNotNull(tableName, "El nombre del campo tabla no puede ser nulo");
		try {
			JCoTable jcoTable = jCoFunction.getTableParameterList().getTable(tableName);
			return new TableParam(jcoTable);
		} catch (ConversionException e) {
			throw new NonexistentTableParameterException(
					String.format("El parametro %s no es de tipo tabla en la funcion %s", tableName, jCoFunction.getName()), e);
		} catch (JCoRuntimeException e) {
			throw new NonexistentTableParameterException(String.format("No existe el parámetro %s en la funcion %s", tableName, jCoFunction.getName()), e);
		}
	}
	
}
