package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class EchoToolTest {

	private IEchoTool echotool;

	@Before
	public void before() {

	}

	@After
	public void after() {
		echotool = null;
	}

	@Test
	public void printOneWordInput() throws IOException {
		// Test expected behavior
		// Assume args are correctly parsed by shell
		String[] args = { "test1" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "");
		assertTrue(result.equals(args[0] + "\n"));
		assertEquals(echotool.getStatusCode(), 0);
	}

	@Test
	public void printInputWithDollarSign() throws IOException {
		// Test error-handling 1
		// Assume args are correctly parsed by shell
		String[] args = { "I", "am", "a", "str$ing" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "");
		assertTrue(result.equals("Error in input. Cannot enter $ sign"));
		assertEquals(1, echotool.getStatusCode());
	}

	@Test
	public void printInputWithSpecialSymbols() throws IOException {
		// Test error-handling 1
		// Assume args are correctly parsed by shell
		// Assume all characters (except quotes) including / are printed
		// correctly
		String[] args = { "/%+I", "a@#m", "^a", "st\"r*\"\"(ing" };
		String expectedOutput = "";
		for (String arg : args) {
			expectedOutput += arg + " ";
		}
		expectedOutput = expectedOutput.substring(0, expectedOutput.length() - 1);
		expectedOutput += "\n";
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "");
		assertTrue(result.equals(expectedOutput));
		assertEquals(echotool.getStatusCode(), 0);
	}

	@Test
	public void printNoInput() throws IOException {
		// Test error-handling 2
		echotool = new EchoTool(null);
		String result = echotool.execute(null, "");
		assertTrue(result.equals("\n"));
		assertEquals(echotool.getStatusCode(), 0);
	}

	@Test
	public void printMultipleWordInput() throws IOException {
		// Test error-handling 1
		// Assume args are correctly parsed by shell
		String[] args = { "I", "am", "a", "string" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "");
		assertTrue(result.equals("I am a string\n"));
		assertEquals(echotool.getStatusCode(), 0);
	}
}
