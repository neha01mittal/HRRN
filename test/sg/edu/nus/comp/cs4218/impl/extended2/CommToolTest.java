package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommToolTest {

	private CommTool commTool;
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

	// test interface methods
	@Test
	public void testExecuteHelpTest() {
		String[] args = { "-c", "-help", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String result = "comm : Compares two sorted files line by line. With no options, produce three-column output."
				+ "\nColumn one contains lines unique to FILE1, column two contains lines unique to FILE2,"
				+ "\nand column three contains lines common to both files."
				+ "\n"
				+ "\nCommand Format - comm [OPTIONS] FILE1 FILE2"
				+ "\nFILE1 - Name of the file 1"
				+ "\nFILE2 - Name of the file 2"
				+ "\n-c : check that the FILE1 is correctly sorted (increasing order) and also FILE2"
				+ "\n if it is not correctly sorted, 'Not Sorted!' is shown"
				+ "\n-d : do not check that the FILE1 and FILE2 are correctly sorted"
				+ "\n-help : Brief information about supported options";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), result);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteNoOptionTest() {
		String[] args = { "commTestCase1a.txt", "commTestCase1b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);
	}

	@Test
	public void testExecuteNoOptionTest1() {
		String[] args = { "commTestCase1b.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee"
				+ "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	/** @NewCaseAdded
	 * 
	 */
	@Test
	public void testExecuteNoOptionTestAbsolutePath() {
		String workingDir = System.getProperty("user.dir");
		String[] args = { workingDir + File.separator + "commTestCase1b.txt",
				workingDir + File.separator + "commTestCase1a.txt" };
		commTool = new CommTool(args);

		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee"
				+ "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	/** @NewCaseAdded
	 * 
	 */
	@Test
	public void testExecuteNoOptionTestInvalidFile() {
		String workingDir = System.getProperty("user.dir");
		String[] args = { workingDir + File.separator + "nofile.txt",
				workingDir + File.separator + "commTestCase1a.txt" };
		commTool = new CommTool(args);
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), INVALID_COMMAND);
		assertEquals(commTool.getStatusCode(), 1);

	}

	/** @NewCaseAdded
	 * 
	 */
	@Test
	public void testExecuteNoOptionTestInvalidFile2() {
		String workingDir = System.getProperty("user.dir");
		String[] args = { workingDir + File.separator + "commTestCase1b.txt",
				workingDir + File.separator + "abcd.txt" };
		commTool = new CommTool(args);
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), INVALID_COMMAND);
		assertEquals(commTool.getStatusCode(), 1);

	}

	/** @NewCaseAdded
	 * 
	 */
	@Test
	public void testExecuteInvalidOptions() {
		String workingDir = System.getProperty("user.dir");
		String[] args = { "-c", "-f", "-v",
				workingDir + File.separator + "commTestCase1b.txt",
				workingDir + File.separator + "abcd.txt" };
		commTool = new CommTool(args);
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), INVALID_COMMAND);
		assertEquals(commTool.getStatusCode(), 1);

	}

	@Test
	public void testExecuteDoNotCheckSortTest() {
		String[] args = { "-d", "commTestCase1a.txt", "commTestCase1b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void testExecuteDoNotCheckSortTest1() {
		String[] args = { "-d", "commTestCase1b.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee"
				+ "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	/**
	 * @NewCaseAdded
	 */
	@Test
	public void testExecuteDoNotCheckSortTest2() {
		String[] args = { "-d", "wrongFileINPUT.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "Invalid command";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 1);

	}
	
	@Test
	public void testExecuteCheckSortTest() {
		String[] args = { "-c", "commTestCase1a.txt", "commTestCase1b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);
	}

	@Test
	public void testExecuteCheckSortTest1() {
		String[] args = { "-c", "commTestCase1b.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee"
				+ "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);
	}

	@Test
	public void testExecuteCheckSortTest2() {
		String[] args = { "-c", "commTestCase2a.txt", "commTestCase2b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "bbb" + "\n\t\tccc" + "\n\t\t\t\tddd" + "\n\t\teee"
				+ "\nfff" + "\nNot Sorted!";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	/**
	 * NewCaseAdded
	 */
	@Test
	public void testExecuteCheckSortTest4() {
		String[] args = { "-c", "-", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "Invalid command";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 1);

	}

	@Test
	public void testExecuteCheckSortTest3() {
		String[] args = { "-c", "commTestCase2b.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\tbbb" + "\nccc" + "\n\t\t\t\tddd" + "\neee"
				+ "\n\t\tfff" + "\nNot Sorted!";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}
/**
 * @CORRECTED - changed file name to uniqTestCase1 and 2 from
  uniqTestCase1b and 1a (No files with those names were given to us)
 * @throws IOException
 */
	@Test
	public void testExecuteMutipleOptionsTest() throws IOException {
	
		String[] args = { "-c", "-d", "uniqTestCase1.txt", "uniqTestCase2.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);

	}
	
	/**
	 * NewCaseAdded
	 * @throws IOException
	 */
	@Test
	public void testExecuteMutipleOptionsTest1() throws IOException {
		// @CORRECTED - changed file name to uniqTestCase1 and 2 from
		// uniqTestCase1b and 1a (No files with those names were given to us)
		String[] args = { "-", "-", "uniqTestCase1.txt", "uniqTestCase2.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);

	}
	
	/**
	 * NewCaseAdded
	 * @throws IOException
	 */
	@Test
	public void testExecuteMutipleOptionsTest2() throws IOException {
		// @CORRECTED - changed file name to uniqTestCase1 and 2 from
		// uniqTestCase1b and 1a (No files with those names were given to us)
		String[] args = { "-d", "-", "uniqTestCase1.txt", "uniqTestCase2.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);

	}

	// no positive test for this one possible
	@Test
	public void testExecuteInvalidOptionTest() throws IOException {
		String[] args = { "-" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);
	}

	// no positive test for this one possible
	@Test
	public void testExecuteInvalidFileTest() throws IOException {
		String[] args = { "-i", "commTestCase1a.txt", "input2q3t.tx3t" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);
	}

	@Test
	public void testExecuteSameFileTest() throws IOException {
		String[] args = { "-c", "commTestCase1a.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\t\t\tccc" + "\n\t\t\t\teee"
				+ "\n\t\t\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);
	}

	/**
	 * @NEWCASEADDED
	 * @throws IOException
	 */
	@Test
	public void testExecuteSameFileTest2() throws IOException {
		String[] args = { "-d", "commTestCase1a.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\t\t\tccc" + "\n\t\t\t\teee"
				+ "\n\t\t\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);
	}
	
	@Test
	public void testCompareFilesDifferentInputTest() {
		String input1 = "abc aed";
		String input2 = "acdef fe";

		assertTrue(Integer.parseInt(commTool.compareFiles(input1, input2)) < 0);
	}

	/**
	 * @NEWCASEADDED
	 */
	@Test
	public void testCompareFilesDifferentInputTest1() {
		String input1 = "azz aed";
		String input2 = "acdef fe";

		assertTrue(Integer.parseInt(commTool.compareFiles(input1, input2)) > 0);
	}
	
	/**
	 * @NEWCASEADDED
	 */
	@Test
	public void testCompareFilesDifferentInputTest2() {
		String input1 = "I am zz aed";
		String input2 = "acdef fe";
		String result = commTool.compareFiles(input1, input2);
		assertTrue(Integer.parseInt(result)< 0);
	}

	@Test
	public void testCompareFilesSameInputTest() {
		String input1 = "1245 ggg";
		String input2 = "1245 ggg";

		assertTrue(Integer.parseInt(commTool.compareFiles(input1, input2)) == 0);
	}

	@Test
	public void testCompareFilesDoNotCheckSortStatusDifferentInputTest() {
		String input1 = "acdef 124";
		String input2 = "abc 125";

		assertTrue(Integer.parseInt(commTool.compareFilesDoNotCheckSortStatus(
				input1, input2)) > 0);
	}

	@Test
	public void testCompareFilesDoNotCheckSortStatusSameInputTest() {
		String input1 = "1245 ggg";
		String input2 = "1245 ggg";

		assertTrue(Integer.parseInt(commTool.compareFilesDoNotCheckSortStatus(
				input1, input2)) == 0);
	}

	@Test
	public void testCompareFilesCheckSortStatusSortedTest() {
		commTool.currentLine1 = "abc";
		String input1 = "abc ";

		commTool.currentLine2 = "ab efh";
		String input2 = "acdef";

		assertTrue(Integer.parseInt(commTool.compareFilesCheckSortStatus(
				input1, input2)) < 0);
	}

	@Test
	public void testCompareFilesCheckSortStatusInput1UnsortedTest() {
		commTool.currentLine1 = "abc ewr";
		String input1 = "abc ";

		commTool.currentLine2 = "ab efh";
		String input2 = "acdef";

		assertEquals(commTool.compareFilesCheckSortStatus(input1, input2),
				NOT_SORTED);
	}

	@Test
	public void testCompareFilesCheckSortStatusInput2UnsortedTest() {
		commTool.currentLine1 = "abc";
		String input1 = "abc ";

		commTool.currentLine2 = "eifjefe epfamfe";
		String input2 = "acdef";

		assertEquals(NOT_SORTED, commTool.compareFilesCheckSortStatus(input1, input2));
	}

	/**
	 * @NewCaseAdded
	 */
	@Test 
	public void testParserForDF1() {
		String[] args = { "-d", "-", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String output = commTool.parse();
		assertEquals("d:f1",output);
	}
	

	/**
	 * @NewCaseAdded
	 */
	@Test 
	public void testParserForCF2() {
		String[] args = { "-c", "commTestCase1a.txt", "-" };
		commTool = new CommTool(args);
		String output = commTool.parse();
		assertEquals("c:f2",output);
	}
	
	/**
	 * @NewCaseAdded
	 */
	@Test 
	public void testParserForInvalidCommand() {
		String[] args = { "-c", "-", "commTestCase1a.txt", "-" };
		commTool = new CommTool(args);
		String output = commTool.parse();
		assertEquals(INVALID_COMMAND,output);
	}
	// user defined function
	@Test
	public void readFiletest() {
		File f = new File("commTestCase1a.txt");
		List<String> output = commTool.readFile(f);
		List<String> expected = new ArrayList<String>();
		expected.add("aaa");
		expected.add("ccc");
		expected.add("eee");
		expected.add("gggggg");
		assertEquals(expected, output);
	}
}
