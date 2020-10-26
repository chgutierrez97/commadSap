package ast.sap.connector.misc;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

public class Args4jParser {
	private boolean errorFree = false;

	@Option(name = "-n", aliases = { "--client" }, required = false, usage = "numero de instancia de server sap")
	private String clientNumber;

	@Option(name = "-c", aliases = { "--command" }, required = true, usage = "comando a correr")
	private String command;

	@Option(name = "-e", aliases = { "--event" }, required = false, usage = "event id a disparar")
	private String eventId;

	@Option(name = "-h", aliases = { "--host" }, required = false, usage = "ip/host id del servidor sap")
	private String host;

	@Option(name = "-i", aliases = { "--jobid" }, required = false, usage = "id del job a correr/inspeccionar")
	private String jobId;

	@Option(name = "-j", aliases = { "--jobname" }, required = false, usage = "nombre del job a crear/correr/inspeccionar")
	private String jobName;

	@Option(name = "-p", aliases = { "--pass" }, required = false, usage = "password de usuario sap a loguearse/encriptar")
	private String password;

	@Option(name = "-t", aliases = { "--step" }, required = false, usage = "step del job")
	private String jobStep;

	@Option(name = "-v", aliases = { "--variant" }, required = false, usage = "variante del step del job", depends = "-t")
	private String stepVariant;

	@Option(name = "-s", aliases = { "--sysno" }, required = false, usage = "numero de sistema")
	private String systemNumber;

	@Option(name = "-u", aliases = { "--user" }, required = false, usage = "sap username")
	private String user;

	public Args4jParser(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		ParserProperties.defaults().withUsageWidth(120);
		try {
			parser.parseArgument(args);

			errorFree = true;
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
		}
	}

	public String getUser() {
		return user;
	}

	public boolean isErrorFree() {
		return errorFree;
	}

	public String getPassword() {
		return password;
	}

	public String getJobStep() {
		return jobStep;
	}

	public String getStepVariant() {
		return stepVariant;
	}

	public String getCommand() {
		return command;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public String getEventId() {
		return eventId;
	}

	public String getHost() {
		return host;
	}

	public String getJobId() {
		return jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public String getSystemNumber() {
		return systemNumber;
	}
}
