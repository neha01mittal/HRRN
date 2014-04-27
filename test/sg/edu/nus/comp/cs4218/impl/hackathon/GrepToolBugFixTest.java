package sg.edu.nus.comp.cs4218.impl.hackathon;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

public class GrepToolBugFixTest {
	
	private static File	rootParent;

	@Before
	public void before() {
		rootParent = new File(System.getProperty("user.dir"));
	}

	@After
	public void after() {
	}
	
	/* Bug ID: 49
	 * Fix location: GrepTool.java, line 228
	 */
	@Test
	public void testCountLinesEmptyParas() {
		String[] args = new String[0];
		GrepTool gt = new GrepTool(args);
		int matchingLines = gt.getCountOfMatchingLines("", "");
		assertEquals(0, matchingLines);
	}

	/* Bug ID: 48
	 * Fix location: GrepTool.java, line 618
	 */
	@Test
	public void testMatchngLinesTrailingContextWithRegex() {
		String[] args = new String[0];
		GrepTool gt = new GrepTool(args);
		String contentAlphabet = "a\na\na\n" + "b\nb\n" + "c\nc\n" + "z\n";
		String matchingLines = gt.getMatchingLinesWithTrailingContext(1, "(a|b|z)", contentAlphabet);
		assertEquals("a\na\na\nb\nb\nc\nz", matchingLines);
	}
	
	/* Bug ID: 48
	 * Fix location: GrepTool.java, line 618
	 */
	@Test
	public void testMatchingLinesOnly() {
		String[] args = new String[0];
		GrepTool gt = new GrepTool(args);
		String matchingLines = gt.getMatchingLinesOnlyMatchingPart("a",
				"a a a b c d a");
		assertEquals("aaaa", matchingLines);
	}

	/* Bug ID: 72
	 * Fix location: GrepTool.java, line 91
	 */
	@Test
	public void testCatAndGrep() {
		String[] commands = {"cat testCase_2.txt", "grep \"(g|e)\" -"};
		PipingTool pipingTool = new PipingTool(commands);
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "gek1517";
		assertEquals(expectedOutput, output);
	}
}
