package ast.sap.connector.chain.logs;

import com.google.common.base.Optional;

import ast.sap.connector.chain.ChainData;

public class ChainLogReaderInput {

	private String chainId;
	private String logId;
	private Optional<Boolean> dontPoll = Optional.absent();

	public ChainLogReaderInput(String chainId, String logId, boolean... bs) {
		this.chainId = chainId;
		this.logId = logId;

		if (bs.length > 0)
			dontPoll = Optional.of(bs[0]);
	}
	
	public ChainLogReaderInput(ChainData chainData, boolean... bs) {
		this.chainId = chainData.getChain();
		this.logId = chainData.getLogId();

		if (bs.length > 0)
			dontPoll = Optional.of(bs[0]);
	}

	public String getChainId() {
		return chainId;
	}

	public String getLogId() {
		return logId;
	}

	public Optional<Boolean> getDontPoll() {
		return dontPoll;
	}

}
