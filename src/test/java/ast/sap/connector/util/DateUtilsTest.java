package ast.sap.connector.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testAddHours() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat hoursFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss");

		Date base = dateFormat.parse("2017/03/21");
		Date hours = hoursFormat.parse("03:45:17");
		Date res = DateUtils.INSTANCE.addHours(base, hours);

		String expected = "2017/03/21:03:45:17";
		assertEquals(expected, fullFormat.format(res));
	}

}
