package ast.sap.connector.util;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

public class NumberParserTest {

	@Test
	public void testParse() throws ParseException {
		{
			NumberParser numberParser = NumberParser.SPANISH;
			assertEquals(123456.25, numberParser.parse("123.456,25"));
		}

		{
			NumberParser numberParser = NumberParser.JAVA;
			assertEquals(123456.25, numberParser.parse("123456.25"));
		}

		{
			NumberParser numberParser = NumberParser.US;
			assertEquals(123456.25, numberParser.parse("123,456.25"));
		}

		{
			NumberParser numberParser = NumberParser.SPANISH;
			assertEquals("123456", numberParser.parse("123.456").toString());
		}

		{
			NumberParser numberParser = NumberParser.JAVA;
			Number parsed = numberParser.parse("123456");
			assertEquals("123456", parsed.toString());
		}

		{
			NumberParser numberParser = NumberParser.US;
			assertEquals("123456", numberParser.parse("123,456").toString());
		}
		
		
		
		{
			NumberParser numberParser = NumberParser.SPANISH;
			assertEquals(-123456.25, numberParser.parse("-123.456,25"));
		}

		{
			NumberParser numberParser = NumberParser.JAVA;
			assertEquals(-123456.25, numberParser.parse("-123456.25"));
		}

		{
			NumberParser numberParser = NumberParser.US;
			assertEquals(-123456.25, numberParser.parse("-123,456.25"));
		}

		{
			NumberParser numberParser = NumberParser.SPANISH;
			assertEquals("-123456", numberParser.parse("-123.456").toString());
		}

		{
			NumberParser numberParser = NumberParser.JAVA;
			Number parsed = numberParser.parse("-123456");
			assertEquals("-123456", parsed.toString());
		}

		{
			NumberParser numberParser = NumberParser.US;
			assertEquals("-123456", numberParser.parse("-123,456").toString());
		}
	}

	@Test(expected = ParseException.class)
	public void testFailParse() throws ParseException {
		NumberParser numberParser = NumberParser.SPANISH;
		numberParser.parse("123.456,25,26");
	}
}
