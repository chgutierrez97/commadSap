package ast.sap.connector.job.create;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * Representa un par step/programa y variante.
 * 
 * @author martin.zaragoza
 *
 */
public class StepVariantPair {
	private final String program;
	private Optional<String> variant = Optional.absent();

	public StepVariantPair(String program) {
		Preconditions.checkNotNull(program, "El nombre del programa de ABAP no puede ser nulo!");
		this.program = program;
	}

	public StepVariantPair(String program, String variant) {
		this(program);
		this.variant = Optional.fromNullable(variant);
	}

	public String getProgram() {
		return program;
	}

	public Optional<String> getVariant() {
		return variant;
	}

	@Override
	public String toString() {
		return "StepVariantPair [program=" + program + ", variant=" + variant + "]";
	}
}
