package ast.sap.connector.cmd;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Strings;

import ast.sap.connector.chain.ChainData;
import ast.sap.connector.chain.logs.ChainLogReaderInput;
import ast.sap.connector.chain.status.ChainStatusReaderInput;
import ast.sap.connector.cmd.impl.ChainScheduleCommand;
import ast.sap.connector.cmd.impl.ChangeVariantCommand;
import ast.sap.connector.cmd.impl.CopyMonitorJobCommand;
import ast.sap.connector.cmd.impl.CopyRunJobCommand;
import ast.sap.connector.cmd.impl.CreateJobCommand;
import ast.sap.connector.cmd.impl.CreateMonitorJobCommand;
import ast.sap.connector.cmd.impl.CreateRunJobCommand;
import ast.sap.connector.cmd.impl.GetChainErrorsCommand;
import ast.sap.connector.cmd.impl.GetChainLogCommand;
import ast.sap.connector.cmd.impl.GetChainProcessLogCommand;
import ast.sap.connector.cmd.impl.GetChainProcessesCommand;
import ast.sap.connector.cmd.impl.GetChainStartConditionCommand;
import ast.sap.connector.cmd.impl.GetChainStatusCommand;
import ast.sap.connector.cmd.impl.GetJobOutputCommand;
import ast.sap.connector.cmd.impl.GetProcessJobsCommand;
import ast.sap.connector.cmd.impl.GetProcessLogCommand;
import ast.sap.connector.cmd.impl.HelpCommand;
import ast.sap.connector.cmd.impl.JobCountCommand;
import ast.sap.connector.cmd.impl.MonitorChainCommand;
import ast.sap.connector.cmd.impl.MonitorJobCommand;
import ast.sap.connector.cmd.impl.RaiseEventCommand;
import ast.sap.connector.cmd.impl.ReadJobCommand;
import ast.sap.connector.cmd.impl.ReadJobDefinitionCommand;
import ast.sap.connector.cmd.impl.ReadSpoolCommand;
import ast.sap.connector.cmd.impl.RunAndStopJobCommand;
import ast.sap.connector.cmd.impl.RunJobCommand;
import ast.sap.connector.cmd.impl.SetChainStartConditionCommand;
import ast.sap.connector.cmd.impl.StartChainCommand;
import ast.sap.connector.cmd.impl.StatusJobCommand;
import ast.sap.connector.cmd.impl.StopJobCommand;
import ast.sap.connector.cmd.impl.TrackJobCommand;
import ast.sap.connector.cmd.impl.UserGetDetailCommand;
import ast.sap.connector.cmd.impl.XmiLoginCommand;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.job.JobRunData;
import ast.sap.connector.job.create.StepVariantPair;
import ast.sap.connector.job.create.StepVariantValuesTuple;
import ast.sap.connector.job.variant.VariantKeyValuePair;
import ast.sap.connector.main.args.InputArgumentsData;
import ast.sap.connector.xmi.XmiLoginData;

/**
 * Generador de comandos de sap ejecutables.
 * 
 * @author martin.zaragoza
 *
 */
public enum CommandFactory {
	INSTANCE;

