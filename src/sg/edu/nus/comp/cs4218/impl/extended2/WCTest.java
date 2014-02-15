package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WCTest {

	private WcTool wctool;
	private WcTool wctool2;
	private WcTool wctool3;
	private WcTool wctool4;
	private WcTool wctool5;
	private final File f = new File(System.getProperty("user.dir"));

	@Before
	public void setUp() throws Exception {
		wctool = new WcTool(null);

	}

	@After
	public void tearDown() throws Exception {
		wctool = null;
	}

	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void getCharacterCountTest() {

		String[] args = { "-m", "testCase_1.txt" };
		wctool = new WcTool(args);
		assertEquals("50 testCase_1.txt", wctool.execute(null, null));
		wctool = null;

		String[] args2 = { "-m", "testCase_2.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("33 testCase_2.txt", wctool2.execute(null, null));
		wctool2 = null;

		String[] args3 = { "-m", "testCase_3.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("16 testCase_3.txt", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-m", "testCase_4.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("38 testCase_4.txt", wctool4.execute(null, null));
		wctool4 = null;

		String[] args5 = { "-m", "testCase_5.txt" };
		wctool5 = new WcTool(args5);
		assertEquals("31 testCase_5.txt", wctool5.execute(null, null));
		wctool5 = null;
	}

	// test the functionality of getWordCount to count the number of words in a
	// file and the execute to call the method
	@Test
	public void getWordCountTest() {

		String[] args = { "-w", "testCase_1.txt" };
		wctool = new WcTool(args);
		assertEquals("10 testCase_1.txt", wctool.execute(null, null));
		wctool = null;

		String[] args2 = { "-w", "testCase_2.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("4 testCase_2.txt", wctool2.execute(null, null));
		wctool2 = null;

		String[] args3 = { "-w", "testCase_3.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("6 testCase_3.txt", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-w", "testCase_4.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("6 testCase_4.txt", wctool4.execute(null, null));
		wctool4 = null;

		String[] args5 = { "-w", "testCase_5.txt" };
		wctool5 = new WcTool(args5);
		assertEquals("5 testCase_5.txt", wctool5.execute(null, null));
		wctool5 = null;
	}

	// test the functionality of getNewLineCount to count the number of lines in
	// a file and the execute to call the method
	@Test
	public void getNewLineCountTest() {
		// assertEquals(wctool.getNewLineCount("testCase_1.txt"), "5");
		// assertEquals(wctool.getNewLineCount("testCase_2.txt"), "4");
		// assertEquals(wctool.getNewLineCount("testCase_3.txt"), "6");
		// assertEquals(wctool.getNewLineCount("testCase_4.txt"), "6");
		// assertEquals(wctool.getNewLineCount("testCase_5.txt"), "5");

		String[] args = { "-l", "testCase_1.txt" };
		wctool = new WcTool(args);
		assertEquals("5 testCase_1.txt", wctool.execute(null, null));
		wctool = null;

		String[] args2 = { "-l", "testCase_2.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("4 testCase_2.txt", wctool2.execute(null, null));
		wctool2 = null;

		String[] args3 = { "-l", "testCase_3.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("6 testCase_3.txt", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-l", "testCase_4.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("6 testCase_4.txt", wctool4.execute(null, null));
		wctool4 = null;

		String[] args5 = { "-l", "testCase_5.txt" };
		wctool5 = new WcTool(args5);
		assertEquals("5 testCase_5.txt", wctool5.execute(null, null));
		wctool5 = null;
	}

	// test the functionality of printing help message
	@Test
	public void getHelpTest() {
		assertEquals("-m : Print only the character counts\t" + " -w : Print only the word counts\t" + " -l : Print only the newline counts\t"
				+ " -help : Brief information about supported options", wctool.getHelp());

		String[] args2 = { "-help" };
		wctool2 = new WcTool(args2);
		assertEquals("-m : Print only the character counts\t" + " -w : Print only the word counts\t" + " -l : Print only the newline counts\t"
				+ " -help : Brief information about supported options", wctool2.execute(null, null));
		wctool2 = null;

		// assertEquals(wctool.execute(f, "-help"),
		// "-m : Print only the character counts\t" +
		// " -w : Print only the word counts\t"
		// + " -l : Print only the newline counts\t" +
		// " -help : Brief information about supported options");

	}

	// test if the invalid cases are being handled
	@Test
	public void validationTest() {
		String[] args2 = { "-m", "notExist.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("word count: open failed: notExist.txt: No such file or directory.", wctool2.execute(null, null));
		wctool2 = null;

		String[] args3 = { "-w", "notExist.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("word count: open failed: notExist.txt: No such file or directory.", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-w", "notExist.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("word count: open failed: notExist.txt: No such file or directory.", wctool4.execute(null, null));
		wctool4 = null;

		String[] args5 = { "-x", "testCase_1.txt" };
		wctool5 = new WcTool(args5);
		assertEquals("Invalid arguments.", wctool5.execute(null, null));
		wctool5 = null;

		assertEquals("No arguments and no standard input.", wctool.execute(null, null));

	}

}
