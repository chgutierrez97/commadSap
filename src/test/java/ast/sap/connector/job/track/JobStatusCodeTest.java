package ast.sap.connector.job.track;

import static org.junit.Assert.*;

import org.junit.Test;

public class JobStatusCodeTest {

	@Test
	public void testFromCode() {
		assertEquals(JobStatusCode.X, JobStatusCode.fromCode(""));
		assertEquals(JobStatusCode.X, JobStatusCode.fromCode("INVALID"));
		
		assertEquals(JobStatusCode.X, JobStatusCode.fromCode("X"));
		assertEquals(JobStatusCode.A, JobStatusCode.fromCode("A"));
		assertEquals(JobStatusCode.F, JobStatusCode.fromCode("F"));

	}

}
