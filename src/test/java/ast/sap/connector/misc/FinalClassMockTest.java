package ast.sap.connector.misc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

final class FinalClass {
	public final String finalMethod() {
		return "something";
	}
}

public class FinalClassMockTest {

	@Test
	public void test() {
		FinalClass instance = new FinalClass();

		FinalClass mock = mock(FinalClass.class);
		when(mock.finalMethod()).thenReturn("that other thing");

		assertNotEquals(mock.finalMethod(), instance.finalMethod());
	}

}
