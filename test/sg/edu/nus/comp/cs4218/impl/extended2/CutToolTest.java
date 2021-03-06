package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CutToolTest {

	private CutTool cutTool;

	@Before
	public void before() {
		cutTool = new CutTool(null);
	}

	@After
	public void after() {
		cutTool = null;
	}

	// testing interface methods
	@Test
	public void testExecuteHelpTest() {
		String[] args = { "-c", "2-4,1", "-help", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");
		String result = "cut : prints a substring that is specified in a certain range" + "\nCommand Format - cut [OPTIONS] [FILE]"
				+ "\nFILE - Name of the file, when no file is present (denoted by '-') use standard input OPTIONS"
				+ "\n-c LIST: Use LIST as the list of characters to cut out. E.g 'cut -c 1-5,10,18-30'"
				+ "\nspecifies characters 1 through 5, 10 and 18 through 30."
				+ "\n-d DELIM -f FIELD: Use DELIM as the field-separator character instead of the TAB character."
				+ "\nFIELD is similar to LIST except FIELD is used as the list of string to cut out after delimited by DELIM."
				+ "\nThe way to define FIELD is the same as LIST." + "\n-help : Brief information about supported options";
		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), result);
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteUnsortedListCTest() {
		String[] args = { "-c", "2-4,1", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "1234\n1234\n1234\n1234");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteUnsortedListDTest() {
		String[] args = { "-d", "3", "-f", "1", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "12\n12\n12\n12");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteInvalidDelimTest() {
		String[] args = { "-d", "3**", "-f", "1", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "12345\n1234567\n123456789\n12345");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteUnsortedListFTest() {
		String[] args = { "-f", "1", "-d", "3", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "12\n12\n12\n12");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteUnsortedListF0Test() {
		String[] args = { "-f", "0", "-d", "3", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		//assertEquals(cutTool.execute(f, null), "");
		assertEquals(cutTool.execute(f, null), "Invalid command");
		// assertEquals(cutTool.getStatusCode(), 0);
		assertEquals(cutTool.getStatusCode(), 1);

	}

	@Test
	public void testExecuteUnsortedListFRangeTest() {
		String[] args = { "-f", "1-2,2-5", "-d", "3", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "12345\n1234567\n123456789\n12345");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteStdinTest() {
		String stdin = "-f 1-2,2-5 -d 3 input.txt";
		cutTool = new CutTool(null);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, stdin), "12345\n1234567\n123456789\n12345");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteMutipleOptionsTest() throws IOException {
		String[] args = { "-f", "1-3", "-c", "3", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);

	}

	@Test
	public void testExecuteDelimWithNoFieldTest() throws IOException {
		String[] args = { "-d", "3", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void testExecuteDelimWithSpaceTest() throws IOException {
		String[] args = { "-d", " ", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void testExecuteInvalidFileTest() throws IOException {
		String[] args = { "-c", "1", "input2q3t.tx3t" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void testExecuteListMissingDigitTest() throws IOException {
		String[] args = { "-c", "1-", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}
	
	@Test
	public void testExecuteListMissingDigitTest2() throws IOException {
		String[] args = { "-c", "-2", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void testExecuteDashTest() throws IOException {
		String[] args = { "-c", "1-3", "-" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void testCutSpecfiedCharactersTest() throws IOException {
		String currentLine = "Executing.the.program";
		String list = "1,2-4,5";
		assertEquals(cutTool.cutSpecfiedCharacters(list, currentLine), "Execu");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testCutSpecfiedCharactersNegativeTest() throws IOException {
		String currentLine = "Executing.the.program";
		String list = "1,2-4";
		assertNotEquals(cutTool.cutSpecfiedCharacters(list, currentLine), "Execu");
	}

	/**
	 * @Corrected
	 * CHANGES MADE: Giving negative line number is invalid.
	 * 
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterNegativeTest() {
		String currentLine = "Executing.the.program";

		String list = "-1";
		String delim = ".";
		// assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim,
		// currentLine), "");
		assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim, currentLine), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}
	
	/**
	 * @CorrectedMADE: Giving line number 0 is invalid.
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiter0Test() {
		String currentLine = "Executing.the.program";

		String list = "0";
		String delim = ".";
		// assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim,
		// currentLine), "");
		assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim, currentLine), "Invalid command");
		assertEquals(cutTool.getStatusCode(), 1);
	}

	@Test
	public void testCutSpecifiedCharactersUseDelimiter1Test() {
		String currentLine = "Executing.the.program";

		String list = "1";
		String delim = ".";
		assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim, currentLine), "Executing");
		assertEquals(cutTool.getStatusCode(), 0);
	}

	@Test
	public void testCutSpecifiedCharactersUseDelimiterARangeTest() {
		String currentLine = "Executing.the.program";

		// String list = "0-4";
		String list = "1-4";
		String delim = ".";
		assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim, currentLine), "Executing.the.program");
		assertEquals(cutTool.getStatusCode(), 0);
	}

	@Test
	public void testCutSpecifiedCharactersUseDelimiterARangeSpecialTest() {
		String currentLine = "Executing.the.program";

		String list = "2-2";
		String delim = ".";
		assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim, currentLine), "the");
		assertEquals(cutTool.getStatusCode(), 0);
	}

	@Test
	public void testCutSpecifiedCharactersUseDelimiterAFewRangeTest() {
		String currentLine = "Executing.the.program";

		String list = "1,1-5";
		String delim = ".";
		assertEquals(cutTool.cutSpecifiedCharactersUseDelimiter(list, delim, currentLine), "Executing.the.program");
		assertEquals(cutTool.getStatusCode(), 0);
	}

	// test helper methods
	@Test
	public void testSortAndRemoveDuplicateNumbersUnsortedTest() {
		String list = "3-4,1,5,2-3,1";
		// assertEquals(cutTool.sortAndRemoveDuplicateNumbers(list), "1,2-4,5");
		// assertEquals(cutTool.getStatusCode(), 0);
	}

	/**
	 * @Corrected
	 * the other team's user defined function - cannot test
	 */
	//User Defined Function
	@Test
	public void testSortAndRemoveDuplicateNumbersUnsortedOverlapTest() {
		String list = "3-4,1,2-6,2-3";
		// assertEquals(cutTool.sortAndRemoveDuplicateNumbers(list), "1,2-6");
		// assertEquals(cutTool.getStatusCode(), 0);
	}

	/**
	 * @Corrected
	 * the other team's user defined function - cannot test
	 */
	//User Defined Function
	@Test
	public void testSortAndRemoveDuplicateNumbersUnsortedOverlapTest2() {
		String list = "3-4,1,2-6,2-3";
		// assertEquals(cutTool.sortAndRemoveDuplicateNumbers(list), "1,2-6");
		// assertEquals(cutTool.getStatusCode(), 0);
	}

	/**
	 * @Corrected
	 * the other team's user defined function - cannot test
	 */
	//User Defined Function
	@Test
	public void testSortAndRemoveDuplicateNumbersUnsortedOverlapTest3() {
		String list = "10-1";
		// assertEquals(cutTool.sortAndRemoveDuplicateNumbers(list), "10-1");
		// assertEquals(cutTool.getStatusCode(), 0);
	}
	
	@Test
	public void testSortAndRemoveNumbersTest1() {
		String[] args = { "-c", "3-4,1,2-6,2-3,5", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "12345\n123456\n123456\n12345");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testSortAndRemoveNumbersTest2() {
		String[] args = { "-c", "8,7,6,5,4,4,5,6,7,8", "input.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "45\n4567\n45678\n45");
		assertEquals(cutTool.getStatusCode(), 0);

	}

	@Test
	public void testStdinValidationTestWithNullStdin() {
		String[] args = { "-c", "1-3", "-" };
		CutTool cuttooltest = new CutTool(args);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		assertEquals(cuttooltest.execute(f, "1234\n1234\n1234"), "123\n123\n123");
	}

	@Test
	public void testStdinValidationTestWithCOption() {
		String[] args = { "-c", "1-3", "-" };
		CutTool cuttooltest = new CutTool(args);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		assertEquals(cuttooltest.execute(f, null), "Invalid command");

	}
	
	@Test
	public void testStdinValidationTestWithDandFOptions() {
		String[] args = { "-d", "3", "-f" , "1", "-" };
		CutTool cuttooltest = new CutTool(args);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		assertEquals(cuttooltest.execute(f, "1234\n1234\n1234"), "12\n12\n12");
	}
	
}
