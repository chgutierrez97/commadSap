package ast.sap.connector.misc;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Joiner;

public class JoinerTest {

	@Test
	public void test() {
		String[] args = { "-c", "MONITOR_CHAIN", "-p", "1234", "-j", "SOME_CHAIN" };
		String joined = Joiner.on(" ").join(args);
		System.out.println(joined);
		
		String replaced = joined.replaceFirst("-p +\\S+", "");
		System.out.println(replaced);
	}

}
