package ast.sap.connector.job.mod;

import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.func.SapBapiret2;
import ast.sap.connector.func.SapFunction;
import ast.sap.connector.func.SapFunctionResult;
import ast.sap.connector.job.JobRunData;

@Deprecated
public class JobHeaderModifier {

	private SapRepository sapRepository;

	public JobHeaderModifier(SapRepository sapRepository) {
		super();
		this.sapRepository = sapRepository;
	}

	public SapBapiret2 modifyHeader(JobRunData jobData) {
		SapFunction function = sapRepository.getFunction("BAPI_XBP_JOB_HEADER_MODIFY")
				.setInParameter("JOBCOUNT", jobData.getJobId())
				.setInParameter("JOBNAME", jobData.getJobName())
				.setInParameter("EXTERNAL_USER_NAME", jobData.getExternalUsername());
		// .setInParameter("DONT_RELEASE", release)
		function.setInTableParameter("JOB_HEADER") // TODO: agregar la estructura BP20HEAD
				.appendRow()
				.setValue("SDLSTRTDT", "20170811");
		// function.setInTableParameter("MASK").appendRow().setValue("STARTCOND",""); //TODO: definir la estructura BP20HMSK
		SapFunctionResult execute = function.execute();
		return new SapBapiret2(execute.getStructure("RETURN"));
	}

}
