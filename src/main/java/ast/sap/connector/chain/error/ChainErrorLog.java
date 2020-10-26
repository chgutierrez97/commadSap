package ast.sap.connector.chain.error;

import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.List;

public class ChainErrorLog {
	private List<ChainErrorLogEntry> errorLogEntries = new ArrayList<>();

	public ChainErrorLog(OutTableParam logDetails) {
		int rowCount = logDetails.getRowCount();
		for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
			OutTableRow currentRow = logDetails.currentRow();
			errorLogEntries.add(new ChainErrorLogEntry(currentRow));
			logDetails.nextRow();
		}
	}

	/**
	 * Obtiene las entradas del log.
	 * @return Obtiene todas las entradas del log obtenidas.
	 */
	public List<ChainErrorLogEntry> getEntries() { return new ArrayList<>(errorLogEntries); }

	/**
	 * Obtiene las entradas de log explicitamente marcadas como erroneas mediante {@link ChainErrorLogEntry#hasError()}
	 * @return las entradas de log explicitamente marcadas como erroneas mediante {@link ChainErrorLogEntry#hasError()}
	 */
	public List<ChainErrorLogEntry> getErrorEntries() {
		return FluentIterable.from(errorLogEntries).filter(new Predicate<ChainErrorLogEntry>() {
			@Override public boolean apply(ChainErrorLogEntry input) { return input.hasError(); }
		}).toList();
	}
}
