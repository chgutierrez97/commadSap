package ast.sap.connector.cmd;

/**
 * Representan los comandos disponibles de la aplicacion.
 * 
 * @author martin.zaragoza
 *
 */
public enum AvailableCommand {
	XMI_LOGIN,
	TRACK_JOB,
	RUN_JOB,
	CREATE_JOB,
	USER_GET_DETAIL,
	STOP_JOB,
	RAISE_EVENT,
	GET_JOB_OUTPUT,
	MONITOR_JOB,
	STATUS_JOB,
	RUN_STOP_JOB,
	READ_SPOOL,
	READ_JOB_DEFINITION,
	JOB_COUNT,
	READ_JOB,
	CHANGE_VARIANT,
	CREATE_RUN_JOB,
	CREATE_MONITOR_JOB,
	START_CHAIN,
	GET_CHAIN_LOG,
	GET_CHAIN_ERRORS,
	GET_PROCESS_LOG,
	GET_PROCESS_JOBS,
	GET_CHAIN_START_CONDITION,
	CHAIN_SCHEDULE,
	CHAIN_SET_STARTCOND,
	ENCRYPT_PASSWORD,
	CHAIN_GET_PROCESSES,
	CHAIN_GET_PROCESS_LOG,
	CHAIN_GET_STATUS,
	MONITOR_CHAIN,
	COPY_RUN_JOB,
	COPY_MONITOR_JOB,
	HELP;
}
