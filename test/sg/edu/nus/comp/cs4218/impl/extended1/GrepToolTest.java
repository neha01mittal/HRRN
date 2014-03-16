package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;

/**
 * Searches for pattern in a file
 * 
 * @usage grep [-A | -B | -C | -c | -o | -v | -help] pattern [path]
 * @options grep -A NUM filename: Print NUM lines of trailing context after
 *          matching lines grep -B NUM filename: Print NUM lines of leading
 *          context before matching lines grep -C NUM filename: Print NUM lines
 *          of output context grep -c ���pattern��� filename : Print a count of
 *          matching lines with pattern grep -c ������pattern������ filename : Print a
 *          count of matching lines with ���pattern���(pattern surrounded by double
 *          quotes) grep -c ���pattern��� file1 file2 : Print a count of matching
 *          lines containing pattern for both files grep -o ���pattern��� filename:
 *          Show only the part of a matching line that matches PATTERN grep -v
 *          ���pattern��� filename: Select non-matching (instead of matching) lines
 *          grep -help : Brief information about supported options grep -o -v
 *          ���pattern filename: Provides the conjunction of results from both
 *          options grep -<any option> ���pattern��� filename: Provides the same
 *          output as compared to pattern surrounded by double quotes grep -<any
 *          option> <pattern with one word> filename: Provides the same output
 *          even without surrounding quotes if the pattern consists of one word
 *          grep -<any option> ���pattern��� file1 file2: Provides the output after
 *          executing the command on both files grep filename: Prints the
 *          command since no option was provided
 * @note
 * @success
 * @exceptions
 */
public class GrepToolTest {

	private static String	originalDirString;

	@BeforeClass
	public static void beforeClass() {
		originalDirString = System.getProperty("user.dir");
	}

	@Before
	public void before() {
	}

	@Test
	public void testSingleOption() {
		String input = "grep -A 2 \"temp\" data/unitTest/file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.parse();
		assertEquals(parsed.size(), 1);

		String pattern = gt.getPatternFromInput();
		assertEquals("temp", pattern);

		Vector<String> fileList = gt.getFileListFromInput();
		assertEquals(fileList.size(), 1);

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "#Alignment:#  Alignment of cells is attempted to be preserved.\n"
				+ "\n" + "BORDER";
		assertEquals(expected, result);
	}

	@Test
	public void testMultipleOptions() {
		String input = "grep -A 2 -B 3 \"temp\" data/unitTest/file1.txt data/unitTest/file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[4] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.parse();
		assertEquals(parsed.size(), 2);

		String pattern = gt.getPatternFromInput();
		assertEquals(pattern, "temp");

		Vector<String> fileList = gt.getFileListFromInput();
		assertEquals(fileList.size(), 2);

		String result = gt
				.execute(new File(originalDirString), "");
		// System.out.println(result);
		String expected = "data/unitTest/file1.txt:               could have inter-word spacing that lined up by accident.\n"
				+ "data/unitTest/file1.txt: #Cell Size:#  If you have more than one line (as just above) then\n"
				+ "data/unitTest/file1.txt:               you will simply get empty cells where the other column is empty.\n"
				+ "data/unitTest/file1.txt: #Alignment:#  Alignment of cells is attempted to be preserved.\n"
				+ "data/unitTest/file1.txt: \n" + "data/unitTest/file1.txt: BORDER";
		assertEquals(expected, result);
	}

	@Test
	public void testRegexPattern() {
		String input = "grep \"\\^A\" data/unitTest/file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[0] = "\\^A";

		GrepTool gt = new GrepTool(args);
		Map<String, ArrayList<String>> parsed = gt.parse();
		assertEquals(0, parsed.size());

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "^A^A^A\n" + "B^A\n" + "BBB^A";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLines() {
		String input = "grep -c \"^A\" data/unitTest/file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "^A";

		GrepTool gt = new GrepTool(args);

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "4";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesForTwoFiles() {
		String input = "grep -c \"A\" data/unitTest/file1.txt data/unitTest/file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "A";

		GrepTool gt = new GrepTool(args);

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "data/unitTest/file1.txt: 23\n" + "data/unitTest/file2.txt: 9";
		// String expected = "32";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesWhichContainPattern() {
		String input = "grep -c \"This has\" data/unitTest/file1.txt data/unitTest/file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 2, tokens.length);
		args[0] = "-c";
		args[1] = "This has";

		GrepTool gt = new GrepTool(args);

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "data/unitTest/file1.txt: 1\n" + "data/unitTest/file2.txt: 0";
		// String expected = "1";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesWhichDoNotContainPattern() {

		// Does not work.. It only handles -c
		String input = "grep -c -v \"This\" data/unitTest/file1.txt data/unitTest/file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "This";

		GrepTool gt = new GrepTool(args);

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "data/unitTest/file1.txt: 294\n" + "data/unitTest/file2.txt: 19";
		// String expected = "313";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesInvalidFile() {

		// Does not work.. It only handles -c
		String input = "grep -c \"This\" filex.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "This";

		GrepTool gt = new GrepTool(args);

		String result = gt
				.execute(new File(originalDirString), "");
		String expected = "0";
		assertEquals(expected, result);
	}

	@Test
	public void testGrepWithNoOptions() {

		// Does not work.. It only handles -c
		String input = "grep data/unitTest/file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		// args[1] = "";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(originalDirString),
				input);
		assertEquals("grep data/unitTest/file1.txt", result);
	}

	@Test
	public void testOnlyGrep() {

		String input = "grep";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		// args[1] = "";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(originalDirString),
				input);
		assertEquals("Invalid command", result);
	}

