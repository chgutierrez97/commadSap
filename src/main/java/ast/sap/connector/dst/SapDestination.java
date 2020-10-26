package ast.sap.connector.dst;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sap.conn.jco.JCoAttributes;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoRepository;

import ast.sap.connector.dst.exception.ContextCloseException;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.xmi.XmiSession;

/**
 * Representa una conexion con el server SAP.
 * 
 * @author martin.zaragoza
 *
 */
public class SapDestination {
	private final JCoDestination jcoDestination;

	SapDestination(JCoDestination jcoDestination) throws RepositoryGetFailException {
		Preconditions.checkNotNull(jcoDestination, "El destino no puede ser nulo");
		this.jcoDestination = jcoDestination;
	}

	/**
	 * Inicia un contexto de sesion para la invocacion de funciones consecutivas
	 * que requieran manejo de estado (por ejemplo las funciones BAPI_XBP,
	 * requieren iniciar sesion con XMI mediante XMI_LOGON).
	 * 
	 * @see XmiSession
	 * 
	 * @return Repositorio de funciones de sap persistente.
	 * @throws RepositoryGetFailException
	 *             Si ocurre un error al obtener el repositorio de funciones de
	 *             sap.
	 */
	public SapRepository openContext() throws RepositoryGetFailException {
		JCoContext.begin(jcoDestination);
		return getRepository();
	}

	public SapDestination closeContext() {
		try {
			JCoContext.end(jcoDestination);
			return this;
		} catch (JCoException e) {
			throw new ContextCloseException("Error al cerrar la sesion con sap", e);
		}
	}

	/**
	 * Obtiene un repositorio de funciones de sap sin contexto ni estado.
	 * 
	 * @return repositorio de funciones de sap sin contexto ni estado.
	 * @throws RepositoryGetFailException
	 *             Si ocurrio un error al obtener el repositorio.
	 */
	public SapRepository statelessRepository() throws RepositoryGetFailException {
		return getRepository();
	}

	private SapRepository getRepository() throws RepositoryGetFailException {
		try {
			Optional<JCoRepository> jcoRepository = Optional.fromNullable(jcoDestination.getRepository());
			if (jcoRepository.isPresent()) {
				return new SapRepository(jcoRepository.get(), jcoDestination);
			}

			throw new RepositoryGetFailException("No es posible obtener el repositorio de " + jcoDestination);
		} catch (JCoException e) {
			throw new RepositoryGetFailException(
					"Error al iniciar sesion de sap u obtener el respositorio del destino " + jcoDestination, e);
		}
	}

	public JCoAttributes getAttributes() throws JCoException {
		return jcoDestination.getAttributes();
	}
}
