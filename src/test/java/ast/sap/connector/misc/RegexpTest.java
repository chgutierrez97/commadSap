package ast.sap.connector.misc;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Optional;
import com.sap.conn.jco.JCoException;

import ast.sap.connector.util.KeyValuePair;

public class RegexpTest {
	@Test
	public void testNumber() throws ParseException {
		{
			String regex = "\\d+(\\.|\\,|\\d)*";
			assertTrue("12.4".matches(regex));
			assertTrue("12".matches(regex));
			assertTrue("12.23134".matches(regex));

			Number parsedNumber = NumberFormat.getInstance(Locale.US).parse("123,654.25");
			assertEquals("123654.25", parsedNumber.toString());
		}

		{
			String regex = "-?\\d{1,3}(\\.\\d{3})*(\\,\\d+)?";
			assertTrue("123.321".matches(regex));
			assertTrue("123".matches(regex));
			assertTrue("123,321".matches(regex));
			assertTrue("123.321,25".matches(regex));
			assertTrue("123.321.654".matches(regex));
			assertTrue("123.321.654,333".matches(regex));
			assertTrue("5".matches(regex));
			assertTrue("5,678".matches(regex));
			
			assertTrue("-123.321".matches(regex));
			assertTrue("-123".matches(regex));
			assertTrue("-123,321".matches(regex));
			assertTrue("-123.321,25".matches(regex));
			assertTrue("-123.321.654".matches(regex));
			assertTrue("-123.321.654,333".matches(regex));
			assertTrue("-5".matches(regex));
			assertTrue("-5,678".matches(regex));
			
			assertFalse("123.".matches(regex));
			assertFalse("123,25,33".matches(regex));
			assertFalse(".321".matches(regex));
			assertFalse("123,".matches(regex));
		}
	}

	public static void main(String[] args) {
		System.out.println(JCoException.JCO_ERROR_COMMUNICATION);
	}

	@Test
	public void test() {
		String s = "[ (martin:zaragoza),  (mateo:zaragoza)   ]";

		match2(s);
		match2("[(martin:zaragoza)]");
		match2("[(martin:zaragoza),(mateo:zaragoza)]");
		match2("[(fecha:2017-08-08),(mateo:zaragoza)]");
		match2("[(fecha:2017/08/08),(mateo:zaragoza)]");

		System.out.println("( split count: " + "[(martin:zaragoza),(mateo:zaragoza)]".split("\\(").length);
		System.out.println(", split count: " + "[(martin:zaragoza),(mateo:zaragoza)]".split("\\,").length);
	}

	public void match(String s) {
		System.out.println();
		s = s.replaceAll("\\,\\(", "\\, \\(");

		// String regex = "\\[\\s*\\([\\w\\-/@]+:[\\w\\-/@]+\\)(\\s*,\\s*\\([\\w\\-/@]+:[\\w\\-/@]+\\))*\\s*\\]";
		String regex = "\\[\\s*(\\(\\w+:[\\w-/]+\\)\\s*,?\\s*)+\\]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);

		assertTrue(matcher.find());
		assertTrue(s.matches(regex));

		for (int i = 0; i <= matcher.groupCount(); i++) {
			Optional<String> group = Optional.fromNullable(matcher.group(i));
			System.out.println(String.format("Group %d= %s", i, group.or("null")));
			if (group.isPresent()) {
				String cleanGroup = group.get().replaceAll("\\[", "")
						.replaceAll("\\]", "")
						.replaceAll("\\,", "")
						.replaceAll("\\)", "")
						.replaceAll("\\(", "").trim();
				System.out.println("cleanGroup: " + cleanGroup);

				String[] split = cleanGroup.split(":");
				System.out.println(String.format("Key: %s ; Value: %s", split[0].trim(), split[1].trim()));
			}
		}
	}

	public void match2(String s) {
		System.out.println();
		s = s.replaceAll("\\,\\(", "\\, \\(");

		String regex = "\\[\\s*(\\(\\w+:[\\w-/]+\\)\\s*,?\\s*)+\\]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);

		assertTrue(matcher.find());
		assertTrue(s.matches(regex));

		String clean = s.replaceAll("[\\[\\]\\(\\)]", "").trim();
		String[] pairs = clean.split(Pattern.quote(","));

		List<KeyValuePair<String, String>> keyValuePairs = new ArrayList<>();
		for (String pair : pairs) {
			String[] split = pair.split(Pattern.quote(":"));
			keyValuePairs.add(new KeyValuePair<String, String>(split[0].trim(), split[1].trim()));
		}

		System.out.println(keyValuePairs);
	}

}
