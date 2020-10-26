package ast.sap.connector.chain;

/**
 * @author franco.milanese
 *
 *         Datos basicos de una Process Chain RSPC
 */
public class ChainData {

	private String logId;
	private String chain;

	public ChainData(String logId, String chain) {
		this.chain = chain;
		this.logId = logId;
	}

	public ChainData(String logId) {
		this.logId = logId;
	}

	public String getChain() {
		return chain;
	}

	public String getLogId() {
		return logId;
	}

	@Override
	public String toString() {
		return "ChainData [logId=" + logId + ", chain=" + chain + "]";
	}

}
