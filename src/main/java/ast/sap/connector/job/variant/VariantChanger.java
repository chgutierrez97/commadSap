package ast.sap.connector.job.variant;

import java.text.ParseException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.InTableParam;
import ast.sap.connector.func.InTableRow;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;

import com.google.common.base.Optional;

public class VariantChanger {
	private static final Logger LOGGER = LoggerFactory.getLogger(VariantChanger.class);

	private final SapRepository repository;

	public VariantChanger(SapRepository repository) {
		this.repository = repository;
	}

	/**
	 * Modifica los campos de una variante.
	 * 
	 * @param changeVariantData
	 *            - Informacion de la variante y campos a modificar.
	 * @return Resultado del cambio de variante.
	 * @throws VariantFieldChangeException
	 *             Si ocurrio un error al modificar alguno de los campos.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public SapBapiret2 changeVariant(ChangeVariantData changeVariantData)
			throws VariantFieldChangeException, FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException {
		Object programName = changeVariantData.getProgram();
		Object externalUsername = changeVariantData.getExternalUsername();


		VariantReader variantReader = new VariantReader(repository);
		Varinfo varinfo = variantReader.readVariant(changeVariantData);
		SapBapiret2 bapiret2 = varinfo.getRet();
		if (bapiret2.hasError() || !varinfo.getVariant().isPresent()) {
			// Retornar SapBapiRet2 en caso de error o en caso de que no tenga variante dado que no se modificaria nada
			return bapiret2;
		}

		Variant variant = varinfo.getVariant().get();

		LOGGER.debug(variant.toString());
		// TODO: MODIFICAR LA TABLA RECIBIDA (variantData) DE LA FUNCION ANTERIOR Y ACTUALIZANDO LOS CAMPOS NECESARIOS (HAY QUE VER COMO VIENEN ESOS CAMPOS Y
		// CUALES SON)
		// for (VariantInfo variant : variantData.getVariantInfoList()) {
		// //SETEAR EL/LOS VALOR/ES A MODIFICAR
		// }
		// TODO: MODIFICO LA VARIANTE DEL PROGRAMA RECIBIDO
		String variantName = variant.getVariantEntries().get(0).getVariant();
		SapFunction function = repository.getFunction("BAPI_XBP_VARIANT_CHANGE")
				.setInParameter("ABAP_PROGRAM_NAME", programName)
				.setInParameter("ABAP_VARIANT_NAME", variantName)
				.setInParameter("EXTERNAL_USER_NAME", externalUsername);

		Collection<VariantKeyValuePair> variantValuePairs = changeVariantData.getVariantValuePairs();
		/*
		 * TODO : DEBEMOS TENER MUCHO CUIDADO CON EL SETEO DE LOS CAMPOS DE TIPO FECHA... EL FORMATO DE FECHA DE VARIANT_INFO ES dd.MM.yyyy PERO EL FORMATO DE
		 * FECHA DE BAPI_XBP_VARIANT_CHANGE ES yyyyMMdd
		 */

		InTableParam table = function.setInTableParameter("VARIANT_INFO");
		for (VariantEntry variantEntry : variant.getVariantEntries()) {
			InTableRow newRow = table.appendRow()
					.setValue("REPORT", variantEntry.getReport())
					.setValue("VARIANT", variantEntry.getVariant())
					.setValue("PNAME", variantEntry.getPname())
					.setValue("PKIND", variantEntry.getPkind())
					.setValue("POLEN", variantEntry.getPolen())
					.setValue("PTEXT", variantEntry.getPtext())
					.setValue("PSIGN", variantEntry.getPsign())
					.setValue("POPTION", variantEntry.getPoption())
					.setValue("PLOW", variantEntry.getPlow())
					.setValue("PHIGH", variantEntry.getPhigh());

			Optional<VariantKeyValuePair> matchingEntry = matchingEntry(variantEntry, variantValuePairs);
			if (matchingEntry.isPresent()) {
				String value;
				try {
					value = variantEntry.getFieldType().transform(matchingEntry.get().value);
				} catch (ParseException e) {
					throw new VariantFieldChangeException("Error al modificar el campo " + matchingEntry.get().key + ":: Formato del dato invalido", e);
				}
				newRow.setValue("PLOW", value);
			}
		}
		SapFunctionResult result2 = function.execute();
		return new SapBapiret2(result2.getStructure("RETURN"));
	}

	private Optional<VariantKeyValuePair> matchingEntry(VariantEntry variantEntry, Collection<VariantKeyValuePair> variantValuePairs) {
		for (VariantKeyValuePair variantValuePair : variantValuePairs) {
			if (variantEntry.getPname().equals(variantValuePair.key)) return Optional.fromNullable(variantValuePair);
		}
		return Optional.absent();
	}
}
