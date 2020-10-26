package ast.sap.connector.cmd.impl;

import java.util.ArrayList;
import java.util.List;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.variant.VariantData;
import ast.sap.connector.job.variant.VariantChanger;
import ast.sap.connector.job.variant.VariantKeyValuePair;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * @author franco.milanese
 *
 * Comando para la modificacione de una variante de un programa
 */
public class ChangeVariantCommand extends SapXmiCommand {

	private JobRunData jobData;
	private String programName;

	public ChangeVariantCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData, String programName) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
		this.programName = programName;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();

		VariantChanger variantChanger = new VariantChanger(repository);
		/* LE PASO UN VALOR DE PRUEBA PARA QUE MODIFIQUE LA VARIANTE. ESTOS VALORES LE DEBERIAN LLEGAR AL COMANDO DESDE EL CommandFactory */
		List<VariantKeyValuePair> variantValuePairs = new ArrayList<VariantKeyValuePair>();
//		variantValuePairs.add(new VariantKeyValuePair("P_FECHA", "20170321"));
//		variantValuePairs.add(new KeyValuePair<String, Object>("P_NUMER", "34567"));
		VariantData changeVariantData = new VariantData(programName, jobData.getExternalUsername(),	variantValuePairs);
		SapBapiret2 bapiret2 = variantChanger.changeVariant(changeVariantData);

		return new SapCommandResult(bapiret2);
	}

}
