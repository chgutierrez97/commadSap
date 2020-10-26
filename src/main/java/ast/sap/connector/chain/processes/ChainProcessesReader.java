package ast.sap.connector.chain.processes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.func.exception.RspcExecuteException;

/**
 * @author franco.milanese
 * 
 */
public class ChainProcessesReader {

	public static final Logger LOGGER = LoggerFactory.getLogger(ChainProcessesReader.class);

	private final SapRepository repository;

	public ChainProcessesReader(SapRepository repository) {
		this.repository = repository;
	}

	/**
	 * Lee los procesos propios de una process chain. Por ejemplo los jobs y los triggers de la misma.
	 * 
	 * @param chainData
	 * @return ChainProcesses - Lista de procesos de la cadena
	 * 
	 * @throws RspcExecuteException
	 *             Excepcion RSPC, propia de la ejecucion de la funcion.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public ChainProcessBundle readProcesses(ChainData chainData)
			throws RspcExecuteException, FunctionNetworkErrorException, FunctionExecuteException, FunctionGetFailException {
		LOGGER.debug("Obteniendo los procesos de la cadena {} - {}", chainData.getChain(), chainData.getLogId());
		
		SapFunction function = repository.getFunction("RSPC_API_CHAIN_GET_PROCESSES")
				.setInParameter("I_CHAIN", chainData.getChain())
				.setInParameter("I_LOGID", chainData.getLogId());
		
		SapFunctionResult result = function.execute();
		ChainProcessBundle chainProcesses = new ChainProcessBundle(result.getOutTableParameter("E_T_PROCESSLIST"));
		LOGGER.debug(chainProcesses.toString());
		
		ChainProcessBundle finalBundle = new ChainProcessBundle();
		finalBundle.addAll(chainProcesses.getProcesses());
		
		for (ProcessEntry entry : chainProcesses.getProcesses()) {
			if (hasNestedProcesses(entry)) {
				ChainData childChainData = new ChainData(entry.getInstance(), entry.getVariant());
				ChainProcessBundle childChainProcessBundle = readProcesses(childChainData);
				finalBundle.addAll(childChainProcessBundle.getProcesses());
			}
		}
		
		return finalBundle;

	}

	private boolean hasNestedProcesses(ProcessEntry entry) {
		return entry.getType().equals("CHAIN");
	}

	public static ChainProcessesReader build(SapRepository sapRepository) {
		return new ChainProcessesReader(sapRepository);
	}

}
