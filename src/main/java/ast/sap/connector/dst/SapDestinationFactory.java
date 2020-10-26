package ast.sap.connector.dst;

import com.google.common.base.Preconditions;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

import ast.sap.connector.dst.exception.DestinationGetFailException;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.util.DestinationConfigBuilder;

public enum SapDestinationFactory {
	INSTANCE;

	/**
	 * Construye un destino de sap (configuracion de conexion) a partir de un "nombre de destino". Debe existir un archivo llamado
	 * "destinationName".jcoDestination en el directorio raiz del programa.<br/>
	 * 
	 * Para construir un jcoDestination facilmente usar {@link DestinationConfigBuilder}.
	 * 
	 * @param destinationName
	 *            - Nombre del archivo destino sap (sin extension).
	 * @return Destino de Sap configurado.
	 * @throws DestinationGetFailException
	 *             Si el destino indicado no existe o si ocurrio un error al levantarlo.
	 */
	public SapDestination getDestination(String destinationName) throws DestinationGetFailException {
		Preconditions.checkNotNull(destinationName, "El nombre del destino no puede ser nulo");

		try {
			return new SapDestination(JCoDestinationManager.getDestination(destinationName));
		} catch (RepositoryGetFailException | JCoException e) {
			throw new DestinationGetFailException(String.format("Ocurrio un error al intentar obtener el destino %s", destinationName), e);
		}
	}
}
