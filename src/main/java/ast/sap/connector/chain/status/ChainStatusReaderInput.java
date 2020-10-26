package ast.sap.connector.chain.status;

import com.google.common.base.Optional;

public class ChainStatusReaderInput {
	private String chainId;
	private String logId;
	private Optional<Boolean> dontUpdate = Optional.absent();
	private Optional<Boolean> dontPoll = Optional.absent();

	/**
	 * Crea una instancia de datos de lectura de una cadena.
	 * 
	 * @param chainId
	 *            Id / nombre de la cadena.
	 * @param logId
	 *            Id del log de la cadena.
	 * @param bs
	 *            [OPCIONAL] banderas para modificar el comportamiento por defecto. dontUpdate: "Don't adjust status (less impact)" ; dontPoll : Don't
	 *            recalculate status (better performance).
	 */
	public ChainStatusReaderInput(String chainId, String logId, boolean... bs) {
		this.chainId = chainId;
		this.logId = logId;

		if (bs.length > 0) dontUpdate = Optional.of(bs[0]);
		if (bs.length > 1) dontPoll = Optional.of(bs[1]);
	}

	public String getChainId() {
		return chainId;
	}

	public String getLogId() {
		return logId;
	}

	public Optional<Boolean> getDontUpdate() {
		return dontUpdate;
	}

	public Optional<Boolean> getDontPoll() {
		return dontPoll;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public void setDontUpdate(Optional<Boolean> dontUpdate) {
		this.dontUpdate = dontUpdate;
	}

	public void setDontPoll(Optional<Boolean> dontPoll) {
		this.dontPoll = dontPoll;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chainId == null) ? 0 : chainId.hashCode());
		result = prime * result + ((dontPoll == null) ? 0 : dontPoll.hashCode());
		result = prime * result + ((dontUpdate == null) ? 0 : dontUpdate.hashCode());
		result = prime * result + ((logId == null) ? 0 : logId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ChainStatusReaderInput other = (ChainStatusReaderInput) obj;
		if (chainId == null) {
			if (other.chainId != null) return false;
		} else if (!chainId.equals(other.chainId)) return false;
		if (dontPoll == null) {
			if (other.dontPoll != null) return false;
		} else if (!dontPoll.equals(other.dontPoll)) return false;
		if (dontUpdate == null) {
			if (other.dontUpdate != null) return false;
		} else if (!dontUpdate.equals(other.dontUpdate)) return false;
		if (logId == null) {
			if (other.logId != null) return false;
		} else if (!logId.equals(other.logId)) return false;
		return true;
	}
}
