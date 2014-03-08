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
		assertEquals(wctool.getCharacterCount(readFile("testCase_1.txt", std)), "50");
		assertEquals(wctool.getCharacterCount(readFile("testCase_2.txt", std)), "33");
		assertEquals(wctool.getCharacterCount(readFile("testCase_3.txt", std)), "16");
		assertEquals(wctool.getCharacterCount(readFile("testCase_4.txt", std)), "38");
		assertEquals(wctool.getCharacterCount(readFile("testCase_5.txt", std)), "31");

		String[] args2 = { "-m", "testCase_1.txt" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null), "50");
		wctool2 = null;

		String[] args3 = { "-m", "testCase_2.txt" };
		wctool3 = new WcTool(args3);
		assertEquals(wctool3.execute(null, null), "33");
		wctool3 = null;

		String[] args4 = { "-m", "testCase_3.txt" };
		wctool4 = new WcTool(args4);
		assertEquals(wctool4.execute(null, null), "16");
		wctool4 = null;

		assertEquals(wctool.execute(f, "-m testCase_4.txt"), "38");

		wctool5 = new WcTool(null);
		assertEquals(wctool5.execute(f, "-m testCase_5.txt"), "31");
	}

	// test the functionality of getWordCount to count the number of words in a
	// file and the execute to call the method
	@Test
	public void getWordCountTest() throws IOException {
		assertEquals(wctool.getWordCount(readFile("testCase_1.txt", std)), "10");
		assertEquals(wctool.getWordCount(readFile("testCase_2.txt", std)), "4");
		assertEquals(wctool.getWordCount(readFile("testCase_3.txt", std)), "6");
		assertEquals(wctool.getWordCount(readFile("testCase_4.txt", std)), "6");
		assertEquals(wctool.getWordCount(readFile("testCase_5.txt", std)), "5");

		String[] args2 = { "-w", "testCase_1.txt" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null), "10");
		wctool2 = null;

		String[] args3 = { "-w", "testCase_2.txt" };
		wctool3 = new WcTool(args3);
		assertEquals(wctool3.execute(null, null), "4");
		wctool3 = null;

		String[] args4 = { "-w", "testCase_3.txt" };
		wctool4 = new WcTool(args4);
		assertEquals(wctool4.execute(null, null), "6");
		wctool4 = null;

		assertEquals(wctool.execute(f, "-w testCase_4.txt"), "6");

		wctool5 = new WcTool(null);
		assertEquals(wctool5.execute(f, "-w testCase_5.txt"), "5");
	}

	// test the functionality of getNewLineCount to count the number of lines in
	// a file and the execute to call the method
	public void getNewLineCountTest() throws IOException {
		assertEquals(wctool.getNewLineCount(readFile("testCase_1.txt", std)), "5");
		assertEquals(wctool.getNewLineCount(readFile("testCase_2.txt", std)), "4");
		assertEquals(wctool.getNewLineCount(readFile("testCase_3.txt", std)), "6");
		assertEquals(wctool.getNewLineCount(readFile("testCase_4.txt", std)), "6");
		assertEquals(wctool.getNewLineCount(readFile("testCase_5.txt", std)), "5");

		String[] args2 = { "-l", "testCase_1.txt" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null), "5");
		wctool2 = null;

		String[] args3 = { "-l", "testCase_2.txt" };
		wctool3 = new WcTool(args3);
		assertEquals(wctool3.execute(null, null), "4");
		wctool3 = null;

		String[] args4 = { "-l", "testCase_3.txt" };
		wctool4 = new WcTool(args4);
		assertEquals(wctool4.execute(null, null), "6");
		wctool4 = null;

		assertEquals(wctool.execute(f, "-l testCase_4.txt"), "6");

		assertEquals(wctool.execute(f, "-l testCase_5.txt"), "5");
	}

	// test the functionality of printing help message
	@Test
	public void getHelpTest() {
		assertEquals(wctool.getHelp(), "-m : Print only the character counts\t" + " -w : Print only the word counts\t"
				+ " -l : Print only the newline counts\t" + " -help : Brief information about supported options");

		String[] args2 = { "-help" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null), "-m : Print only the character counts\t" + " -w : Print only the word counts\t"
				+ " -l : Print only the newline counts\t" + " -help : Brief information about supported options");
		wctool2 = null;

		assertEquals(wctool.execute(f, "-help"), "-m : Print only the character counts\t" + " -w : Print only the word counts\t"
				+ " -l : Print only the newline counts\t" + " -help : Brief information about supported options");

	}

	// test if the invalid cases are being handled
	@Test
	public void validationTest() {
		String[] args2 = { "-m", "notExist.txt" };
		wctool2 = new WcTool(args2);
		assertEquals(wctool2.execute(null, null), "word count: open failed: notExist.txt: No such file or directory.");
		wctool2 = null;

		String[] args3 = { "-w", "notExist.txt" };
		wctool3 = new WcTool(args3);
		assertEquals(wctool3.execute(null, null), "word count: open failed: notExist.txt: No such file or directory.");
		wctool3 = null;

		String[] args4 = { "-w", "notExist.txt" };
		wctool4 = new WcTool(args4);
		assertEquals(wctool4.execute(null, null), "word count: open failed: notExist.txt: No such file or directory.");
		wctool4 = null;

		String[] args5 = { "-x", "testCase_1.txt" };
		wctool5 = new WcTool(args5);
		assertEquals(wctool5.execute(null, null), "Invalid arguments.");
		wctool5 = null;

		assertEquals(wctool.execute(null, null), "No arguments and no standard input.");
	}
	
	public String readFile(String fileName, Charset encoding) throws IOException {
		try {
			String filePath;
			File f = new File(fileName);
			if (f.isAbsolute())
				filePath = fileName;
			else
				filePath = System.getProperty("user.dir") + File.separator + fileName;
	
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			throw new IOException(fileName);
		}
	}
}