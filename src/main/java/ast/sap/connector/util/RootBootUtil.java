package ast.sap.connector.util;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RootBootUtil {

	public boolean comparadorDeCaracteres2(String sTexto, String sTextoBuscado) {
		sTexto = sTexto.toLowerCase();
		sTextoBuscado = sTextoBuscado.toLowerCase();
		boolean flag = false;
		int contador = 0;
		if (sTexto.indexOf(sTextoBuscado) > -1) {
			flag = true;
		}
		if (sTexto.contains("" + sTextoBuscado)) {
			flag = true;
		}
		return flag;
	}

	public boolean comparadorDeCaracteres(String sTexto, String sTextoBuscado) {
		boolean coincidencia = false;
		sTexto = limpiarAcentos(sTexto).toLowerCase();
		sTextoBuscado = limpiarAcentos(sTextoBuscado).toLowerCase();
		Pattern patron = Pattern.compile(sTextoBuscado);
		Matcher m = patron.matcher(sTexto);
		coincidencia = m.find();
		boolean flag = false;
		int contador = 0;
		if (sTexto.indexOf(sTextoBuscado) > -1) {
			flag = true;
		}
		if (sTexto.contains("" + sTextoBuscado)) {
			flag = true;
		}
		if (coincidencia) {
			flag = true;
		}
		return flag;
	}

	public static String limpiarAcentos(String cadena) {
		String limpio = null;
		if (cadena != null) {
			String original = cadena;
			String cadenaNormalize = Normalizer.normalize(original, Normalizer.Form.NFD);
			String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
			limpio = cadenaSinAcentos;
		}
		return limpio;
	}

	public Date addDays(Date fecha, int days) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(fecha);
		cal1.add(Calendar.DAY_OF_YEAR, days);
		return cal1.getTime();
	}

}
