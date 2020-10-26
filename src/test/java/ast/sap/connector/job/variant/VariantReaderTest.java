package ast.sap.connector.job.variant;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Optional;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.SapStruct;

public class VariantReaderTest {

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

	@Test
	public void testReadVariant() {
		String programName = "ZTEST_3_CAMPOS";
		String variantName = "PRUEBA_VARIANT";

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

		SapRepository repository = mock(SapRepository.class);
		when(repository.getFunction("BAPI_XBP_VARINFO")).thenReturn(function);

		/*
		 * FIN DE MOCKS -------------------------------------------------------------------------------------------------------------------------------------------
		 */

		VariantReader variantReader = new VariantReader(repository);
		ReadVariantData variantData = VariantData.newReadVariantData(programName, variantName, "mzaragoz");
		Varinfo varinfo = variantReader.readVariant(variantData);
		SapBapiret2 varRet = varinfo.getRet();
		Optional<Variant> variant = varinfo.getVariant();

		assertFalse(varRet.hasError());
		assertTrue(variant.isPresent());

		List<VariantEntry> variantEntries = variant.get().getVariantEntries();
		assertEquals(3, variantEntries.size());

		assertEquals(VariantFieldType.DATE, variantEntries.get(0).getFieldType());
		assertEquals(VariantFieldType.NUMBER, variantEntries.get(1).getFieldType());
		assertEquals(VariantFieldType.TEXT, variantEntries.get(2).getFieldType());
	}

}
