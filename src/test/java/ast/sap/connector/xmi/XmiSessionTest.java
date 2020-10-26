package ast.sap.connector.xmi;

import static org.junit.Assert.assertEquals;
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
import ast.sap.connector.func.exception.FunctionExecuteException;
import ast.sap.connector.xmi.exception.XmiLoginException;

public class XmiSessionTest {
	@Test
	public void testLogin() {
		String sessionId = "123456";
		XmiLoginData loginData = new XmiLoginData();

		SapFunctionResult resultMock = mock(SapFunctionResult.class);
		when(resultMock.getOutParameterValue("SESSIONID")).thenReturn(sessionId);

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenReturn(resultMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XMI_LOGON")).thenReturn(functionMock);

		XmiSession xmiSession = new XmiSession(repoMock, loginData);

		assertEquals(sessionId, xmiSession.getSessionData().getSessionId());
		assertEquals(loginData.getXmiInterface(), xmiSession.getSessionData().getXmiInterface());

		verify(resultMock, times(1)).getOutParameterValue("SESSIONID");
		verify(functionMock, times(1)).setInParameter("EXTCOMPANY", loginData.getCompany());
		verify(functionMock, times(1)).setInParameter("EXTPRODUCT", loginData.getProduct());
		verify(functionMock, times(1)).setInParameter("INTERFACE", loginData.getXmiInterface());
		verify(functionMock, times(1)).setInParameter("VERSION", loginData.getVersion());
	}
	
	@Test (expected = XmiLoginException.class)
	public void testLoginWithError() {
		XmiLoginData loginData = new XmiLoginData();

		SapFunction functionMock = mock(SapFunction.class);
		when(functionMock.setInParameter(anyString(), any())).thenReturn(functionMock);
		when(functionMock.execute()).thenThrow(new FunctionExecuteException("XmiLoginException Exception"));

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XMI_LOGON")).thenReturn(functionMock);

		new XmiSession(repoMock, loginData);
		
	}

	@Test
	public void testLogout() {
		String sessionId = "123456";
		XmiLoginData loginData = new XmiLoginData();

		/* INICIO MOCK ------------------------------------------------------------------------------- */

		SapStruct logonRetMock = mock(SapStruct.class);
		when(logonRetMock.getValue("TYPE")).thenReturn("");

		SapFunctionResult logonResMock = mock(SapFunctionResult.class);
		when(logonResMock.getOutParameterValue("SESSIONID")).thenReturn(sessionId);
		when(logonResMock.getStructure("RETURN")).thenReturn(logonRetMock);

		SapFunction logonMock = mock(SapFunction.class);
		when(logonMock.setInParameter(anyString(), any())).thenReturn(logonMock);
		when(logonMock.execute()).thenReturn(logonResMock);

		SapStruct logoffRet = mock(SapStruct.class);
		when(logoffRet.getValue("TYPE")).thenReturn("");

		SapFunctionResult logoffResMock = mock(SapFunctionResult.class);
		when(logoffResMock.getStructure("RETURN")).thenReturn(logoffRet);

		SapFunction logoffMock = mock(SapFunction.class);
		when(logoffMock.setInParameter(anyString(), any())).thenReturn(logoffMock);
		when(logoffMock.execute()).thenReturn(logoffResMock);

		SapRepository repoMock = mock(SapRepository.class);
		when(repoMock.getFunction("BAPI_XMI_LOGON")).thenReturn(logonMock);
		when(repoMock.getFunction("BAPI_XMI_LOGOFF")).thenReturn(logoffMock);

		/* FIN MOCK ------------------------------------------------------------------------------- */

		XmiSession xmiSession = new XmiSession(repoMock, loginData);
		SapBapiret2 logoutRet = xmiSession.logout();
		assertFalse(logoutRet.hasError());

		assertEquals(sessionId, xmiSession.getSessionData().getSessionId());
		assertEquals(loginData.getXmiInterface(), xmiSession.getSessionData().getXmiInterface());

		verify(logoffMock, times(1)).setInParameter("INTERFACE", loginData.getXmiInterface());
	}
}
