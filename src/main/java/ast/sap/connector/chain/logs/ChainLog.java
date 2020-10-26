package ast.sap.connector.chain.logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;

/**
 * Representa el log de una cadena.
 * 
 * @author franco.milanese
 *
 */
public class ChainLog {
	private List<ChainLogEntry> entries = new ArrayList<ChainLogEntry>();

	/**
	 * Crea una instancia de entradas de log de cadena.
	 * 
	 * @param entriesTable
	 *            Tabla de logs de cadena a parsear https://www.sapdatasheet.org/abap/tabl/rspc_s_msg.html.
	 */
	public ChainLog(OutTableParam entriesTable) {
		parseEntries(entriesTable);
	}

	private void parseEntries(OutTableParam entriesTable) {
		int rowCount = entriesTable.getRowCount();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			try {
				OutTableRow currentRow = entriesTable.currentRow();
				ChainLogEntry chainLogEntry = new ChainLogEntry(currentRow);
				entries.add(chainLogEntry);
			} catch (Exception e) {
				throw new ChainLogParseException("Ocurrio un error al parsear el log de la cadena", e);
			}
			entriesTable.nextRow();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ChainLog [chainLogEntries=");
		for (ChainLogEntry chainLogEntry : entries) {
			sb.append("\n  " + chainLogEntry.getPrettyString());
		}
		sb.append("\n]");
		return sb.toString();
	}

	/**
	 * Obtiene las entradas de log parseadas.
	 * 
	 * @return entradas de log parseadas
	 */
	public List<ChainLogEntry> getEntries() {
		return Collections.unmodifiableList(this.entries);
	}

	/**
	 * Imprime el log en la terminal.
	 */
	public void printLogStdout() {
		for (ChainLogEntry chainLogEntry : entries) {
			System.out.println(chainLogEntry);
		}
	}
}
