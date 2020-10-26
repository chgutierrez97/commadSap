package ast.sap.connector.job.track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * Codigos de status de jobs.
 * 
 * @see https://archive.sap.com/discussions/thread/211601
 * @see https://help.sap.com/saphelp_nw70/helpdata/en/c4/3a8016505211d189550000e829fbbd/frameset.htm
 * 
 * @author martin.zaragoza
 *
 */
public enum JobStatusCode {
	A("Cancelled"),
	F("Finished"),
	P("Scheduled"),
	R("Active"),
	S("Released"),
	Y("Ready"),
	N("NotFound"),
	Z("Active"),
	I("Intercepted"),
	X("Unknown");

	public static final Logger LOGGER = LoggerFactory.getLogger(JobStatusCode.class);

	public final String label;

	private JobStatusCode(String lbl) {
		this.label = lbl;
	}

	public boolean isRunning() {
		return this.equals(R) || this.equals(Z);
	}

	public boolean isReleased() {
		return this.equals(S);
	}

	public boolean hasFinished() {
		return this.equals(F);
	}

	public boolean notFinished() {
		return !hasFinished();
	}
	
	/**
	 * Obtiene una instancia del estado a partir de un codigo de estado.
	 * 
	 * @param code
	 *            Codigo de estado a interpretar.
	 * @return Estado de job.
	 */
	public static JobStatusCode fromCode(String code) {
		try {
			return JobStatusCode.valueOf(Optional.fromNullable(code).or(""));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Estado de job invalido o desconocido: " + code);
			return JobStatusCode.X;
		}
	}

	@Override
	public String toString() {
		return String.format("%s - %s", this.name(), this.label);
	}
}
