package ast.sap.connector.chain;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.sap.conn.jco.AbapException;

import ast.sap.connector.chain.start.ChainStarter;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.RspcExecuteException;

/**
 * 
 * @author franco.milanese
 *
 */
public class ChainStarterTest {

	@Test
	public void testStartChain() {
		String logId = "1234567890";

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getOutParameterValue("E_LOGID")).thenReturn(logId);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenReturn(resultMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("RSPC_API_CHAIN_START")).thenReturn(functionMock);

		ChainStarter chainStarter = new ChainStarter(repoMock);
		String chainId = "SOME_CHAIN";

		/* FIN MOCKS ----------------------------------------------------------------------- */

		ChainData chainData = chainStarter.startChain(chainId);

		assertEquals(logId, chainData.getLogId());
		assertEquals(chainId, chainData.getChain());

		verify(functionMock, times(1)).execute();
		verify(functionMock, times(1)).setInParameter("I_CHAIN", chainId);
	}
	
	
	@Test (expected = RspcExecuteException.class)
	public void testStartChainWithException() {

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenThrow(new RspcExecuteException("mensaje de prueba", new AbapException("FAILED", "pruebas")));

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("RSPC_API_CHAIN_START")).thenReturn(functionMock);

		ChainStarter chainStarter = new ChainStarter(repoMock);
		String chainId = "SOME_CHAIN";
		/* FIN MOCKS ----------------------------------------------------------------------- */

		chainStarter.startChain(chainId);

	}

}
