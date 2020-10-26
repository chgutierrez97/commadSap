package ast.sap.connector.chain.status;

/**
 * Informacion del estado de una cadena.
 * 
 * @see https://www.sapdatasheet.org/abap/func/rspc_api_chain_get_status.html
 * 
 * @author martin.zaragoza
 *
 */
public class ChainStatus {
	// TODO : VERIFICAR QUE LOS TIPOS SEAN CORRECTOS
	private ChainStatusCode status;
	private String manualAbort;
	private String readableStatus;

	public ChainStatus(ChainStatusCode status, String manualAbort, String readableStatus) {
		this.status = status;
		this.manualAbort = manualAbort;
		this.readableStatus = readableStatus;
	}

	public ChainStatusCode getStatus() {
		return status;
	}

	public String getManualAbort() {
		return manualAbort;
	}

	public String getReadableStatus() {
		return readableStatus;
	}

	@Override
	public String toString() {
		return "ChainStatus [status=" + status + ", manualAbort=" + manualAbort + ", readableStatus=" + readableStatus + "]";
	}
	
	

}
