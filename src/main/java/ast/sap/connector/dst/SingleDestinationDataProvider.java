package ast.sap.connector.dst;

import java.util.Properties;

import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import ast.sap.connector.util.ConnectionData;

/**
 * Proveedor de datos de destinos de sap.
 * 
 * @author martin.zaragoza
 *
 */
public class SingleDestinationDataProvider implements DestinationDataProvider {
	private final String destination;
	private final ConnectionData connectionData;

	private SingleDestinationDataProvider(String destination, ConnectionData connectionData) {
		this.destination = destination;
		this.connectionData = connectionData;
	}

	/**
	 * Crea un nuevo proveedor de destinos de sap.
	 * 
	 * @param destination
	 *            Nombre del destino de sap.
	 * @param connectionData
	 *            Datos de conexion con el destino sap.
	 * @return nuevo proveedor de destinos de sap.
	 */
	public static SingleDestinationDataProvider buildNew(String destination, ConnectionData connectionData) {
		return new SingleDestinationDataProvider(destination, connectionData);
	}

	@Override
	public Properties getDestinationProperties(String destinationName) throws DataProviderException {
		if (destination.equals(destinationName)) {
			Properties connectProperties = new Properties();
			connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectionData.getHost());
			connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectionData.getSystemNumber());
			connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, connectionData.getSapClient());
			connectProperties.setProperty(DestinationDataProvider.JCO_USER, connectionData.getUserId());
			connectProperties.setProperty(DestinationDataProvider.JCO_LANG, connectionData.getLanguage());

			/* TODO : EL PASSWORD DEBERIA VIAJAR ENCRIPTADO Y SE DEBERIA DESENCRIPTAR AQUI */
			connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, connectionData.getPassword());
			return connectProperties;
		} else {
			return null;
		}
	}

	@Override
	public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {}

	@Override
	public boolean supportsEvents() {
		return false;
	}

	/**
	 * Registra el proveedor de datos de destinos de sap para que sea accesible desde el {@link JCoDestinationManager}.
	 * 
	 * @return this.
	 * @throws IllegalStateException
	 *             Si el proveedor de datos de destino ya fue registrado antes.
	 */
	public SingleDestinationDataProvider register() throws IllegalStateException {
		com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(this);
		return this;
	}

	public String getDestination() {
		return destination;
	}
}
