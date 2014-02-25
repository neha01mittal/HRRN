package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniqToolTest {

	private UniqTool	uniqTool;

	@Before
	public void before() {
		uniqTool = new UniqTool(null);
	}

	@After
	public void after() {
		uniqTool = null;
	}

	// testing interface methods
	@Test
	public void executeHelpTest() {
		String[] args = { "-i", "-help", "input.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String result = "uniq : Writes the unique lines in the given input. The input need not be sorted, but repeated input lines are detected only if they are adjacent."
				+ "\nCommand Format - uniq [OPTIONS] [FILE]"
				+ "\nFILE - Name of the file, when no file is present (denoted by '-') use standard input"
				+ "\nOPTIONS"
				+ "\n-f NUM : Skips NUM fields on each line before checking for uniqueness. Use a null"
				+ "\nstring for comparison if a line has fewer than n fields. Fields are sequences of"
				+ "\nnon-space non-tab characters that are separated from each other by at least one space or tab."
				+ "\nNUM is a single digit positive integer"
				+ "\n-i : Ignore differences in case when comparing lines."
				+ "\n-help : Brief information about supported options";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), result);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeNoOptionTest() {
		String[] args = { "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	@Test
	public void executeNoOptionTest1() {
		String[] args = { "uniqTestCase1.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	@Test
	public void executeNoOptionTest2() {
		String[] args = { "uniqTestCase2.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\nAaAaA aaa" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeIgnoreCaseTest() {
		String[] args = { "-i", "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeIgnoreCaseTest1() {
		String[] args = { "-i", "uniqTestCase1.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeIgnoreCaseTest2() {
		String[] args = { "-i", "uniqTestCase2.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";

		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeFieldNegativeTest() {
		String[] args = { "-f", "-1", "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeField0Test() {
		String[] args = { "-f", "0", "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeFieldTest() {
		String[] args = { "-f", "1", "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	@Test
	public void executeStdinTest() {
		// Test case wrong !!!!!
		// String stdin = "-i -f 1 uniqTestCase2.txt";

		String stdin = readFile("uniqTestCase2.txt");
		uniqTool = new UniqTool(new String[] { "-i", "-f", "1", "-" });
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, stdin), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	// Test case wrong !!!!!
	// @Test
	// public void executeFieldMoreThanLineLengthTest() {
	// String[] args = { "-f", "3", "uniqTestCase.txt" };
	// uniqTool = new UniqTool(args);
	// String workingDir = System.getProperty("user.dir");
	// String output = "AAAAA AAA" + "\n123456789" + "\n12345";
	// File f = new File(workingDir);
	// assertEquals(uniqTool.execute(f, null), output);
	// assertEquals(uniqTool.getStatusCode(), 0);
	// }

	// Test case wrong !!!!!
	// @Test
	// public void executeMutipleOptionsTest() throws IOException {
	// String[] args = { "-f", "1", "-f", "3", "uniqTestCase.txt" };
	// uniqTool = new UniqTool(args);
	// String workingDir = System.getProperty("user.dir");
	// File f = new File(workingDir);
	// assertEquals(uniqTool.execute(f, null), "Invalid command");
	// assertEquals(uniqTool.getStatusCode(), 1);
	// }

	@Test
	public void executeIgnoreCaseFieldTest() {
		String[] args = { "-f", "0", "-i", "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeIgnoreCaseFieldTest1() {
		String[] args = { "-i", "-f", "0", "uniqTestCase1.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeIgnoreCaseFieldTest2() {
		String[] args = { "-i", "-f", "0", "uniqTestCase2.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);

	}

	@Test
	public void executeIgnoreCaseFieldTest3() {
		String[] args = { "-i", "-f", "1", "uniqTestCase2.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "12345 111" + "\n123456789" + "\nAAAAA AAA" + "\n123";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	@Test
	public void executeFieldWithNoNumTest() throws IOException {
		String[] args = { "-f", "input.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), "Invalid command");
		assertEquals(uniqTool.getStatusCode(), 1);
	}

	@Test
	public void executeFieldWithSpaceTest() throws IOException {
		String[] args = { "-f", " ", "input.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), "Invalid command");
		assertEquals(uniqTool.getStatusCode(), 1);
	}

	@Test
	public void executeInvalidFileTest() throws IOException {
		String[] args = { "-i", "input2q3t.tx3t" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), "Invalid command");
		assertEquals(uniqTool.getStatusCode(), 1);
	}

	@Test
	public void executeInvalidNumTest() throws IOException {
		String[] args = { "-f", "1-", "input.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), "Invalid command");
		assertEquals(uniqTool.getStatusCode(), 1);
	}

	@Test
	public void executeNegativeNumTest() throws IOException {
		String[] args = { "-f", "-1", "uniqTestCase.txt" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		String output = "AAAAA AAA" + "\n123456789" + "\n12345";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	@Test
	public void executeDashTest() throws IOException {
		String[] args = { "-i", "-" };
		uniqTool = new UniqTool(args);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, null), "Invalid command");
		assertEquals(uniqTool.getStatusCode(), 1);
	}

	@Test
	public void getUniqueCheckCaseBothSameTest() {
		boolean checkCase = true;
		uniqTool.currentLine = "I thInk iT Will Work.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUnique(checkCase, input), "I thInk iT Will Work.");
	}

	@Test
	public void getUniqueCheckCaseBothDifferentTest() {
		boolean checkCase = true;
		uniqTool.currentLine = "I thInk iT Will Work.";
		String input = "I thInk iT Will Wor";

		assertEquals(uniqTool.getUnique(checkCase, input), "I thInk iT Will Work.\nI thInk iT Will Wor");
	}

	@Test
	public void getUniqueIgnoreCaseBothSameTest() {
		boolean checkCase = false;
		uniqTool.currentLine = "I THINK IT WILL WORK.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUnique(checkCase, input), "I THINK IT WILL WORK.");
	}

	@Test
	public void getUniqueIgnoreCaseBothDifferentTest() {
		boolean checkCase = false;
		uniqTool.currentLine = "I THINK IT WILL WORK.";
		String input = "I THINK IT WILL WOR";

		assertEquals(uniqTool.getUnique(checkCase, input), "I THINK IT WILL WORK.\nI THINK IT WILL WOR");

	}

	@Test
	public void getUniqueSkipNumCheckCaseNegativeTest() {
		int num = -1;
		boolean checkCase = false;
		uniqTool.currentLine = "o thInk iT Will Work.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "o thInk iT Will Work.\nI thInk iT Will Work.");

	}

	@Test
	public void getUniqueSkipNumCheckCase0Test() {
		int num = 0;
		boolean checkCase = false;
		uniqTool.currentLine = "o thInk iT Will Work.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "o thInk iT Will Work.\nI thInk iT Will Work.");

	}

	@Test
	public void getUniqueSkipNumCheckCase1Test() {
		int num = 1;
		boolean checkCase = false;
		uniqTool.currentLine = "abc thInk iT Will Work.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "abc thInk iT Will Work.");

	}

	@Test
	public void getUniqueSkipNumCheckCase3Test() {
		int num = 3;
		boolean checkCase = false;
		uniqTool.currentLine = "I thInk ERG Will Work.";
		String input = "I thInk ABC Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "I thInk ERG Will Work.");

	}

	@Test
	public void getUniqueSkipNumCheckCaseFieldsInLineEqualsNumTest() {
		int num = 5;
		boolean checkCase = false;
		uniqTool.currentLine = "I thInk iT Will Work.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "");

	}

	@Test
	public void getUniqueSkipNumCheckCaseFieldsInLineLessThanNumTest() {
		int num = 6;
		boolean checkCase = false;
		uniqTool.currentLine = "I thInk iT Will Work.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "");

	}

	@Test
	public void getUniqueSkipNumCheckCaseLineWithDifferentLengthTest() {
		int num = 2;
		boolean checkCase = false;
		uniqTool.currentLine = "I thInk iT Will Work.";
		String input = "I thInk iT Will Work. done!";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "I thInk iT Will Work.\nI thInk iT Will Work. done!");

	}

	@Test
	public void getUniqueSkipNumIgnoreCase0Test() {
		int num = 0;
		boolean checkCase = false;
		uniqTool.currentLine = "i THINK IT WILL WORK.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "i THINK IT WILL WORK.");

	}

	@Test
	public void getUniqueSkipNumIgnoreCase1Test() {
		int num = 1;
		boolean checkCase = false;
		uniqTool.currentLine = "abc THINK IT WILL WORK.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "abc THINK IT WILL WORK.");

	}

	@Test
	public void getUniqueSkipNumIgnoreCase3Test() {
		int num = 3;
		boolean checkCase = false;
		uniqTool.currentLine = "I thInk ERG WILL WORK.";
		String input = "I thInk ABC Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "I thInk ERG WILL WORK.");

	}

	@Test
	public void getUniqueSkipNumIgnoreCaseFieldsInLineEqualsNumTest() {
		int num = 5;
		boolean checkCase = false;
		uniqTool.currentLine = "I THINK IT WILL WORK.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "");

	}

	@Test
	public void getUniqueSkipNumIgnoreCaseFieldsInLineLessThanNumTest() {
		int num = 6;
		boolean checkCase = false;
		uniqTool.currentLine = "I THINK IT WILL WORK.";
		String input = "I thInk iT Will Work.";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "");

	}

	@Test
	public void getUniqueSkipNumIgnoreCaseLineWithDifferentLengthTest() {
		int num = 2;
		boolean checkCase = false;
		uniqTool.currentLine = "I THINK IT WILL WORK.";
		String input = "I thInk iT Will Work. done!";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), "I THINK IT WILL WORK.\nI thInk iT Will Work. done!");

	}

	@Test
	public void getUniqueSkipNumIgnoreCaseEmptyTest() {
		int num = 0;
		boolean checkCase = false;
		uniqTool.currentLine = " ";
		String input = "";

		assertEquals(uniqTool.getUniqueSkipNum(num, checkCase, input), " \n");

	}

	private String readFile(String path) {
		File newFile = new File(path);
		String fullText = "";
		try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				fullText += sCurrentLine + "\n";
			}
		} catch (IOException e) {
			return null;
		}
		return fullText;
	}
}
