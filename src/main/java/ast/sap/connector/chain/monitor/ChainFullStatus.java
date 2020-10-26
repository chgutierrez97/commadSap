package ast.sap.connector.chain.monitor;

import ast.sap.connector.chain.processes.ProcessLogPair;
import ast.sap.connector.chain.status.ChainStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estructura para el retorno del monitoreo de la cadena
 *
 * @author franco.milanese
 */
public class ChainFullStatus {
	public static final Logger LOGGER = LoggerFactory.getLogger(ChainFullStatus.class);
	private ChainStatus chainStatus;
	private List<ProcessLogPair> processLogPairs;

	/**
	 * @param chainStatus     Estado final de la cadena.
	 * @param processLogPairs Listado de Procesos que contiene la cadena.
	 */
	public ChainFullStatus(ChainStatus chainStatus, List<ProcessLogPair> processLogPairs) {
		this.chainStatus = chainStatus;
		this.processLogPairs = Collections.unmodifiableList(processLogPairs);
	}

	public ChainStatus getChainStatus() {
		return chainStatus;
	}

	public List<ProcessLogPair> getProcessLogPairs() {
		return processLogPairs;
	}

	@Override
	public String toString() {
		return "ChainFullStatus [chainStatus=" + chainStatus + ", processLogPairs=" + processLogPairs + "]";
	}

	/**
	 * Obtiene los procesos y logs con errores.
	 *
	 * @return procesos y logs con errores.
	 */
	public List<ProcessLogPair> getProcessesWithErrors() {
		LOGGER.debug("Buscando procesos con errores en logs de cadena");
		List<ProcessLogPair> processes = new ArrayList<>();
		for (ProcessLogPair processLogPair : processLogPairs) {
			if (processLogPair.getProcessEntry().hasError()) {
				processes.add(processLogPair);
			}
		}
		return processes;
		//		return FluentIterable.from(processLogPairs).filter(new Predicate<ProcessLogPair>() {
		//			@Override
		//			public boolean apply(ProcessLogPair processLogPair) {
		//				return processLogPair.getJobLog().hasError();
		//			}
		//		}).toList();
	}
}
