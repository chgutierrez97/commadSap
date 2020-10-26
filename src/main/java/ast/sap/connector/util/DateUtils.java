package ast.sap.connector.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Herramientas para manipulacion de fechas.
 * 
 * @author martin.zaragoza
 *
 */
public enum DateUtils {
	INSTANCE;

	/**
	 * Suma las horas del segundo objeto Date al primer objeto Date.
	 * 
	 * @param base
	 *            - Fecha base (dia, mes, ano) a la cual se le sumaran horas.
	 * @param hours
	 *            - Fecha a usar para sumar las horas.
	 * @return Primera fecha con horas sumadas de la segunda fecha.
	 */
	public Date addHours(Date base, Date hours) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(base);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(hours);

		cal1.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
		cal1.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
		cal1.set(Calendar.SECOND, cal2.get(Calendar.SECOND));

		return cal1.getTime();
	}
	
	/**
	 * Suma las horas del segundo objeto Date al primer objeto Date.
	 * 
	 * @param base
	 *            - Fecha base (dia, mes, ano) a la cual se le sumaran horas.
	 * @param hours
	 *            - Fecha a usar para sumar las horas.
	 * @return Primera fecha con horas sumadas de la segunda fecha.
	 */
	public Date addDays(Date fecha, int days) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(fecha);
		cal1.add(Calendar.DAY_OF_YEAR, days);
		return cal1.getTime();
	}
}
