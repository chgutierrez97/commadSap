package ast.sap.connector.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Parser de valores numericos.
 * 
 * @author martin.zaragoza
 *
 */
public enum NumberParser {
	/**
	 * Parsea numeros del tipo 1.234.567,33 [. es separador de miles , es separador decimal]
	 */
	SPANISH("-?\\d{1,3}(\\.\\d{3})*(\\,\\d+)?", Locale.GERMANY),
	/**
	 * Parsea numeros del tipo 1,234,567.33 [, es separador de miles . es separador decimal]
	 */
	US("-?\\d{1,3}(\\,\\d{3})*(\\.\\d+)?", Locale.US),
	/**
	 * Parsea numeros del tipo 1234567.33 [no hay separador de miles . es separador decimal]
	 */
	JAVA("-?\\d+(\\.\\d+)?", Locale.US);

	public final String regex;
	private final Locale locale;

	private NumberParser(String regex, Locale locale) {
		this.regex = regex;
		this.locale = locale;
	}

	/**
	 * Parsea un valor numerico.
	 * 
	 * @param numberStr
	 *            String de valor numerico.
	 * @return Numero parseado
	 * @throws ParseException
	 *             Si el valor numerico es invalido.
	 */
	public Number parse(String numberStr) throws ParseException {
		if (numberStr == null) throw new ParseException("El valor a parsear es nulo", 0);

		if (numberStr.matches(regex)) return NumberFormat.getInstance(locale).parse(numberStr.trim());

		throw new ParseException(String.format("Formato de numero %s invalido para locale %s", numberStr, this.toString()), 0);
	}
}
