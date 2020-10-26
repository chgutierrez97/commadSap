package ast.sap.connector.job.evt;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;

public class EventRaiserTest {

	@Test
	public void testRaiseEvent() {
		String eventId = "TEST_BASIS";
		String externalUsername = "mzaragoz";

		/* INICIO DE MOCK -------------------------------------------------------------------- */

		SapStruct retMock = mock(SapStruct.class);
		when(retMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getStructure("RETURN")).thenReturn(retMock);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.execute()).thenReturn(resultMock);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XBP_EVENT_RAISE")).thenReturn(functionMock);

		/* FIN DE MOCK -------------------------------------------------------------------- */

		EventRaiser eventRaiser = new EventRaiser(repoMock);
		SapBapiret2 ret = eventRaiser.raiseEvent(externalUsername, eventId);
		assertFalse(ret.hasError());

		verify(repoMock, times(1)).getFunction("BAPI_XBP_EVENT_RAISE");
		verify(functionMock, times(1)).execute();
		verify(functionMock, times(1)).setInParameter("EVENTID", eventId);
		verify(functionMock, times(1)).setInParameter("EXTERNAL_USER_NAME", externalUsername);
		verify(resultMock, times(1)).getStructure("RETURN");
	}

}