	/**
	 * Obtiene un comando de sap a ejecutar a partir de los argumentos de entrada del programa y el repositorio de funciones de sap.
	 * 
	 * @param inputArguments
	 *            - Argumentos de entrada del conector sap.
	 * @param sapRepository
	 *            - Repositorio de funciones de sap inicializado.
	 * @return Comando de sap listo para ser ejecutado.
	 */
	public SapCommand getCommand(InputArgumentsData inputArguments, SapRepository sapRepository) {
		XmiLoginData xmiLoginData = inputArguments.newXmiLoginData();
		JobRunData jobData = inputArguments.newJobRunData();
		
		
		
		/* Campo programa a correr / programa cuya variante debe ser modificada */
		String program = inputArguments.getSingleStep();
		/* Campo variante a usar para correr un programa / variante a modificar antes de correr un programa */
		String variant = inputArguments.getSingleVariant();
		/* Valores de variante a establecer */
		List<VariantKeyValuePair> variantValuePairs = inputArguments.getVariantKeyValuePairs();

		AvailableCommand command = inputArguments.getCommand();

		/* Temporalmente el id y el log_id de una cadena se ingresaran mediante los parametros jobname y jobid respectivamente */
		String chainId = inputArguments.getChain();
		String chainLogId = inputArguments.getChainLogId();

		switch (command) {
			case XMI_LOGIN:
				return new XmiLoginCommand(sapRepository, xmiLoginData);
			case TRACK_JOB:
				return new TrackJobCommand(sapRepository, xmiLoginData, jobData);
			case RUN_JOB:
				return Strings.isNullOrEmpty(program) ? new RunJobCommand(sapRepository, xmiLoginData, jobData)
						: new RunJobCommand(sapRepository, xmiLoginData, jobData, new StepVariantValuesTuple(program, variant, variantValuePairs));
			case CREATE_JOB:
				StepVariantPair step = new StepVariantPair(program, variant);
				return new CreateJobCommand(sapRepository, xmiLoginData, jobData, Collections.singletonList(step));
			case USER_GET_DETAIL:
				return new UserGetDetailCommand(sapRepository, jobData.getExternalUsername());
			case STOP_JOB:
				return new StopJobCommand(sapRepository, xmiLoginData, jobData);
			case RAISE_EVENT:
				return new RaiseEventCommand(sapRepository, xmiLoginData, jobData, inputArguments.getEventId());
			case GET_JOB_OUTPUT:
				return new GetJobOutputCommand(sapRepository, xmiLoginData, jobData);
			case MONITOR_JOB:
				return Strings.isNullOrEmpty(program) ? new MonitorJobCommand(sapRepository, xmiLoginData, jobData)
						: new MonitorJobCommand(sapRepository, xmiLoginData, jobData, new StepVariantValuesTuple(program, variant, variantValuePairs));
			case STATUS_JOB:
				return Strings.isNullOrEmpty(program) ? new StatusJobCommand(sapRepository, xmiLoginData, jobData)
						: new StatusJobCommand(sapRepository, xmiLoginData, jobData, new StepVariantValuesTuple(program, variant, variantValuePairs));
			case RUN_STOP_JOB:
				return new RunAndStopJobCommand(sapRepository, xmiLoginData, jobData);
			case READ_SPOOL:
				return new ReadSpoolCommand(sapRepository, xmiLoginData, jobData);
			case READ_JOB_DEFINITION:
				return new ReadJobDefinitionCommand(sapRepository, xmiLoginData, jobData);
			case JOB_COUNT:
				return new JobCountCommand(sapRepository, xmiLoginData, jobData);
			case READ_JOB:
				return new ReadJobCommand(sapRepository, xmiLoginData, jobData);
				// case MODIFY_HEADER:
				// return new ModifyHeaderCommand(sapRepository, xmiLoginData, jobData);
			case CHANGE_VARIANT:
				return new ChangeVariantCommand(sapRepository, xmiLoginData, jobData, inputArguments.getSingleStep());
			case CREATE_RUN_JOB:
				StepVariantValuesTuple stepVariantValuesTuple = new StepVariantValuesTuple(program, variant, variantValuePairs);
				return new CreateRunJobCommand(sapRepository, xmiLoginData, jobData, stepVariantValuesTuple);
			case CREATE_MONITOR_JOB:
				StepVariantValuesTuple stepVariantValuesTuple2 = new StepVariantValuesTuple(program, variant, variantValuePairs);
				return new CreateMonitorJobCommand(sapRepository, xmiLoginData, jobData, stepVariantValuesTuple2);
				/* CHAINS */
			case START_CHAIN:
				return new StartChainCommand(sapRepository, xmiLoginData, inputArguments.getJobName());
			case GET_CHAIN_LOG:
				return new GetChainLogCommand(sapRepository, new ChainLogReaderInput(chainId, chainLogId));
			case GET_CHAIN_ERRORS:
				return new GetChainErrorsCommand(sapRepository, xmiLoginData, inputArguments.getJobName(), "5B343YBQY3RUZ0Q1E94CRIHOA");
			case GET_PROCESS_LOG:
				return new GetProcessLogCommand(sapRepository, xmiLoginData, "5B343YBQY3RUZ0Q1E94CRIHOA", "ABAP", "Z_SCHEDULED_DATE_VAR", jobData.getJobId());
			case GET_PROCESS_JOBS:
				return new GetProcessJobsCommand(sapRepository, xmiLoginData, inputArguments.getJobName(), "TRIGGER", "SCHEDULE_VARIANT", "RSPROCESS",
						"5AYGVUPLZTDAHIQ88DZETQ8ZE");
			case GET_CHAIN_START_CONDITION:
				return new GetChainStartConditionCommand(sapRepository, xmiLoginData, inputArguments.getJobName());
			case CHAIN_SCHEDULE:
				return new ChainScheduleCommand(sapRepository, xmiLoginData, inputArguments.getJobName(), null);
			case CHAIN_SET_STARTCOND:
				return new SetChainStartConditionCommand(sapRepository, xmiLoginData, inputArguments.getJobName(), "");
			case CHAIN_GET_PROCESSES:
				return new GetChainProcessesCommand(sapRepository, xmiLoginData, new ChainData("5B343YBQY3RUZ0Q1E94CRIHOA", inputArguments.getJobName()));
			case CHAIN_GET_PROCESS_LOG:
				return new GetChainProcessLogCommand(sapRepository, xmiLoginData, "5B343YBQY3RUZ0Q1E94CRIHOA", "ABAP", "Z_SCHEDULED_DATE_PROG", "17082800");
			case CHAIN_GET_STATUS:
				return new GetChainStatusCommand(sapRepository, new ChainStatusReaderInput(chainId, chainLogId));
			case MONITOR_CHAIN:
				return new MonitorChainCommand(sapRepository, xmiLoginData, chainId, inputArguments.getUser());
			case COPY_RUN_JOB:
				StepVariantValuesTuple stepVariantValuesTuples = new StepVariantValuesTuple(program, variant, variantValuePairs);
				return new CopyRunJobCommand(sapRepository, xmiLoginData, jobData, stepVariantValuesTuples,inputArguments.getJobId());	
			case COPY_MONITOR_JOB:
				StepVariantValuesTuple stepVariantValuesTuples2 = new StepVariantValuesTuple(program, variant, variantValuePairs);
				return new CopyMonitorJobCommand(sapRepository, xmiLoginData, jobData, stepVariantValuesTuples2,inputArguments.getJobId());		
			default:
				return new HelpCommand();
		}
	}
}