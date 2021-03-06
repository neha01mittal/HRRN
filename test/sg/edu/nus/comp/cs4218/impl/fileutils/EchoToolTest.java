package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

/**
 * echo writes its arguments separated by blanks and terminated by a newline on
 * the standard output Prints the user input as it is.
 * 
 * @usage echo [ | string]
 * @options echo word: Prints word echo ���word���: Prints word echo
 *          ���������word���������: Prints word echo ���������word���������:
 *          Prints word echo ������word������: Prints ���word��� (To look for
 *          word in double quotes, it needs to escaped by single quotes before
 *          and after the double quotes) echo word\n : Prints echo word\n echo
 *          word word2 :Prints word word2
 * @note Removes the first enclosing (single and double) quotes pair- it finds
 *       the first matching pair and removes them. If there is an unmatched
 *       quote, it just prints it. Does not escapes any escape sequences like \n
 *       \t. If the command is followed by no input, the output with a new line.
 * @success
 * @exceptions
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
		String result = echotool.execute(null, null);

		assertEquals(0, echotool.getStatusCode());
		assertEquals("", result);
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
		expectedOutput = expectedOutput.substring(0,
				expectedOutput.length() - 1);
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
		echotool = new EchoTool(null);
		String result = echotool.execute(null, "I am a string");

		assertEquals(echotool.getStatusCode(), 0);
		assertEquals("", result);
	}

	@Test
	public void testInputWithBothArgsandStdin() throws IOException {
		// should always process the back part
		String[] args = { "I", "am", "a", "string" };
		echotool = new EchoTool(args);
		String result = echotool.execute(null, "I am also a string");

		assertEquals(echotool.getStatusCode(), 0);
		assertEquals("I am a string", result);
	}
}
