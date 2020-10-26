package ast.sap.connector.job.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;

/**
 * @author franco.milanese
 *
 */
public class Variant {
	private List<VariantEntry> variantEntries = new ArrayList<VariantEntry>();

	public Variant(OutTableParam outTableParameter) {
		parseVariantList(outTableParameter);
	}

	private void parseVariantList(OutTableParam outTableParameter) {
		for (int i = 0; i < outTableParameter.getRowCount(); i++) {
			OutTableRow outTableRow = outTableParameter.currentRow();
			VariantEntry variantInfo = new VariantEntry(outTableRow);
			variantEntries.add(variantInfo);
			outTableParameter.nextRow();
		}
	}

	public List<VariantEntry> getVariantEntries() {
		return Collections.unmodifiableList(variantEntries);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TABLE VARIANT_INFO");
		for (VariantEntry varInfo : variantEntries) {
			sb.append("\n  " + varInfo.toString());
		}
		sb.append("\n]");
		return sb.toString();
	}
}
