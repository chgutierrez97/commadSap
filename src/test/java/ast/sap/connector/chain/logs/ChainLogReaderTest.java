package ast.sap.connector.chain.logs;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;

public class ChainLogReaderTest {

	private static final String CHAIN = "Z_TEST_WAIT2";

	private String tt(int i) {
		return "Texto de prueba " + i;
	}

	private String randStringNo() {
		return "" + ((int) (Math.random() * 1000));
	}

	private SapFunction mockGetChainLogFunction() throws ParseException {

		OutTableRow outRow = mock(OutTableRow.class);
		when(outRow.getValue("MSGID")).thenReturn(tt(1), tt(2), tt(3));
		when(outRow.getValue("MSGNO")).thenReturn(randStringNo(), randStringNo(), randStringNo());
		when(outRow.getValue("MSGTY")).thenReturn("000", "000", "000");
		when(outRow.getValue("MSGV1")).thenReturn("000", "000", "000");
		when(outRow.getValue("MSGV2")).thenReturn("000", "000", "000");
		when(outRow.getValue("MSGV3")).thenReturn("000", "000", "000");
		when(outRow.getValue("MSGV4")).thenReturn("000", "000", "000");

		OutTableParam outTable = mock(OutTableParam.class);
		when(outTable.currentRow()).thenReturn(outRow);
		when(outTable.getRowCount()).thenReturn(2);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutTableParameter("E_T_LOG")).thenReturn(outTable);
		when(result.getOutTableParameter("E_T_LOG_DETAILS")).thenReturn(outTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		return function;
	}

	@Test
	public void testChainReadLogMocked() throws ParseException {
		SapFunction getChainLogFunction = mockGetChainLogFunction();

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_LOG")).thenReturn(getChainLogFunction);

		ChainLogReader chainlogReader = new ChainLogReader(sapRepository);
		ChainLogReaderInput chainLogReaderInput = new ChainLogReaderInput(CHAIN, "00000000000");
		ChainLog chainLog = chainlogReader.readChainLog(chainLogReaderInput);
		
		System.out.println(chainLog);

		List<ChainLogEntry> logEntries = chainLog.getEntries();
		assertEquals(2, logEntries.size());
	}
	

}
