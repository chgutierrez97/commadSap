package ast.sap.connector.chain.monitor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;

import ast.sap.connector.chain.processes.ProcessEntry;
import ast.sap.connector.chain.processes.ProcessLogPair;
import ast.sap.connector.chain.status.ChainStatus;
import ast.sap.connector.chain.status.ChainStatusCode;

public class ChainFullStatusTest {

	@Test
	public void testGetProcessesWithErrors() {
		ChainStatus chainStatus = new ChainStatus(ChainStatusCode.R, "", "");
		List<ProcessLogPair> processLogPairs = mockProcessLogPairs();
		ChainFullStatus chainFullStatus = new ChainFullStatus(chainStatus, processLogPairs);
		assertEquals(4, chainFullStatus.getProcessLogPairs().size());
		assertEquals(1, chainFullStatus.getProcessesWithErrors().size());
	}


	private List<ProcessLogPair> mockProcessLogPairs() {
		List<ProcessLogPair> processLogPairs = new ArrayList<ProcessLogPair>();
		ProcessLogPair processLog = mock(ProcessLogPair.class);
		when(processLog.getProcessEntry()).thenReturn(mock(ProcessEntry.class));
		when(processLog.getProcessEntry().hasError()).thenReturn(false,false,true,false);
		processLogPairs.addAll(Arrays.asList(processLog,processLog,processLog,processLog));
		return processLogPairs;
	}
}
