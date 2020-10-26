package ast.sap.connector.chain.processes;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;

public class ChainProcessesReaderTest {

	@Test
	public void testReadProcesses() {
		OutTableRow row = mock(OutTableRow.class);
		String type1 = "TRIGGER";
		String count1 = "123456";
		String type2 = "ABAP";
		String count2 = "789123";
		when(row.getValue("TYPE")).thenReturn(type1, type2);
		when(row.getValue("JOB_COUNT")).thenReturn(count1, count2);

		OutTableParam table = mock(OutTableParam.class);
		when(table.currentRow()).thenReturn(row, row);
		when(table.getRowCount()).thenReturn(2);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutTableParameter("E_T_PROCESSLIST")).thenReturn(table);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_PROCESSES")).thenReturn(function);

		/* FIN DE MOCKS -------------------------------------------------------------------------- */

		ChainProcessesReader chainProcessesReader = new ChainProcessesReader(sapRepository);
		String logId = "654231";
		String chain = "SOME_CHAIN";
		ChainData chainData = new ChainData(logId, chain);
		ChainProcessBundle chainProcessBundle = chainProcessesReader.readProcesses(chainData);

		List<ProcessEntry> processes = chainProcessBundle.getProcesses();
		assertEquals(2, processes.size());

		ProcessEntry process1 = processes.get(0);
		assertEquals(type1, process1.getType());
		assertEquals(count1, process1.getJobCount());

		ProcessEntry process2 = processes.get(1);
		assertEquals(type2, process2.getType());
		assertEquals(count2, process2.getJobCount());
	}

	@Test
	public void testReadNestedProcesses() {
		OutTableRow row = mock(OutTableRow.class);
		when(row.getValue("TYPE")).thenReturn("TRIGGER", "CHAIN", "ABAP", "TRIGGER", "ABAP");
		when(row.getValue("JOB_COUNT")).thenReturn("17300800", "17302100", "17302300", "17302800", "17302900");
		when(row.getValue("VARIANTE")).thenReturn("Z_CHAIN_IN_CHAIN_EVENT_VAR", "Z_CHAIN_S_EVENT", "Z_ABAP_PROGRAM", "Z_CHAIN_S_EVENT_VAR",
				"Z_CHAIN_S_EVENT_PROG");
		when(row.getValue("INSTANCE")).thenReturn("5BP5BLTT9TF700P486581X111", "5BOUSCOS8CKN78FYZ3E3QH222", "5BOUSCOS8FFFFFFFFFFFFF333",
				"5BD22E0FNMOCACDQHXNUDZ444",
				"5BD22GCXG7B4Z8BOA4DLGL555");

		OutTableParam table = mock(OutTableParam.class);
		when(table.currentRow()).thenReturn(row, row, row, row, row);
		when(table.getRowCount()).thenReturn(3, 2);

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getOutTableParameter("E_T_PROCESSLIST")).thenReturn(table, table);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);

		SapRepository sapRepository = mock(SapRepository.class);
		when(sapRepository.getFunction("RSPC_API_CHAIN_GET_PROCESSES")).thenReturn(function);

		/* FIN DE MOCKS -------------------------------------------------------------------------- */

		ChainProcessesReader chainProcessesReader = new ChainProcessesReader(sapRepository);
		String logId = "654231";
		String chain = "SOME_CHAIN";
		ChainData chainData = new ChainData(logId, chain);
		ChainProcessBundle chainProcessBundle = chainProcessesReader.readProcesses(chainData);

		List<ProcessEntry> processes = chainProcessBundle.getProcesses();
		assertEquals(5, processes.size());

		ProcessEntry process1 = processes.get(0);
		assertEquals("TRIGGER", process1.getType());
		assertEquals("17300800", process1.getJobCount());
		assertEquals("Z_CHAIN_IN_CHAIN_EVENT_VAR", process1.getVariant());
		assertEquals("5BP5BLTT9TF700P486581X111", process1.getInstance());

		ProcessEntry process2 = processes.get(1);
		assertEquals("CHAIN", process2.getType());
		assertEquals("17302100", process2.getJobCount());
		assertEquals("Z_CHAIN_S_EVENT", process2.getVariant());
		assertEquals("5BOUSCOS8CKN78FYZ3E3QH222", process2.getInstance());

		ProcessEntry process3 = processes.get(2);
		assertEquals("ABAP", process3.getType());
		assertEquals("17302300", process3.getJobCount());
		assertEquals("Z_ABAP_PROGRAM", process3.getVariant());
		assertEquals("5BOUSCOS8FFFFFFFFFFFFF333", process3.getInstance());

		ProcessEntry process4 = processes.get(3);
		assertEquals("TRIGGER", process4.getType());
		assertEquals("17302800", process4.getJobCount());
		assertEquals("Z_CHAIN_S_EVENT_VAR", process4.getVariant());
		assertEquals("5BD22E0FNMOCACDQHXNUDZ444", process4.getInstance());

		ProcessEntry process5 = processes.get(4);
		assertEquals("ABAP", process5.getType());
		assertEquals("17302900", process5.getJobCount());
		assertEquals("Z_CHAIN_S_EVENT_PROG", process5.getVariant());
		assertEquals("5BD22GCXG7B4Z8BOA4DLGL555", process5.getInstance());
	}

}
