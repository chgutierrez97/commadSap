package ast.sap.connector.job.create;

import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Optional;

import ast.sap.connector.job.variant.VariantKeyValuePair;

/**
 * Representa un trio de valores programa-variante-valoresDeVariante
 * 
 * @author martin.zaragoza
 *
 */
public class StepVariantValuesTuple extends StepVariantPair {
	private Optional<Collection<VariantKeyValuePair>> variantValuePairs = Optional.absent();

	public StepVariantValuesTuple(String program, String variant) {
		super(program, variant);
	}

	public StepVariantValuesTuple(String program) {
		super(program);
	}

	public StepVariantValuesTuple(String program, String variant, Collection<VariantKeyValuePair> variantValuePairs) {
		super(program, variant);
		this.variantValuePairs = Optional.fromNullable(Collections.unmodifiableCollection(variantValuePairs));
	}

	public Optional<Collection<VariantKeyValuePair>> getVariantValuePairs() {
		return variantValuePairs;
	}
}
