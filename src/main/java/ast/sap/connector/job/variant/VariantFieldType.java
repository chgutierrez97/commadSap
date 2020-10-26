package ast.sap.connector.job.variant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ast.sap.connector.config.Configuration;

public enum VariantFieldType {
	DATE, NUMBER, TEXT;

	/**
	 * Transforma un valor dependiendo de su tipo.
	 * 
	 * @param value
	 *            Valor a transformar.
	 * @return String transformado.
	 * @throws ParseException
	 *             En caso que el formato del valor sea invalido.
	 */
	public String transform(String value) throws ParseException {
		switch (this) {
			case DATE:
				SimpleDateFormat dateFormat = Configuration.getArgsInDateFormat();
				Date date = dateFormat.parse(value);
				return Configuration.getSapOutDateFormat().format(date);

			case NUMBER:
				String regex = Configuration.getSapOutNumberRegex();
				if (value.matches(regex)) return value;
				throw new ParseException("El valor a asignar es un numero invalido", 0);
			default:
				return value;
		}
	}
}
