package ast.sap.connector.cmd.impl;

import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.SapXmiCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.OutTableParam;
import ast.sap.connector.func.OutTableRow;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Lee el spool de un job. Se debe considerar que se intenta leer el spool del primer step del job unicamente.
 * 
 * @author mzaragoz
 *
 */
public class ReadSpoolCommand extends SapXmiCommand {

	private JobRunData jobData;

	public ReadSpoolCommand(SapRepository sapRepository, XmiLoginData xmiLoginData, JobRunData jobData) {
		super(sapRepository, xmiLoginData);
		this.jobData = jobData;
	}

	@Override
	protected SapCommandResult perform() {
		SapRepository repository = repository();
		SapFunction function = repository.getFunction("BAPI_XBP_JOB_SPOOLLIST_READ_20")
				.setInParameter("JOBNAME", jobData.getJobName())
				.setInParameter("JOBCOUNT", jobData.getJobId())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername())
				.setInParameter("STEP_NUMBER", 1);
		
	
		SapFunctionResult result = function.execute();
		SapBapiret2 sapBapiret2 = new SapBapiret2(result.getStructure("RETURN"));
		OutTableParam spoolList = result.getOutTableParameter("SPOOL_LIST");
		// OutTableParam spoolListPlain =
		// result.getOutTableParameter("SPOOL_LIST_PLAIN");

		for (int i = 0; i < spoolList.getRowCount(); i++) {
			OutTableRow row = spoolList.currentRow();
			System.out.println(row.getValue("LINE"));
			spoolList.nextRow();
		}

		return new SapCommandResult(sapBapiret2);
	}

}
