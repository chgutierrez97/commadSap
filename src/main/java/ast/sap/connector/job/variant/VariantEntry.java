package ast.sap.connector.job.variant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ast.sap.connector.config.Configuration;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.util.NumberParser;

/**
 * @author franco.milanese
 *
 *         Estructura de la tabla BAPIVARINFO, utilizada en el manejo de variantes de programa
 */
public class VariantEntry {
	private String report;
	private String variant;
	private String pname;
	private String pkind;
	private Integer polen;
	private String ptext;
	private String psign;
	private String poption;
	private String plow;
	private String phigh;
	private VariantFieldType fieldType = VariantFieldType.TEXT;

	public VariantEntry(OutTableRow outTableRow) {
		this.report = (String) outTableRow.getValue("REPORT");
		this.variant = (String) outTableRow.getValue("VARIANT");
		this.pname = (String) outTableRow.getValue("PNAME");
		this.pkind = (String) outTableRow.getValue("PKIND");
		this.polen = (Integer) outTableRow.getValue("POLEN");
		this.ptext = (String) outTableRow.getValue("PTEXT");
		this.psign = (String) outTableRow.getValue("PSIGN");
		this.poption = (String) outTableRow.getValue("POPTION");

		this.plow = parseField((String) outTableRow.getValue("PLOW"));

		this.phigh = (String) outTableRow.getValue("PHIGH");
	}

	/**
	 * @param outTableRow
	 * 
	 *            El formato de fecha recibida al consultar la variante en SAP es distinto al formato que se debe enviar para cambiarla, por lo tanto se realiza
	 *            la conversion de dd.MM.yyyy a yyyyMMdd
	 * @return
	 */
	String parseField(String plow) {
		SimpleDateFormat dateFormat = Configuration.getSapInDateFormat();
		try {
			Date parsed = dateFormat.parse(plow);
			String valueToSet = Configuration.getSapOutDateFormat().format(parsed);
			fieldType = VariantFieldType.DATE;
			return valueToSet;
		} catch (ParseException e) {
			try {
				NumberParser numberParser = Configuration.getSapInNumberParser();
				Number number = numberParser.parse(plow.trim());
				fieldType = VariantFieldType.NUMBER;
				return number.toString();
			} catch (ParseException e1) {
				fieldType = VariantFieldType.TEXT;
				return plow;
			}
		}
	}

	public String getReport() {
		return report;
	}

	public String getVariant() {
		return variant;
	}

	public String getPname() {
		return pname;
	}

	public String getPkind() {
		return pkind;
	}

	public Integer getPolen() {
		return polen;
	}

	public String getPtext() {
		return ptext;
	}

	public String getPsign() {
		return psign;
	}

	public String getPoption() {
		return poption;
	}

	public String getPlow() {
		return plow;
	}

	public String getPhigh() {
		return phigh;
	}

	public VariantFieldType getFieldType() {
		return fieldType;
	}

	@Override
	public String toString() {
		return "VariantEntry [report=" + report + ", variant=" + variant + ", pname=" + pname + ", pkind=" + pkind + ", polen=" + polen + ", ptext=" + ptext
				+ ", psign=" + psign + ", poption=" + poption + ", plow=" + plow + ", phigh=" + phigh + ", fieldType=" + fieldType + "]";
	}
}