	@Test
	public void testGrepWithAbsolutePath() {

		File f = new File("data/unitTest/file1.txt");
		String input = "grep -c \"A\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "A";

		GrepTool gt = new GrepTool(args);

		String result = gt
				.execute(new File(originalDirString), "");
		assertEquals("23", result);
	}

	@Test
	public void testGrepToolOptionA() {
		File f = new File("data/unitTest/file1.txt");
		String input = "grep -A 2 \"reserve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "reserve";

		GrepTool gt = new GrepTool(args);
		String fileContent = "";
		try {
			fileContent = readFile("data/unitTest/file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesWithTrailingContext(2, "reserve",
				fileContent);
		String expected = "#Alignment:#  Alignment of cells is attempted to be preserved.\n"
				+ "\n"
				+ "BORDER\n"
				+ "A. I would like to be able to preserve lettered lists, that is:\n"
				+ "   a) recognise that they are letters and not numbers (which it already\n"
				+ "      does)\n"
				+ "   b) display the correct OL properties with CSS so as to preserve\n"
				+ "      that information.\n";
		assertEquals(expected, result);
	}

	@Test
	public void testOptionB() {
		File f = new File("data/unitTest/file1.txt");
		String input = "grep -B 2 \"reserve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "reserve";

		GrepTool gt = new GrepTool(args);
		String fileContent = "";
		try {
			fileContent = readFile("data/unitTest/file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesWithLeadingContext(2, "reserve",
				fileContent);
		String expected = "#Cell Size:#  If you have more than one line (as just above) then\n"
				+ "              you will simply get empty cells where the other column is empty.\n"
				+ "#Alignment:#  Alignment of cells is attempted to be preserved.\n"
				+ "I would like to implement.\n"
				+ "\n"
				+ "A. I would like to be able to preserve lettered lists, that is:\n"
				+ "   a) recognise that they are letters and not numbers (which it already\n"
				+ "      does)\n"
				+ "   b) display the correct OL properties with CSS so as to preserve";
		assertEquals(expected, result);
	}

	@Test
	public void testOptionC() {
		File f = new File("data/unitTest/file1.txt");
		String input = "grep -C 2 \"reserve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		assert args != null;
		args[2] = "reserve";

		GrepTool gt = new GrepTool(args);
		assert gt != null;
		String fileContent = "";
		try {
			fileContent = readFile("data/unitTest/file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesWithOutputContext(2, "reserve",
				fileContent);
		String expected = "#Cell Size:#  If you have more than one line (as just above) then\n"
				+ "              you will simply get empty cells where the other column is empty.\n"
				+ "#Alignment:#  Alignment of cells is attempted to be preserved.\n"
				+ "\n"
				+ "BORDER\n"
				+ "I would like to implement.\n"
				+ "\n"
				+ "A. I would like to be able to preserve lettered lists, that is:\n"
				+ "   a) recognise that they are letters and not numbers (which it already\n"
				+ "      does)\n"
				+ "   b) display the correct OL properties with CSS so as to preserve\n"
				+ "      that information.\n";

		assertEquals(expected, result);
	}

	@Test
	public void testCountOption() {
		File f = new File("data/unitTest/file2.txt");
		String input = "grep -v \"A\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		assert args != null;
		args[1] = "A";

		GrepTool gt = new GrepTool(args);
		assert gt != null;
		String fileContent = "";
		try {
			fileContent = readFile("data/unitTest/file2.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getNonMatchingLines("A", fileContent);
		String expected = "\n" + "\n" + "\n" + "C D\n" + "\n" + "B\n" + "\n"
				+ "\n" + "\n" + "\n";
		assertEquals(expected, result);
	}

	@Test
	public void testGetMatchingPart() {
		File f = new File("data/unitTest/file1.txt");
		String input = "grep -c \"p.*ve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		assert args != null;
		args[1] = "p.*ve";

		GrepTool gt = new GrepTool(args);
		assert gt != null;
		String fileContent = "";
		try {
			fileContent = readFile("data/unitTest/file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesOnlyMatchingPart("p.*ve",
				fileContent);
		String expected = "ple Conve\n" + "ptions to conve\n" + "p ve\n"
				+ "people are usually conve\n"
				+ "pretty decent about figuring out which leve\n"
				+ "ppend a file automatically to all conve\n"
				+ "paragraphs and list items have\n" + "pted to be preserve\n"
				+ "preserve\n"
				+ "play the correct OL properties with CSS so as to preserve";
		assertEquals(expected, result);
	}

	@Test
	public void testPipingWithGrepFullOption() {
		String input = "cat file1.txt | grep \"A\" data/unitTest/file2.txt";
		String[] tokens = input.split(" ");

		String[] catArgs = Arrays.copyOfRange(tokens, 1, 2);
		CatTool ct = new CatTool(catArgs);
		String catToolOutput = ct.execute(
				new File(originalDirString), "");

		String[] grepArgs = Arrays.copyOfRange(tokens, 4, tokens.length);
		grepArgs[0] = "A";

		GrepTool gt = new GrepTool(grepArgs);

		String result = gt.execute(new File(originalDirString),
				catToolOutput);
		String expected = "^A^A^A\n" + "A\n" + "AA\n" + "A B\n" + "A\n"
				+ "BAA\n" + "BBAA\n" + "B^A\n" + "BBB^A";
		assertEquals(expected, result);
	}

	@Test
	public void testPipingWithGrepFullOption02() {
		String input = "cat testCase_3.txt | grep -A 2 -B 3 b";
		String[] tokens = input.split(" ");

		String[] catArgs = Arrays.copyOfRange(tokens, 1, 2);
		CatTool ct = new CatTool(catArgs);
		String catToolOutput = ct.execute(
				new File(originalDirString), "");

		String[] grepArgs = Arrays.copyOfRange(tokens, 4, tokens.length);
		grepArgs[4] = "b";

		GrepTool gt = new GrepTool(grepArgs);

		String result = gt.execute(new File(originalDirString),
				catToolOutput);
		String expected = "a\n" + "b\n" + "b\n" + "c\n" + "c";
		assertEquals(expected, result);

		int statusCode = gt.getStatusCode();
		assertEquals(0, statusCode);
	}

	@Test
	public void testGetHelp() {
		GrepTool gt = new GrepTool(null);
		String result = gt.getHelp();
		String expected = "The grep command searches one or more input files \n"
				+ "for lines containing a match to a specified pattern. \n"
				+ "The grep tool must work on all characters in UTF-8 encoding.\n"
				+ "Command Format - grep [OPTIONS] PATTERN [FILE]\n"
				+ "PATTERN - This specifies a regular expression pattern that describes a set of strings\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n"
				+ "OPTIONS\n"
				+ "-A NUM : Print NUM lines of trailing context after matching lines\n"
				+ "-B NUM : Print NUM lines of leading context before matching lines\n"
				+ "-C NUM : Print NUM lines of output context\n"
				+ "-c : Suppress normal output. Instead print a count of matching lines for each input file\n"
				+ "-o : Show only the part of a matching line that matches PATTERN\n"
				+ "-v : Select non-matching (instead of matching) lines\n"
				+ "-help : Brief information about supported options";

		assertEquals(expected, result);
	}

	private static String readFile(String file, GrepToolTest gtt)
			throws FileNotFoundException {
		String path;
		File f = new File(file);
		if (f.isAbsolute())
			path = file;
		else
			path = originalDirString + File.separator + file;
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			String fileContent = Charset.forName("UTF-8").decode(ByteBuffer.wrap(encoded)).toString();
			return fileContent.replace("\r", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Test
	public void testParser() {
		String input = "grep -A 2 \"temp\" file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.parse();
		assertEquals(parsed.size(), 1);
	}

	@Test
	public void testGetFileListFromInput() {
		String input = "grep -A 2 \"temp\" file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		gt.lastOpt = 1;
		Vector<String> fileList = gt.getFileListFromInput();
		assertEquals(fileList.size(), 1);
	}

	@Test
	public void testGetFileListFromMultipleFileInput() {
		String input = "grep -A 2 \"temp\" file1.txt file2.txt file3.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		gt.lastOpt = 1;
		Vector<String> fileList = gt.getFileListFromInput();
		assertEquals(fileList.size(), 3);
	}

	@Test
	public void testGetPatternFromInput() {
		String input = "grep -A 2 \"temp\" file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.lastOpt = 1;
		gt.getCountOfMatchingLines("temp", input);
		String pattern = gt.getPatternFromInput();
		assertEquals("temp", pattern);
	}

	@Test
	public void testGetPatternInDoubleQuotesFromInput() {
		String input = "grep -A 2 \"=\" file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "\"=\"";

		GrepTool gt = new GrepTool(args);
		gt.lastOpt = 1;
		gt.getCountOfMatchingLines("\"=\"", input);
		String pattern = gt.getPatternFromInput();
		assertEquals("\"=\"", pattern);
	}
}