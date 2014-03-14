package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WcToolTest {

	private WcTool wctool;
	private WcTool wctool2;
	private WcTool wctool3;
	private WcTool wctool4;
	private WcTool wctool5;
	private final File f = new File(System.getProperty("user.dir"));
	Charset std = Charset.forName("UTF-8");

	@Before
	public void before() {
		wctool = new WcTool(null);
	}

	@After
	public void after() {
		wctool = null;
	}

	// test the functionality of getCharacterCount to count the number of
	// characters in a file and the execute to call the method
	@Test
	public void getCharacterCountTest() throws IOException {
		// assertEquals(wctool.getCharacterCount(readFile("testCase_1.txt",
		// std)), "50");
		assertEquals("33", wctool.getCharacterCount(readFile("testCase_2.txt", std)));
		assertEquals("16", wctool.getCharacterCount(readFile("testCase_3.txt", std)));
		assertEquals("38", wctool.getCharacterCount(readFile("testCase_4.txt", std)));
		assertEquals("31", wctool.getCharacterCount(readFile("testCase_5.txt", std)));

		String[] args2 = { "-m", "testCase_1.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("46\ttestCase_1.txt", wctool2.execute(null, null));
		
		wctool2 = null;
		String[] args3 = { "-m", "testCase_2.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("33\ttestCase_2.txt", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-m", "testCase_3.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("16\ttestCase_3.txt", wctool4.execute(null, null));
	}

	// test the functionality of getWordCount to count the number of words in a
	// file and the execute to call the method
	@Test
	public void getWordCountTest() throws IOException {
		assertEquals("10", wctool.getWordCount(readFile("testCase_1.txt", std)));
		assertEquals("4", wctool.getWordCount(readFile("testCase_2.txt", std)));
		assertEquals("6", wctool.getWordCount(readFile("testCase_3.txt", std)));
		assertEquals("6", wctool.getWordCount(readFile("testCase_4.txt", std)));
		assertEquals("5", wctool.getWordCount(readFile("testCase_5.txt", std)));

		String[] args2 = { "-w", "testCase_1.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("10\ttestCase_1.txt", wctool2.execute(null, null));
		wctool2 = null;

		String[] args3 = { "-w", "testCase_2.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("4\ttestCase_2.txt", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-w", "testCase_3.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("6\ttestCase_3.txt", wctool4.execute(null, null));
	}

	// test the functionality of getNewLineCount to count the number of lines in
	// a file and the execute to call the method
	@Test
	public void getNewLineCountTest() throws IOException {
		assertEquals(wctool.getNewLineCount(readFile("testCase_1.txt", std)),
				"4");
		assertEquals(wctool.getNewLineCount(readFile("testCase_2.txt", std)),
				"3");
		assertEquals(wctool.getNewLineCount(readFile("testCase_3.txt", std)),
				"5");
		assertEquals(wctool.getNewLineCount(readFile("testCase_4.txt", std)),
				"6");
		assertEquals(wctool.getNewLineCount(readFile("testCase_5.txt", std)),
				"4");

		String[] args2 = { "-l", "testCase_1.txt" };
		wctool2 = new WcTool(args2);
		assertEquals("4\ttestCase_1.txt", wctool2.execute(null, null));
		wctool2 = null;

		String[] args3 = { "-l", "testCase_2.txt" };
		wctool3 = new WcTool(args3);
		assertEquals("3\ttestCase_2.txt", wctool3.execute(null, null));
		wctool3 = null;

		String[] args4 = { "-l", "testCase_3.txt" };
		wctool4 = new WcTool(args4);
		assertEquals("5\ttestCase_3.txt", wctool4.execute(null, null));
	}

	// test the functionality of printing help message
	@Test
	public void getHelpTest() {
		assertEquals(wctool.getHelp(), "-m : Print only the character counts\t"
				+ " -w : Print only the word counts\t"
				+ " -l : Print only the newline counts\t"
				+ " -help : Brief information about supported options");

		String[] args2 = { "-help" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null),
				"-m : Print only the character counts\t"
						+ " -w : Print only the word counts\t"
						+ " -l : Print only the newline counts\t"
						+ " -help : Brief information about supported options");
		wctool2 = null;

		wctool = new WcTool(args2);
		assertEquals(wctool.execute(f, "-help"),
				"-m : Print only the character counts\t"
						+ " -w : Print only the word counts\t"
						+ " -l : Print only the newline counts\t"
						+ " -help : Brief information about supported options");

	}

	// test if the invalid cases are being handled
	@Test
	public void validationTest() {
		String[] args2 = { "-m", "notExist.txt" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null),
				"word count: open failed: notExist.txt: No such file or directory.");
		wctool2 = null;

		String[] args3 = { "-l", "notExist.txt" };
		wctool3 = new WcTool(args3);
		assertEquals(wctool3.execute(null, null),
				"word count: open failed: notExist.txt: No such file or directory.");
		wctool3 = null;

		String[] args4 = { "-w", "notExist.txt" };
		wctool4 = new WcTool(args4);
		assertEquals(wctool4.execute(null, null),
				"word count: open failed: notExist.txt: No such file or directory.");
		wctool4 = null;

		String[] args5 = { "-x", "testCase_1.txt" };
		wctool5 = new WcTool(args5);
		assertEquals(wctool5.execute(null, null), "Invalid arguments.");
		wctool5 = null;

		assertEquals(wctool.execute(null, null),
				"No arguments and no standard input.");
	}

	@Test
	public void testWc() {
		String[] args = { "-l" };
		String stdin = "txt2html/HTML::TextToHTML Sample Conversion\n"
				+ "\n"
				+ "This sample is based hugely on the original sample.txt produced\n"
				+ "by Seth Golub for txt2html.\n"
				+ "\n"
				+ "I used the following options to convert this document:\n"
				+ "\n"
				+ "-titlefirst -mailmode -make_tables\n"
				+ "--system_link_dict txt2html.dict\n"
				+ "--append_body sample.foot --infile sample.txt --outfile sample.html\n"
				+ "\n"
				+ "This has either been done at the command line with:\n"
				+ "\n"
				+ "	perl -MHTML::TextToHTML -e run_txt2html -- *options*\n";
		WcTool wt = new WcTool(args);
		String result = wt.execute(null, stdin);
		String expected = "14";
		assertEquals(expected, result);
	}
	
	@Test
	public void testGeneralOption() {
		String[] args = {"testCase_5.txt"};
		WcTool wt = new WcTool(args);
		String result = wt.execute(null, "");
		String expected = "\t4\t5\t31\ttestCase_5.txt";
		assertEquals(expected, result);
	}
	
	@Test
	public void testRelativePath() {
		String[] args = {"-l", "test/file1.txt"};
		WcTool wt = new WcTool(args);
		String result = wt.execute(null, "");
		String expected = "302\ttest/file1.txt";
		assertEquals(expected, result);
	}

	public String readFile(String fileName, Charset encoding)
			throws IOException {
		try {
			String filePath;
			File f = new File(fileName);
			if (f.isAbsolute())
				filePath = fileName;
			else
				filePath = System.getProperty("user.dir") + File.separator
						+ fileName;

			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			throw new IOException(fileName);
		}
	}
}