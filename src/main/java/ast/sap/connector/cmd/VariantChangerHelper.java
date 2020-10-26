package ast.sap.connector.cmd;

import java.util.Collection;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.variant.ChangeVariantData;
import ast.sap.connector.job.variant.VariantChanger;
import ast.sap.connector.job.variant.VariantData;
import ast.sap.connector.job.variant.VariantKeyValuePair;

/**
 * Facilita la modificacion de una variante.
 * 
 * @author martin.zaragoza
 *
 */
public enum VariantChangerHelper {
	INSTANCE;

	/**
	 * Modifica una variante.
	 * 
	 * @param repository
	 *            Repositorio de funciones de sap.
	 * @param externalUsername
	 *            Nombre de usuario conectado a sap.
	 * @param stepVariantTuple
	 *            Datos de la variante a modificar.
	 * @return Resultado de modificacion de variante.
	 */
	public SapBapiret2 changeVariant(SapRepository repository, String externalUsername, StepVariantValuesTuple stepVariantTuple) {
		VariantChanger variantChanger = new VariantChanger(repository);

		String program = stepVariantTuple.getProgram();
		String variant = stepVariantTuple.getVariant().get();
		Collection<VariantKeyValuePair> variantValuePairs = stepVariantTuple.getVariantValuePairs().get();
		ChangeVariantData changeVariantData = VariantData.newChangeVariantData(program, variant, externalUsername, variantValuePairs);

		SapBapiret2 changeVariantRet = variantChanger.changeVariant(changeVariantData);
		return changeVariantRet;
	}
}
