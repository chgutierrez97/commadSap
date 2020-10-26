package ast.sap.connector.job.variant;

import java.util.Collections;
import java.util.Collection;

import ast.sap.connector.cmd.impl.ChangeVariantCommand;

import com.google.common.base.Optional;

public class VariantData implements ChangeVariantData {
	/**
	 * El nombre de la variante es opcional. De esta manera, si el nombre no se indica, entonces la variante a cambiar sera la primera encontrada relacionada
	 * con el programa.
	 */
	private Optional<String> variant = Optional.absent();
	private String program;
	private String externalUsername;
	private Collection<VariantKeyValuePair> variantValuePairs;

	
	/**
	 * @deprecated No deberia usarse. Existe solo para pruebas 
	 * @param program
	 * @param externalUsername
	 * @param variantValuePairs
	 * @see ChangeVariantCommand
	 */
	public VariantData(String program, String externalUsername, Collection<VariantKeyValuePair> variantValuePairs) {
		this.program = program;
		this.externalUsername = externalUsername;
		this.variantValuePairs = Collections.unmodifiableCollection(variantValuePairs);
	}

	public VariantData(String program, String variant, String externalUsername, Collection<VariantKeyValuePair> variantValuePairs) {
		this(program, variant, externalUsername);
		this.variantValuePairs = Collections.unmodifiableCollection(variantValuePairs);
	}

	private VariantData(String program, String variant, String externalUsername) {
		this.variant = Optional.fromNullable(variant);
		this.program = program;
		this.externalUsername = externalUsername;
	}

	/**
	 * Crea un set de datos para leer una variante.
	 * 
	 * @param program
	 *            Nombre del programa/reporte al cual corresponde la variante.
	 * @param variant
	 *            Nombre de la variante a leer.
	 * @param externalUsername
	 *            Usuario que requiere leer la variante.
	 * @return Datos para leer una variante.
	 */
	public static ReadVariantData newReadVariantData(String program, String variant, String externalUsername) {
		return new VariantData(program, variant, externalUsername);
	}

	/**
	 * Crea un set de datos para modificar una variante.
	 * 
	 * @param program
	 *            Nombre del programa/reporte al cual corresponde la variante.
	 * @param variant
	 *            Nombre de la variante a leer.
	 * @param externalUsername
	 *            Usuario que requiere leer la variante.
	 * @param variantValuePairs
	 *            Campos de la variante a modificar.
	 * @return Datos para modificar una variante.
	 */
	public static ChangeVariantData newChangeVariantData(String program, String variant, String externalUsername,
			Collection<VariantKeyValuePair> variantValuePairs) {
		return new VariantData(program, variant, externalUsername, variantValuePairs);
	};

	@Override
	public String getProgram() {
		return program;
	}

	@Override
	public Optional<String> getVariant() {
		return variant;
	}

	@Override
	public String getExternalUsername() {
		return externalUsername;
	}

	@Override
	public Collection<VariantKeyValuePair> getVariantValuePairs() {
		return variantValuePairs;
	}
}
