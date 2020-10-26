package ast.sap.connector.chain.processes;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;

public class ProcessEntryTest {

	@Test
	public void testHasError() {
		OutTableParam outTable = mockProcessEntries();
		ProcessEntry process = new ProcessEntry(outTable.currentRow());
		ProcessEntry process2 = new ProcessEntry(outTable.currentRow());
		ProcessEntry process3 = new ProcessEntry(outTable.currentRow());
		
		assertFalse(process.hasError());
		assertTrue(process2.hasError());
		assertFalse(process3.hasError());
	}

	private OutTableParam mockProcessEntries() {
		OutTableRow outTableRow = mock(OutTableRow.class);
		when(outTableRow.getValue("CHAIN_ID")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENT_START")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENTP_START")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENTNO_START")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("BACKLINK_START")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("TYPE")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("VARIANTE")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("PREDECESSOR")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("INSTANCE")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("STATE")).thenReturn("F", "A", " ");
		when(outTableRow.getValue("EVENT_END")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENTP_END")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("BACKLINK_END")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("ACTUAL_STATE")).thenReturn("F", "R", " ");
		when(outTableRow.getValue("EVENT_GREEN")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENTP_GREEN")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("BACKLINK_GREEN")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENT_RED")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("EVENTP_RED")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("BACKLINK_RED")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("GREEN_EQ_RED")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("WAIT")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("STARTTIMESTAMP")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("ENDTIMESTAMP")).thenReturn("1", "1", "1");
		when(outTableRow.getValue("JOB_COUNT")).thenReturn("1", "1", "1");

		OutTableParam outTable = mock(OutTableParam.class);
		when(outTable.currentRow()).thenReturn(outTableRow, outTableRow, outTableRow);
		
		return outTable;
	}

}
