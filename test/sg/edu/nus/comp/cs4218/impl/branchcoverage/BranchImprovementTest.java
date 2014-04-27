package sg.edu.nus.comp.cs4218.impl.branchcoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;

/**
 * Branch coverage was improved by adding test cases to test branches which were
 * not covered
 * 
 * It was also improved by deleting redundant or duplicate branches at various
 * places. For example- cat tool was testing if the file can be read multiple
 * times and stdin was checked for null at a method where it cannot be sent as
 * null. There were also assert instead of assertTrue statements in test classes
 * which was reducing the branch coverage
 * 
 */
public class BranchImprovementTest {

	private CommTool commTool;
	private CutTool cutTool;
	private static final String INVALID_COMMAND = "Invalid command";
	private static final String NOT_SORTED = "Not Sorted!";

	@Before
	public void before() {
		commTool = new CommTool(null);
	}

	@After
	public void after() {
		commTool = null;
	}

	// ---------------------COMM TOOL----------------
	@Test
	public void testcompareFilesCheckSortStatusWithNullInputs() {
		String result = commTool.compareFilesCheckSortStatus(null, null);
		assertEquals("-1", result);

	}

	@Test
	public void testcompareFilesCheckSortStatusWithNullInput1AndSmalInput2() {
		commTool.currentLine2 = "xyz";
		String result = commTool.compareFilesCheckSortStatus(null, "abc");
		assertEquals(NOT_SORTED, result);
	}

	@Test
	public void testcompareFilesCheckSortStatusWithNullInput2AndSmalInput1() {
		commTool.currentLine1 = "xyz";
		String result = commTool.compareFilesCheckSortStatus("abc", null);
		assertEquals(NOT_SORTED, result);
	}

	@Test
	public void testExecuteWithNullArgs() {
		CommTool ct = new CommTool(null);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		assertEquals(INVALID_COMMAND, ct.execute(f, null));
	}

	@Test
	public void testdecodeParsedOperationForF2() {
		String[] args = { "commTestCase1a.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "f2");
		assertEquals(true, commTool.flag2);
		assertEquals(stdin, commTool.file2);
		assertEquals(args[0], commTool.file1);
	}

	@Test
	public void testdecodeParsedOperationFor3Args() {
		String[] args = { "-c", "commTestCase1a.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "c:f2");
		assertEquals(true, commTool.flag2);
		assertEquals(stdin, commTool.file2);
		assertEquals(args[1], commTool.file1);
		assertEquals(true, commTool.sortFlag);
	}

	@Test
	public void testdecodeParsedOperationFor3ArgsWithF1c() {
		String[] args = { "-c", "commTestCase1a.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "c:f1");
		assertEquals(true, commTool.flag1);
		assertEquals(stdin, commTool.file1);
		assertEquals(args[2], commTool.file2);
		assertEquals(true, commTool.sortFlag);
	}

	@Test
	public void testdecodeParsedOperationFor3ArgsWithF2d() {
		String[] args = { "-d", "commTestCase1a.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "d:f2");
		assertEquals(true, commTool.flag2);
		assertEquals(args[1], commTool.file1);
		assertEquals(stdin, commTool.file2);
		assertEquals(false, commTool.sortFlag);
	}

	@Test
	public void testdecodeParsedOperationFor3ArgsWithF1d() {
		String[] args = { "-d", "commTestCase1a.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "d:f1");
		assertEquals(true, commTool.flag1);
		assertEquals(stdin, commTool.file1);
		assertEquals(args[2], commTool.file2);
		assertEquals(false, commTool.sortFlag);
	}

	@Test
	public void testBuildParseCodeForCountGreaterThanOne() {
		String[] args = { "-d", "commTestCase1a.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		String result = commTool.buildParseCode("this is parsecode", 3);
		assertEquals(INVALID_COMMAND, result);
	}

	@Test
	public void testBuildParseCodeForF2() {
		String[] args = { "-d", "-", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		String result = commTool.buildParseCode("this is parsecode", 0);
		assertEquals("f2", result);
	}

	// ---------------CUT TOOL
	@Test
	public void stdinValidationTest() {
		CutTool cuttooltest = new CutTool(null);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		cuttooltest.execute(f, null);
		assertEquals(cuttooltest.getStatusCode(), 1);
	}

	@Test
	public void executeDashTest() throws IOException {
		String[] args = { "-c", "1-3", "-" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void cutSpecfiedCharactersTest() throws IOException {
		String[] args = { "-c", "1-3", "-" };
		cutTool = new CutTool(args);
		String currentLine = "Executing.the.program";
		String list = "1,2-4,5";
		assertEquals(cutTool.cutSpecfiedCharacters(list, currentLine), "Execu");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	// --------------PASTE TOOL
	@Test
	public void validationTestCheckNullDirectory() {
		String[] args = { "-s", "testCase_1.txt" };
		PasteTool pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(null, null),
				"Error: No such file or directory\n");
	}

	@Test
	public void testpasteWithDelimeter() {
		File f = new File(System.getProperty("user.dir"));
		String[] args = { "-d", "$$$", "testCase_1.txt", "testCase_2.txt",
				"testCase_3.txt", "testCase_4.txt" };
		PasteTool pastetool = new PasteTool(args);

		assertEquals(pastetool.execute(f, null),
				"1. IBM$$$gek1517$$$a$$$apple\n"
						+ "2. Symantec$$$ACC1002X$$$b$$$ban\n"
						+ "3. Palantir$$$sw2104$$$b$$$banana\n"
						+ "10. hp$$$pc1141$$$c$$$carrot\n"
						+ "11. ihis$$$c$$$carro\n" + "c$$$c");

	}

	// ---------------WC TOOL------------------

	@Test
	public void wordCountNullInput() {
		String[] args = { "-l", "empty.txt" };
		WcTool wt = new WcTool(args);
		String result = wt.getWordCount(null);
		String expectedOutput = "0";
		assertEquals(result, expectedOutput);
	}

	@Test
	public void wordCountCharacterCountNullInput() {
		String[] args = { "-m", "empty.txt" };
		WcTool wt = new WcTool(args);
		String result = wt.getCharacterCount(null);
		String expectedOutput = "0";
		assertEquals(result, expectedOutput);
	}

	// ------------------------SORT TOOL----------------------
	@Test
	public void testForInvalidFileCase() {
		File f = new File(System.getProperty("user.dir"));
		String[] args = { "-c", "notExist.txt" };
		SortTool sorttool = new SortTool(args);
		assertEquals(sorttool.execute(f, null),
				"sort: open failed: notExist.txt: No such file or directory.");
	}

	@Test
	public void testForNullStdin() {
		File f = new File(System.getProperty("user.dir"));
		String[] args3 = { "testCase_2.txt" };
		SortTool sorttool = new SortTool(args3);
		assertEquals(sorttool.execute(f, null),
				"ACC1002X\ngek1517\npc1141\nsw2104");
	}
}
