package ast.sap.connector.chain.status;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;

public class ChainStatusReaderTest {

	@Test
	public void testReadChainStatus() {
		String logId = "1234567890";
		String chainStatusStrCode = "G";
		String chainId = "SOME_CHAIN";

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getOutParameterValue("E_STATUS")).thenReturn(chainStatusStrCode);
		when(resultMock.getOutParameterValue("E_MANUAL_ABORT")).thenReturn("");
		when(resultMock.getOutParameterValue("E_MESSAGE")).thenReturn("");

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenReturn(resultMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("RSPC_API_CHAIN_GET_STATUS")).thenReturn(functionMock);

		/* FIN DE MOCK ----------------------------------------------------------------------------------------- */

		ChainStatusReader chainStatusReader = new ChainStatusReader(repoMock);
		ChainStatus chainStatus = chainStatusReader.readChainStatus(new ChainStatusReaderInput(chainId, logId));
		assertEquals(ChainStatusCode.G, chainStatus.getStatus());

		verify(resultMock, times(1)).getOutParameterValue("E_STATUS");
		verify(resultMock, times(1)).getOutParameterValue("E_MANUAL_ABORT");
		verify(resultMock, times(1)).getOutParameterValue("E_MESSAGE");

		verify(functionMock, times(0)).setInParameter(eq("I_DONT_UPDATE"), any());
		verify(functionMock, times(0)).setInParameter(eq("I_DONT_POLL"), any());
	}

}
