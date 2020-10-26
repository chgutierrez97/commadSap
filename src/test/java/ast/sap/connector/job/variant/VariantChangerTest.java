package ast.sap.connector.job.variant;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Optional;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.InTableParam;
import ast.sap.connector.func.InTableRow;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;

public class VariantChangerTest {
	String programName = "ZTEST_3_CAMPOS";
	String variantName = "PRUEBA_VARIANT";

	private OutTableRow mockVarinfoTableRow(String report, String variant, String pname, String pkind, int polen, String ptext, String psign, String poption,
			String plow, String phigh) {
		OutTableRow row = mock(OutTableRow.class);

		when(row.getValue("REPORT")).thenReturn(report);
		when(row.getValue("VARIANT")).thenReturn(variant);
		when(row.getValue("PNAME")).thenReturn(pname);
		when(row.getValue("PKIND")).thenReturn(pkind);
		when(row.getValue("POLEN")).thenReturn(polen);
		when(row.getValue("PTEXT")).thenReturn(ptext);
		when(row.getValue("PSIGN")).thenReturn(psign);
		when(row.getValue("POPTION")).thenReturn(poption);
		when(row.getValue("PLOW")).thenReturn(plow);
		when(row.getValue("PHIGH")).thenReturn(phigh);

		return row;
	}

	private SapFunction mockVarinfoFunction() {
		OutTableRow varinfoTableRow1 = mockVarinfoTableRow(programName, variantName, "P_FECHA", "P", 10, "fecha", "I", "EQ", "21.03.1991", "");
		OutTableRow varinfoTableRow2 = mockVarinfoTableRow(programName, variantName, "P_NUMER", "P", 10, "número", "I", "EQ", "1.325", "");
		OutTableRow varinfoTableRow3 = mockVarinfoTableRow(programName, variantName, "P_TEXTO", "P", 0, "texto", "I", "EQ", "I AM YOUR FATHER", "");

		OutTableParam varinfoTable = mock(OutTableParam.class);
		when(varinfoTable.getRowCount()).thenReturn(3);
		when(varinfoTable.nextRow()).thenReturn(varinfoTableRow1, varinfoTableRow2, varinfoTableRow3);
		when(varinfoTable.currentRow()).thenReturn(varinfoTableRow1, varinfoTableRow2, varinfoTableRow3);
		when(varinfoTable.isEmpty()).thenReturn(false);

		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");
		when(ret.getValue("MESSAGE")).thenReturn("");

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);
		when(result.getOutTableParameter("VARIANT_INFO")).thenReturn(varinfoTable);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.execute()).thenReturn(result);
		return function;
	}

	private SapFunction mockVariantChangeFunction() {
		InTableRow row = mock(InTableRow.class);
		when(row.setValue(anyString(), any())).thenReturn(row);

		InTableParam table = mock(InTableParam.class);
		when(table.appendRow()).thenReturn(row);

		SapStruct ret = mock(SapStruct.class);
		when(ret.getValue("TYPE")).thenReturn("");
		when(ret.getValue("MESSAGE")).thenReturn("");

		SapFunctionResult result = mock(SapFunctionResult.class);
		when(result.getStructure("RETURN")).thenReturn(ret);

		SapFunction function = mock(SapFunction.class);
		when(function.setInParameter(anyString(), any())).thenReturn(function);
		when(function.setInTableParameter("VARIANT_INFO")).thenReturn(table);
		when(function.execute()).thenReturn(result);

		return function;
	}

	@Test
	public void testChangeVariant() {
		SapFunction varinfoFunction = mockVarinfoFunction();
		SapFunction variantChangeFunction = mockVariantChangeFunction();

		SapRepository repository = mock(SapRepository.class);
		when(repository.getFunction("BAPI_XBP_VARINFO")).thenReturn(varinfoFunction);
		when(repository.getFunction("BAPI_XBP_VARIANT_CHANGE")).thenReturn(variantChangeFunction);

		/*
		 * FIN DE MOCKS -------------------------------------------------------------------------------------------------------------------------------------------
		 */

		VariantChanger variantChanger = new VariantChanger(repository);
		Collection<VariantKeyValuePair> variantValuePairs = Arrays.asList(new VariantKeyValuePair[] {
				new VariantKeyValuePair("P_FECHA", "05/12/1962"),
				new VariantKeyValuePair("P_NUMER", "35657201"),
				new VariantKeyValuePair("P_TEXTO", "PICKLE RIIIIICK"),
		});
		ChangeVariantData changeVariantData = VariantData.newChangeVariantData(programName, variantName, "mzaragoz", variantValuePairs);
		SapBapiret2 ret = variantChanger.changeVariant(changeVariantData);
		
		assertFalse(ret.hasError());
	}

}
