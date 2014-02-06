package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

/**
 * @author Neha Mittal
 * @author Zhang Haoqiang
 */
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
	public void testOneWordInput() throws IOException {
		// Test expected behavior
		// Assume args are correctly parsed by shell
		String[] args = { "test1" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "");

		assertEquals(0, echotool.getStatusCode());
		assertEquals(args[0], result);
	}

	@Test
	public void testNoInput() throws IOException {
		// Test error-handling 1
		echotool = new EchoTool(null);
		echotool.execute(null, null);

		assertNotEquals(0, echotool.getStatusCode());
	}

	@Test
	public void testMultipleWordInput() throws IOException {
		String[] args = { "I", "am", "a", "string" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "");

		assertEquals(echotool.getStatusCode(), 0);
		assertEquals("I am a string", result);
	}

	@Test
	public void testInputWithSpecialSymbols() throws IOException {
		// Test error-handling 1
		// Assume args are correctly parsed by shell
		// Assume all characters (except quotes) including / are tested
		String[] args = { "/%+I", "a@#m", "^a", "st\"r*\"  \"(ing" };
		String expectedOutput = "";
		for (String arg : args) {
			expectedOutput += arg + " ";
		}
		expectedOutput = expectedOutput.substring(0, expectedOutput.length() - 1);
		echotool = new EchoTool(args);
		String result = echotool.execute(null, null);

		assertEquals(0, echotool.getStatusCode());
		assertEquals(expectedOutput, result);
	}

	@Test
	public void testInputWithExtraSpacing() throws IOException {
		// Parser should trim off unnecessary spaces
		String[] args = { "I", " am", " a ", "string" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, null);

		assertEquals(echotool.getStatusCode(), 0);
		assertEquals("I  am  a  string", result);
	}

	@Test
	public void testInputWithOnlyStdin() throws IOException {
		// Parser should trim off unnecessary spaces
		echotool = new EchoTool(null);
		String result = echotool.execute(null, "I am a string");

		assertEquals(echotool.getStatusCode(), 0);
		assertEquals("I am a string", result);
	}

	@Test
	public void testInputWithBothArgsandStdin() throws IOException {
		// Parser should trim off unnecessary spaces
		String[] args = { "I", "am", "a", "string" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "I am also a string");

		assertEquals(echotool.getStatusCode(), 0);
		assertEquals("I am a string I am also a string", result);
	}
}
