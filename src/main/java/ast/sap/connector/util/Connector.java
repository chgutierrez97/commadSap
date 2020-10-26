package ast.sap.connector.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.sap.conn.jco.JCoException;

import ast.sap.connector.dst.SapDestination;
import ast.sap.connector.dst.SapDestinationFactory;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.RepositoryGetFailException;

/**
 * Manejador de conexiones y contextos de repositorios de sap.
 * 
 * @author martin.zaragoza
 *
 */
public enum Connector {
	INSTANCE;

	private String destinationName;
	private SapDestination destination;

	public String getDestinationName() {
		return destinationName;
	}

	public SapDestination getDestination() {
		Preconditions.checkNotNull(destination, "No hay un destino de sap cargado. Invocar a loadDestination() primero.");
		return destination;
	}

	/**
	 * Configura el componente para luego abrir una sesion con sap.
	 * 
	 * @param destinationName
	 *            Nombre del destino sap al cual conectarse.
	 * @return this.
	 */
	public Connector config(String destinationName) {
		if (Strings.isNullOrEmpty(destinationName)) throw new IllegalArgumentException("El nombre del destino no puede ser nulo o vacio!");
		this.destinationName = destinationName;
		return this;
	}

	/**
	 * Carga el destino de sap configurado mediante {@link Connector#config(String)}.
	 * 
	 * @return This.
	 * @throws JCoException
	 *             En caso que ocurra un error al conectarse con el servidor sap.
	 */
	public Connector loadDestination() throws JCoException {
		destination = SapDestinationFactory.INSTANCE.getDestination(destinationName);
		destination.getAttributes();
		return this;
	}

	/**
	 * Abre un contexto persistente y obtiene un repositorio de funciones de sap.
	 * 
	 * @return repositorio de funciones de sap.
	 * @throws RepositoryGetFailException
	 *             En caso que ocurra un error al obtener el repositorio.
	 */
	public SapRepository openContext() throws RepositoryGetFailException {
		return destination.openContext();
	}

	/**
	 * Cierra el contexto persistente de sap activo.
	 */
	public void closeContext() {
		destination.closeContext();
	}
}
