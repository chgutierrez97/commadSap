package ast.sap.connector.func;

import com.sap.conn.jco.JCoTable;

class TableParam implements OutTableParam, InTableParam, OutTableRow, InTableRow {
	private final JCoTable jcoTable;

	TableParam(JCoTable table) {
		this.jcoTable = table;
	}

	@Override
	public InTableRow appendRow() {
		jcoTable.appendRow();
		return this;
	}

	@Override
	public InTableRow setValue(String column, Object value) {
		jcoTable.setValue(column, value);
		return this;
	}

	@Override
	public OutTableRow nextRow() {
		jcoTable.nextRow();
		return this;
	}
	
	@Override
	public OutTableRow currentRow() {
		return this;
	}

	@Override
	public Object getValue(String colName) {
		return jcoTable.getValue(colName);
	}

	@Override
	public int getRowCount() {
		return jcoTable.getNumRows();
	}

	@Override
	public boolean isEmpty() {
		return jcoTable.isEmpty();
	}

	@Override
	public String toString() {
		return "TableParam [jcoTable=" + jcoTable + "]";
	}
}
