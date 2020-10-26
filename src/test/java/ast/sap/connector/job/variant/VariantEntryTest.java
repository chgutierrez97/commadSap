package ast.sap.connector.job.variant;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import ast.sap.connector.config.Configuration;
import ast.sap.connector.func.OutTableRow;

public class VariantEntryTest {
	@BeforeClass
	public static void beforeClass() {
		Configuration.loadConnectorConfig("connectorsap_test.properties");
	}
	
	@Test
	public void testSetPlow() {
		{
			OutTableRow outTableRow = mock(OutTableRow.class);
			when(outTableRow.getValue("PLOW")).thenReturn("123.321");
			VariantEntry variantEntry = new VariantEntry(outTableRow);
			assertEquals(VariantFieldType.NUMBER, variantEntry.getFieldType());	
			assertEquals("123321", variantEntry.getPlow());
		}
		
		{
			OutTableRow outTableRow = mock(OutTableRow.class);
			when(outTableRow.getValue("PLOW")).thenReturn("123.321,25");
			VariantEntry variantEntry = new VariantEntry(outTableRow);
			assertEquals(VariantFieldType.NUMBER, variantEntry.getFieldType());	
			assertEquals("123321.25", variantEntry.getPlow());
		}
		
		{
			OutTableRow outTableRow = mock(OutTableRow.class);
			when(outTableRow.getValue("PLOW")).thenReturn("123");
			VariantEntry variantEntry = new VariantEntry(outTableRow);
			assertEquals(VariantFieldType.NUMBER, variantEntry.getFieldType());			
		}
		
		{
			OutTableRow outTableRow = mock(OutTableRow.class);
			when(outTableRow.getValue("PLOW")).thenReturn("21.03.1991");
			VariantEntry variantEntry = new VariantEntry(outTableRow);
			assertEquals(VariantFieldType.DATE, variantEntry.getFieldType());			
		}
		
		{
			OutTableRow outTableRow = mock(OutTableRow.class);
			when(outTableRow.getValue("PLOW")).thenReturn("Pickle riiiick");
			VariantEntry variantEntry = new VariantEntry(outTableRow);
			assertEquals(VariantFieldType.TEXT, variantEntry.getFieldType());			
		}
	}

}
