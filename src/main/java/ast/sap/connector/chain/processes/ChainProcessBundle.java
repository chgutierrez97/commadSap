package ast.sap.connector.chain.processes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;

/**
 * Conjunto de procesos de una cadena
 * 
 * @author franco.milanese
 *
 *         Lista de la estructura RSPC_S_PROCESSLIST {@link ProcessEntry}}
 * 
 * @see https://www.sapdatasheet.org/abap/tabl/rspc_s_processlist.html
 */
public class ChainProcessBundle {

	private List<ProcessEntry> processes = new ArrayList<ProcessEntry>();

	public ChainProcessBundle(OutTableParam chainProcessesTable) {
		int rowCount = chainProcessesTable.getRowCount();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			try {
				OutTableRow currentRow = chainProcessesTable.currentRow();
				ProcessEntry entry = new ProcessEntry(currentRow);
				processes.add(entry);
			} catch (ChainProcessParseException e) {
				throw new ChainProcessParseException("Ocurrio un error al parsear la tabla de procesos de la cadena.", e);
			}
			chainProcessesTable.nextRow();
		}

	}

	public ChainProcessBundle() {
		// TODO Auto-generated constructor stub
	}

	public List<ProcessEntry> getProcesses() {
//		return new ArrayList<>(processes);
		return processes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ChainProcessBundle [processes= ");
		for (ProcessEntry processEntry : processes) {
			sb.append("\n " + processEntry.toString());
		}
		sb.append("\n]");
		return sb.toString();
	}

	public boolean addAll(Collection<? extends ProcessEntry> arg0) {
		return processes.addAll(arg0);
	}
}
