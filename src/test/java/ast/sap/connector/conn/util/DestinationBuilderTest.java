package ast.sap.connector.conn.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.sap.conn.jco.ext.DestinationDataProvider;

import ast.sap.connector.util.ConnectionData;
import ast.sap.connector.util.DestinationConfigBuilder;

public class DestinationBuilderTest {
	private static final String DESTINATION_NAME_2 = "testDestination_2";
	private static final File DESTINATION_FILE_2 = new File(DESTINATION_NAME_2 + DestinationConfigBuilder.JCO_DESTINATION_FILE_EXTENSION);

	@Before
	public void before() {
		DESTINATION_FILE_2.delete();
	}

	@Test
	public void testBuildCurrDir() throws FileNotFoundException, IOException {
		DestinationConfigBuilder destinationBuilder = DestinationConfigBuilder.INSTANCE;
		String destinationName = DESTINATION_NAME_2;
		ConnectionData connectionData = new ConnectionData("500", "mzaragoz", "Macro2017MM", "EN", "saphanatest", "01");

		File destinationFile = destinationBuilder.build(destinationName, connectionData);
		assertTrue(DESTINATION_FILE_2.exists());

		checkDestinationFile(connectionData, destinationFile);
	}

	private void checkDestinationFile(ConnectionData connectionData, File destinationFile) throws IOException, FileNotFoundException {
		assertTrue(destinationFile.exists());
		Properties destinationProperties = new Properties();
		destinationProperties.load(new FileInputStream(destinationFile));

		assertEquals(connectionData.getHost(), destinationProperties.get(DestinationDataProvider.JCO_ASHOST));
		assertEquals(connectionData.getSystemNumber(), destinationProperties.get(DestinationDataProvider.JCO_SYSNR));
		assertEquals(connectionData.getSapClient(), destinationProperties.get(DestinationDataProvider.JCO_CLIENT));
		assertEquals(connectionData.getUserId(), destinationProperties.get(DestinationDataProvider.JCO_USER));
		assertEquals(connectionData.getPassword(), destinationProperties.get(DestinationDataProvider.JCO_PASSWD));
		assertEquals(connectionData.getLanguage(), destinationProperties.get(DestinationDataProvider.JCO_LANG));
	}

}
