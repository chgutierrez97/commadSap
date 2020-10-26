package ast.sap.connector.config;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigurationTest {

	@Test
	public void testLoadNonexistentConfig() {
		Configuration.loadConnectorConfig("Nonexistent");
		assertNull(Configuration.getHost());
	}

}
