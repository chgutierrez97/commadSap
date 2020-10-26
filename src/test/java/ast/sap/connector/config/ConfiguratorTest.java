package ast.sap.connector.config;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfiguratorTest {
	@BeforeClass
	public static void before() {
		Configuration.loadConnectorConfig("connectorsap.properties");
	}

	@Test
	public void GetPropertyFromConfigFile() {
		Assert.assertEquals("mzaragoz", Configuration.getProperty("username"));
	}

	@Test
	public void GetUsername() {
		Assert.assertEquals("mzaragoz", Configuration.getUsername());
	}
}
