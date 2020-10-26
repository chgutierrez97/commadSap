package ast.sap.connector.main;

import ast.sap.connector.cmd.CommandFactory;

import java.io.File;
import java.lang.Thread;
import ast.sap.connector.cmd.SapCommand;
import ast.sap.connector.cmd.SapCommandResult;
import ast.sap.connector.cmd.impl.EncryptPasswordCommand;
import ast.sap.connector.cmd.impl.HelpCommand;
import ast.sap.connector.config.Configuration;
import ast.sap.connector.dst.SapRepository;
import ast.sap.connector.dst.SingleDestinationDataProvider;
import ast.sap.connector.dst.exception.RepositoryGetFailException;
import ast.sap.connector.func.exception.RspcExecuteException;
import ast.sap.connector.job.track.ReconnectFailException;
import ast.sap.connector.job.variant.VariantFieldChangeException;
import ast.sap.connector.main.args.InputArgsParseException;
import ast.sap.connector.main.args.InputArgumentsData;
import ast.sap.connector.main.args.InputArgumentsParser;
import ast.sap.connector.util.*;
import ast.sap.connector.xmi.exception.XmiLoginException;
import com.google.common.base.Joiner;
import com.sap.conn.jco.JCoException;

import org.codehaus.janino.Java.BreakableStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {
	/*
	 * Codigo de version correspondiente con "\\srv0028\Releases_Desa\CONECTOR SAP"
	 */
	public static final String VERSION = "1.1.1902.1";
	public static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

	private static <E> E firstOrSecond(E item1, E item2) {
		return Selector.GET.firstOrSecond(item1, item2);
	}

	public static void main(String[] arg) {
		
	//String[] args = { "-j", "/AFIL/O_DATANET_S*", "-c", "STATUS_JOB","-i","18/08/2020-12:12:12","-t","RFKK_MASS_ACT_SINGLE_JOB" };
	//String[] args = { "-j", "/AFIL/O_DATANET_S*", "-c", "STATUS_JOB", "-t","RFKK_MASS_ACT_SINGLE_JOB" };
    //String[] args = { "-j", "Z_PRUEBA_2", "-c", "CREATE_MONITOR_JOB", "-t","/AFIL/O_DATANET_S","-i","11045400"};
	//String[] args = { "-j", "Z_PRUEBA", "-c", "COPY_MONITOR_JOB", "-t","/AFIL/O_DATANET_S","-i","11211700"};
	//arg = args;
		
		LOGGER.info("Conector SAP version {}", VERSION);

		String strCmd = Joiner.on(" ").join(arg);
		LOGGER.info("Comando ejecutado: {}", strCmd.replaceFirst("-p +\\S+", "-p **** "));

		LOGGER.info("Iniciando el componente conector sap");
		LOGGER.debug(
				"java.library.path--------------------------------------------------------------------------------------------------------------");
		LOGGER.debug(System.getProperty("java.library.path"));
		LOGGER.debug(
				"-------------------------------------------------------------------------------------------------------------------------------");

		Configuration.loadConnectorConfig("connectorsap.properties");




		InputArgumentsData inputArgs;
		try {
			inputArgs = InputArgumentsParser.INSTANCE.parse(arg);
		} catch (InputArgsParseException e1) {
			LOGGER.error(e1.getMessage());
			return;
		}

		if (inputArgs.isHelp()) {
			new HelpCommand().execute();
			return;
		}

		if (inputArgs.isEncryptPassword()) {
			EncryptPasswordCommand encryptPasswordCommand = new EncryptPasswordCommand(inputArgs.getPassword());
			System.out.println(encryptPasswordCommand.execute().getMessage().or(""));
			return;
		}

		String username = firstOrSecond(inputArgs.getUser(), Configuration.getUsername());
		inputArgs.setUser(username);

		String clientNumber = firstOrSecond(inputArgs.getClientNumber(), Configuration.getClientNumber());
		inputArgs.setClientNumber(clientNumber);
		
		if(inputArgs.getJobId()==null && arg[3].equals("STATUS_JOB")) {
			inputArgs.setJobId("***");
		}
		

		/*
		 * El password en el archivo de configuracion SIEMPRE DEBERA ESTAR ENCRIPTADO,
		 * independientemente del valor de Configuration.encryptionOn()
		 */
		String password;
		if (Configuration.encryptionOn()) {
			LOGGER.info("Encriptacion de passwords habilitada.");
			String rawPassword = firstOrSecond(inputArgs.getPassword(), Configuration.getPassword());
			password = Encryptor.INSTANCE.decrypt(rawPassword);
			System.out.println(Encryptor.INSTANCE.encrypt("Daniel2020"));
			

		} else {
			password = inputArgs.getPassword() == null ? Encryptor.INSTANCE.decrypt(Configuration.getPassword())
					: inputArgs.getPassword();
			System.out.println(Encryptor.INSTANCE.encrypt("Daniel2020"));
		}
		inputArgs.setPassword(password);

		String host = firstOrSecond(inputArgs.getHost(), Configuration.getHost());
		inputArgs.setHost(host);

		String systemNumber = firstOrSecond(inputArgs.getSystemNumber(), Configuration.getSystemNumber());
		inputArgs.setSystemNumber(systemNumber);

		LOGGER.debug("DATOS DE ENTRADA FINALES:");
		LOGGER.debug(inputArgs.toString());

		// String destinationName = "mainDestination";
		String destinationName = "secureDestination";

		OutputError output = null;
		Connector connector = Connector.INSTANCE;
		try {
			LOGGER.info("CONSTRUYENDO CONFIGURACION DE DESTINO SAP {}", destinationName);

			ConnectionData connectionData = new ConnectionData(clientNumber, username, password,inputArgs.getLanguage(), host, systemNumber);
			connectionData.validate();

			/* SE INTENTARA TRABAJAR SIN UN ARCHIVO DE DESTINO */
			SingleDestinationDataProvider.buildNew(destinationName, connectionData).register();
			connector.config(destinationName);
			SapRepository sapRepository = connector.loadDestination().openContext();

			SapCommand command = CommandFactory.INSTANCE.getCommand(inputArgs, sapRepository);
			SapCommandResult commandResult = command.execute();
			
			if (!arg[3].equals("STATUS_JOB")) {
				if (!arg[3].equals("COPY_MONITOR_JOB")) {
					LOGGER.debug("RESULTADO DEL COMANDO: {}", commandResult);
					output = OutputParser.INSTANCE.parseOutput(commandResult);		
				}
				
			}

		} catch (RepositoryGetFailException e) {
			LOGGER.error("OCURRIO UN ERROR AL OBTENER EL REPOSITORIO DE " + destinationName);
			ErrorCode errorCode = ErrorCode.REPOSITORY_GET_FAIL;
			output = new OutputError(errorCode, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (RspcExecuteException e) {
			LOGGER.error("OCURRIO UN ERROR AL EJECUTAR MODULO RSPC");
			ErrorCode errorCode = ErrorCode.RSPC_ERROR;
			output = new OutputError(errorCode, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (JCoException e) {
			LOGGER.error("OCURRIO UN ERROR AL OBTENER LOS ATRIBUTOS DE LA CONEXION");
			ErrorCode errorCode = ErrorCode.SAP_SESSION_ERROR;
			output = new OutputError(errorCode, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (XmiLoginException e) {
			LOGGER.error("OCURRIO UN ERROR AL INICIAR SESION CONTRA XMI");
			ErrorCode errorCode = ErrorCode.XMI_LOGIN_EXCEPTION;
			output = new OutputError(errorCode, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (InvalidConnectionDataException e) {
			LOGGER.error("DATOS DE CONEXION INVALIDOS");
			ErrorCode errorCode = ErrorCode.INSUFFICIENT_CREDENTIALS;
			output = new OutputError(errorCode, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (ExceptionInInitializerError e) {
			LOGGER.error("ERROR AL OBTENER LA BIBLIOTECA NATIVA DE JCO");
			ErrorCode errorCode = ErrorCode.JCO_LIBRARY_ERROR;
			output = new OutputError(errorCode, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (VariantFieldChangeException e) {
			LOGGER.error("ERROR AL MODIFICAR EL CAMPO DE UNA VARIANTE");
			output = new OutputError(ErrorCode.VARIANT_FIELD_CHANGE_ERROR, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (ReconnectFailException e) {
			LOGGER.error("ERROR AL INTENTAR REESTABLECER SESION CON SAP");
			output = new OutputError(ErrorCode.RECONNECT_FAIL, e);
			LOGGER.error("[ERROR] " + output.getCode() + " - " + output.getMessage(), e);
		} catch (Throwable e) {
			LOGGER.error("Error fatal", e);
			ErrorCode errorCode = ErrorCode.UNKNOWN;
			output = new OutputError(errorCode.code, "" + e);
		} finally {
			try {
				connector.closeContext();
			} catch (Exception e) {
			}
			if (output != null) {
				LOGGER.debug("OutputError: {}", output);
				if (output.isSuccess()) {
					return;
				}
				System.out.println("Mensaje: " + output.getMessage());
				System.out.println("Codigo de error: " + output.getTrueCode());
				System.exit(output.getCode());
			}
		}
	}
}
