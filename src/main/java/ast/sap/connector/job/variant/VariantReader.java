package ast.sap.connector.job.variant;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.exception.FunctionGetFailException;
import ast.sap.connector.dst.exception.FunctionNetworkErrorException;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.func.exception.FunctionExecuteException;

/**
 * Puede obtener informacion de la variante asociada a un programa / reporte.
 * 
 * @author mzaragoz
 *
 */
public class VariantReader {
	private SapRepository repository;

	public VariantReader(SapRepository repository) {
		this.repository = repository;
	}

	/**
	 * Obtiene informacion de la variante asociada a un programa / reporte.
	 * 
	 * @param variantData
	 *            Informacion de la variante a leer.
	 * @return Informacion de la variante.
	 * @throws FunctionGetFailException
	 *             En caso que ocurra un error al obtener las funciones de sap.
	 * @throws FunctionExecuteException
	 *             En caso que ocurra un error al ejecutar las funciones de sap.
	 * @throws FunctionNetworkErrorException
	 *             Si ocurrio un error en la red al ejecutar la funcion.
	 */
	public Varinfo readVariant(ReadVariantData variantData) throws FunctionGetFailException, FunctionExecuteException, FunctionNetworkErrorException{
		Object programName = variantData.getProgram();
		Object externalUsername = variantData.getExternalUsername();

		/* OBTENGO LA VARIANTE DEL PROGRAMA RECIBIDO */
		SapFunction function = repository.getFunction("BAPI_XBP_VARINFO")
				.setInParameter("ABAP_PROGRAM_NAME", programName)
				.setInParameter("EXTERNAL_USER_NAME", externalUsername);

		if (variantData.getVariant().isPresent()) {
			function.setInParameter("VARIANT", variantData.getVariant().get());
		}

		SapFunctionResult result = function.execute();
		SapBapiret2 bapiret2 = new SapBapiret2(result.getStructure("RETURN"));
		OutTableParam variantTable = result.getOutTableParameter("VARIANT_INFO");
		if (bapiret2.hasError() || variantTable.isEmpty()) {
			// Retornar SapBapiRet2 en caso de error o en caso de que no tenga variante dado que no se modificaria nada
			return new Varinfo(bapiret2);
		}
		Variant variant = new Variant(variantTable);
		return new Varinfo(bapiret2, variant);
	}
}
