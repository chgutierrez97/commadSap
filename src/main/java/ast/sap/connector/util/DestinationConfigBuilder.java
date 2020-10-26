package ast.sap.connector.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataProvider;

/**
 * Constructor de archivos de conexion de JCO3.
 * 
 * @author martin.zaragoza
 *
 */
public enum DestinationConfigBuilder {
	INSTANCE;

	public static final String JCO_DESTINATION_FILE_EXTENSION = ".jcoDestination";

	/**
	 * Crea un archivo de conexion de JCO3 al server sap. El archivo se creara en el directorio raiz del componente, la extension del mismo sera
	 * ".jcoDestination".
	 * 
	 * @param destinationName
	 *            - Nombre del archivo (sin extension).
	 * @param connectionData
	 *            - Datos de conexion del server sap destino.
	 * @return Referencia a archivo creado.
	 */
	public File build(String destinationName, ConnectionData connectionData) {
		Properties connectProperties = buildProperties(connectionData);
		return createDestinationDataFile(destinationName, connectProperties);
	}

	/**
	 * Crea un archivo de conexion de JCO3 al server sap. El archivo se creara en el directorio raiz del componente, la extension del mismo sera
	 * ".jcoDestination". El archivo contendra configuraciones sobre soporte de pool de conexiones de JCO.
	 * 
	 * @param destinationName
	 *            - Nombre del archivo (sin extension).
	 * @param connectionData
	 *            - Datos de conexion del server sap destino.
	 * @param poolCapacity
	 *            - Cantidad de conexiones del pool.
	 * @param peakLimit
	 *            - Limite de conexiones.
	 * @return Referencia a archivo creado.
	 */
	public File buildWithPool(String destinationName, ConnectionData connectionData, int poolCapacity, int peakLimit) {
		Properties connectProperties = buildProperties(connectionData);

		connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, Integer.toString(poolCapacity));
		connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, Integer.toString(peakLimit));

		return createDestinationDataFile(destinationName, connectProperties);
	}

	/**
	 * Obtiene un archivo de configuracion de destino de sap. El archivo sera buscado dentro del directorio raiz del componente. La extension del archivo
	 * buscado debe ser .jcoDestination .
	 * 
	 * @param destinationName
	 *            - Nombre del archivo (sin extension).
	 * @return Archivo de configuracion de destino.
	 */
	public File getJcoDestinationFile(String destinationName) {
		return new File(destinationName + JCO_DESTINATION_FILE_EXTENSION);
	}

	/**
	 * Obtiene un objeto Properties a partir del archivo de configuracion de destino SAP. El archivo debe existir en el directorio raiz del componente y debe
	 * poseer extension .jcoDestination .
	 * 
	 * @param destinationName
	 *            - Nombre del archivo destino (sin extension).
	 * @return Objeto properties con propiedades del archivo destino de sap cargadas.
	 * @throws FileNotFoundException
	 *             Si el archivo no existe.
	 * @throws IOException
	 *             Si las propiedades del archivo no pueden ser leidas.
	 */
	public Properties getJcoDestinationProperties(String destinationName) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		FileInputStream destinationStream = new FileInputStream(getJcoDestinationFile(destinationName));
		properties.load(destinationStream);
		return properties;
	}

	private Properties buildProperties(ConnectionData connectionData) {
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, connectionData.getHost());
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, connectionData.getSystemNumber());
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, connectionData.getSapClient());
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, connectionData.getUserId());
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, connectionData.getPassword());
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG, connectionData.getLanguage());

		return connectProperties;
	}

	private File createDestinationDataFile(String destinationName, Properties connectProperties) {
		File destCfg = getJcoDestinationFile(destinationName);

		if (destCfg.isDirectory()) {
			throw new IllegalStateException("El archivo " + destCfg.getAbsolutePath() + " es un directorio!");
		}

		try {
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "for tests only !");
			fos.close();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create the destination files", e);
		}
		return destCfg;
	}

}
