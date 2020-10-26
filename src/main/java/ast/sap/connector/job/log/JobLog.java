package ast.sap.connector.job.log;

import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapBapiret2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobLog {
	public static final Logger LOGGER = LoggerFactory.getLogger(JobLog.class);

	private final SapBapiret2 returnStruct;
	private List<LogEntry> logEntries = new ArrayList<LogEntry>();

	public JobLog(SapBapiret2 returnStruct) {
		this.returnStruct = returnStruct;
	}

	public JobLog(SapBapiret2 sapBapiret2, OutTableParam entries) {
		this.returnStruct = sapBapiret2;
		parseEntries(entries);
	}

	private void parseEntries(OutTableParam entries) {
		int rowCount = entries.getRowCount();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			try {
				OutTableRow currentRow = entries.currentRow();
				LogEntry logEntry = new LogEntry(currentRow);
				logEntries.add(logEntry);
			} catch (Exception e) {
				throw new JobLogParseException("Ocurrio un error al parsear el log del job", e);
			}
			entries.nextRow();
		}
	}

	public SapBapiret2 getReturnStruct() {
		return returnStruct;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("JobLog [returnStruct=" + returnStruct + ", logEntries=");
		for (LogEntry logEntry : logEntries) {
			sb.append("\n  " + logEntry.getPrettyString());
		}
		sb.append("\n]");
		return sb.toString();
	}

	/**
	 * Obtiene las entradas de log parseadas.
	 *
	 * @return entradas de log parseadas
	 */
	public List<LogEntry> getLogEntries() {
		return Collections.unmodifiableList(this.logEntries);
	}

	/**
	 * Imprime el log en la terminal.
	 */
	public void printLogStdout() {
		for (LogEntry logEntry : logEntries) {
			System.out.println(logEntry);
			LOGGER.debug("" + logEntry);
		}
	}

	/**
	 * Determina si el log de este job contiene alguna entrada con error.
	 *
	 * @return True si el log contiene alguna entrada con error.
	 */
	public boolean hasError() {
		for (LogEntry logEntry : logEntries) {
			if (logEntry.hasError()) { return true; }
		}
		return false;
	}
}
