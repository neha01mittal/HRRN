package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;

public class CommToolTest {

	private CommTool commTool;

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
	public void executeHelpTest() {
		String[] args = { "-c", "-help", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String result = "comm : Compares two sorted files line by line. With no options, produce three-column output."
				+ "\nColumn one contains lines unique to FILE1, column two contains lines unique to FILE2,"
				+ "\nand column three contains lines common to both files." + "\n" + "\nCommand Format - comm [OPTIONS] FILE1 FILE2"
				+ "\nFILE1 - Name of the file 1" + "\nFILE2 - Name of the file 2"
				+ "\n-c : check that the FILE1 is correctly sorted (increasing order) and also FILE2"
				+ "\n if it is not correctly sorted, 'Not Sorted!' is shown" + "\n-d : do not check that the FILE1 and FILE2 are correctly sorted"
				+ "\n-help : Brief information about supported options";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), result);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeNoOptionTest() {
		String[] args = { "commTestCase1a.txt", "commTestCase1b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee" + "\n\t\tffff" + "\ngggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeNoOptionTest1() {
		String[] args = { "commTestCase1b.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee" + "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeDoNotCheckSortTest() {
		String[] args = { "-d", "commTestCase1a.txt", "commTestCase1b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee" + "\n\t\tffff" + "\ngggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeDoNotCheckSortTest1() {
		String[] args = { "-d", "commTestCase1b.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee" + "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeCheckSortTest() {
		String[] args = { "-c", "commTestCase1a.txt", "commTestCase1b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee" + "\n\t\tffff" + "\ngggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeCheckSortTest1() {
		String[] args = { "-c", "commTestCase1b.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\nbbb" + "\n\t\tccc" + "\n\t\teee" + "\nffff" + "\n\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeCheckSortTest2() {
		String[] args = { "-c", "commTestCase2a.txt", "commTestCase2b.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "bbb" + "\n\t\tccc" + "\n\t\t\t\tddd" + "\n\t\teee" + "\nfff" + "\nNot Sorted!";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeCheckSortTest3() {
		String[] args = { "-c", "commTestCase2b.txt", "commTestCase2a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\tbbb" + "\nccc" + "\n\t\t\t\tddd" + "\neee" + "\n\t\tfff" + "\nNot Sorted!";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);

	}

	@Test
	public void executeMutipleOptionsTest() throws IOException {
		String[] args = { "-c", "-d", "uniqTestCase1b.txt", "uniqTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);

	}

	@Test
	public void executeInvalidOptionTest() throws IOException {
		String[] args = { "-" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);
	}

	@Test
	public void executeInvalidFileTest() throws IOException {
		String[] args = { "-i", "commTestCase1a.txt", "input2q3t.tx3t" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), "Invalid command");
		assertEquals(commTool.getStatusCode(), 1);
	}

	@Test
	public void executeSameFileTest() throws IOException {
		String[] args = { "-c", "commTestCase1a.txt", "commTestCase1a.txt" };
		commTool = new CommTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "\t\t\t\taaa" + "\n\t\t\t\tccc" + "\n\t\t\t\teee" + "\n\t\t\t\tgggggg";
		File f = new File(workingDir);
		assertEquals(commTool.execute(f, null), output);
		assertEquals(commTool.getStatusCode(), 0);
	}

	@Test
	public void compareFilesDifferentInputTest() {
		String input1 = "abc aed";
		String input2 = "acdef fe";

		assertTrue(Integer.parseInt(commTool.compareFiles(input1, input2)) < 0);
	}

	@Test
	public void compareFilesSameInputTest() {
		String input1 = "1245 ggg";
		String input2 = "1245 ggg";

		assertTrue(Integer.parseInt(commTool.compareFiles(input1, input2)) == 0);
	}

	@Test
	public void compareFilesDoNotCheckSortStatusDifferentInputTest() {
		String input1 = "acdef 124";
		String input2 = "abc 125";

		assertTrue(Integer.parseInt(commTool.compareFilesDoNotCheckSortStatus(input1, input2)) > 0);
	}

	@Test
	public void compareFilesDoNotCheckSortStatusSameInputTest() {
		String input1 = "1245 ggg";
		String input2 = "1245 ggg";

		assertTrue(Integer.parseInt(commTool.compareFilesDoNotCheckSortStatus(input1, input2)) == 0);
	}

	@Test
	public void compareFilesCheckSortStatusSortedTest() {
		commTool.currentLine1 = "abc";
		String input1 = "abc ";

		commTool.currentLine2 = "ab efh";
		String input2 = "acdef";

		assertTrue(Integer.parseInt(commTool.compareFilesCheckSortStatus(input1, input2)) < 0);
	}

	@Test
	public void compareFilesCheckSortStatusInput1UnsortedTest() {
		commTool.currentLine1 = "abc ewr";
		String input1 = "abc ";

		commTool.currentLine2 = "ab efh";
		String input2 = "acdef";

		assertEquals(commTool.compareFilesCheckSortStatus(input1, input2), "Not Sorted!");
	}

	@Test
	public void compareFilesCheckSortStatusInput2UnsortedTest() {
		commTool.currentLine1 = "abc";
		String input1 = "abc ";

		commTool.currentLine2 = "eifjefe epfamfe";
		String input2 = "acdef";

		assertEquals(commTool.compareFilesCheckSortStatus(input1, input2), "Not Sorted!");
	}

}
