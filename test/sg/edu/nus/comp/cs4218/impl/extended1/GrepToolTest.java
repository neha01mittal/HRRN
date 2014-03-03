package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import org.junit.AfterClass;
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
 *          of output context grep -c “pattern” filename : Print a count of
 *          matching lines with pattern grep -c ‘“pattern”’ filename : Print a
 *          count of matching lines with “pattern”(pattern surrounded by double
 *          quotes) grep -c “pattern” file1 file2 : Print a count of matching
 *          lines containing pattern for both files grep -o “pattern” filename:
 *          Show only the part of a matching line that matches PATTERN grep -v
 *          “pattern” filename: Select non-matching (instead of matching) lines
 *          grep -help : Brief information about supported options grep -o -v
 *          “pattern filename: Provides the conjunction of results from both
 *          options grep -<any option> ‘pattern’ filename: Provides the same
 *          output as compared to pattern surrounded by double quotes grep -<any
 *          option> <pattern with one word> filename: Provides the same output
 *          even without surrounding quotes if the pattern consists of one word
 *          grep -<any option> “pattern” file1 file2: Provides the output after
 *          executing the command on both files grep filename: Prints the
 *          command since no option was provided
 * @note
 * @success
 * @exceptions
 * 
 */
public class GrepToolTest {

	private static File	file1;
	private static File	file2;

	@BeforeClass
	public static void before() throws IOException {
		// create file1.txt and file2.txt in current directory
		GrepToolTest gtt = new GrepToolTest();
		String curDir = System.getProperty("user.dir");
		file1 = new File(curDir, "file1.txt");
		file2 = new File(curDir, "file2.txt");
		String content = readFile("file1.txt", gtt);
		writeToFile(file1, content);
		String content2 = readFile("file2.txt", gtt);
		writeToFile(file2, content2);
	}

	@AfterClass
	public static void after() throws IOException {
		file1.delete();
		file2.delete();
	}

	@Test
	public void testSingleOption() {
		String input = "grep -A 2 \"temp\" file1.txt";

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

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "\n" + "BORDER\n";
		assertEquals(expected, result);
	}

	@Test
	public void testMultipleOptions() {
		String input = "grep -A 2 -B 3 \"temp\" file1.txt file2.txt";

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

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		// System.out.println(result);
		String expected = "file1.txt:               could have inter-word spacing that lined up by accident.\n"
				+ "file1.txt: #Cell Size:#  If you have more than one line (as just above) then\n"
				+ "file1.txt:               you will simply get empty cells where the other column is empty.\n"
				+ "file1.txt: #Alignment:#  Alignment of cells is attempted to be preserved.\n" + "file1.txt: \n" + "file1.txt: BORDER\n";
		assertEquals(expected, result);
	}

	@Test
	public void testRegexPattern() {
		String input = "grep \"\\^A\" file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[0] = "\\^A";

		GrepTool gt = new GrepTool(args);
		Map<String, ArrayList<String>> parsed = gt.parse();
		assertEquals(0, parsed.size());

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "^A^A^A\n" + "B^A\n" + "BBB^A\n";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLines() {
		String input = "grep -c \"^A\" file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "^A";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "4";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesForTwoFiles() {
		String input = "grep -c \"A\" file1.txt file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "A";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "file1.txt: 23\n" + "file2.txt: 9\n";
		// String expected = "32";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesWhichContainPattern() {
		String input = "grep -c \"This has\" file1.txt file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 2, tokens.length);
		args[0] = "-c";
		args[1] = "This has";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "file1.txt: 1\n" + "file2.txt: 0\n";
		// String expected = "1";
		assertEquals(expected, result);
	}

	@Test
	public void testCountLinesWhichDoNotContainPattern() {

		// Does not work.. It only handles -c
		String input = "grep -c -v \"This\" file1.txt file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "This";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "file1.txt: 294\n" + "file2.txt: 19\n";
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

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "0";
		assertEquals(expected, result);
	}

	@Test
	public void testGrepWithNoOptions() {

		// Does not work.. It only handles -c
		String input = "grep file1.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		// args[1] = "";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), input);
		assertEquals("grep file1.txt\n", result);
	}

	@Test
	public void testOnlyGrep() {

		String input = "grep";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		// args[1] = "";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), input);
		assertEquals("grep\n", result);
	}

	@Test
	public void testGrepWithAbsolutePath() {

		File f = new File("file1.txt");
		String input = "grep -c \"A\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "A";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		assertEquals("23", result);
	}

	@Test
	public void testGrepToolOptionA() {
		File f = new File("file1.txt");
		String input = "grep -A 2 \"reserve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "reserve";

		GrepTool gt = new GrepTool(args);
		String fileContent = "";
		try {
			fileContent = readFile("file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesWithTrailingContext(2, "reserve", fileContent);
		String expected = "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "\n" + "BORDER\n"
				+ "A. I would like to be able to preserve lettered lists, that is:\n"
				+ "   a) recognise that they are letters and not numbers (which it already\n" + "      does)\n"
				+ "   b) display the correct OL properties with CSS so as to preserve\n" + "      that information.\n" + "\n";
		// System.out.println("TEST A: " + result);
		// System.out.println("TEST A expected: " + expected);
		assertEquals(expected, result);
	}

	@Test
	public void testOptionB() {
		File f = new File("file1.txt");
		String input = "grep -B 2 \"reserve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "reserve";

		GrepTool gt = new GrepTool(args);
		String fileContent = "";
		try {
			fileContent = readFile("file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesWithLeadingContext(2, "reserve", fileContent);
		String expected = "#Cell Size:#  If you have more than one line (as just above) then\n"
				+ "              you will simply get empty cells where the other column is empty.\n"
				+ "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "I would like to implement.\n" + "\n"
				+ "A. I would like to be able to preserve lettered lists, that is:\n"
				+ "   a) recognise that they are letters and not numbers (which it already\n" + "      does)\n"
				+ "   b) display the correct OL properties with CSS so as to preserve\n";
		assertEquals(expected, result);
	}

	@Test
	public void testOptionC() {
		File f = new File("file1.txt");
		String input = "grep -B 2 \"reserve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		assert args != null;
		args[2] = "reserve";

		GrepTool gt = new GrepTool(args);
		assert gt != null;
		String fileContent = "";
		try {
			fileContent = readFile("file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesWithLeadingContext(2, "reserve", fileContent);
		String expected = "#Cell Size:#  If you have more than one line (as just above) then\n"
				+ "              you will simply get empty cells where the other column is empty.\n"
				+ "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "I would like to implement.\n" + "\n"
				+ "A. I would like to be able to preserve lettered lists, that is:\n"
				+ "   a) recognise that they are letters and not numbers (which it already\n" + "      does)\n"
				+ "   b) display the correct OL properties with CSS so as to preserve\n";

		assertEquals(expected, result);
	}

	@Test
	public void testCountOption() {
		File f = new File("file2.txt");
		String input = "grep -v \"A\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		assert args != null;
		args[1] = "A";

		GrepTool gt = new GrepTool(args);
		assert gt != null;
		String fileContent = "";
		try {
			fileContent = readFile("file2.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getNonMatchingLines("A", fileContent);
		String expected = "\n" + "\n" + "\n" + "C D\n" + "\n" + "B\n" + "\n" + "\n" + "\n" + "\n";
		assertEquals(expected, result);
	}

	@Test
	public void testGetMatchingPart() {
		File f = new File("file1.txt");
		String input = "grep -c \"p.*ve\" " + f.getAbsolutePath();

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		assert args != null;
		args[1] = "p.*ve";

		GrepTool gt = new GrepTool(args);
		assert gt != null;
		String fileContent = "";
		try {
			fileContent = readFile("file1.txt", this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String result = gt.getMatchingLinesOnlyMatchingPart("p.*ve", fileContent);
		String expected = "ple Conve\n" + "ptions to conve\n" + "p ve\n" + "people are usually conve\n" + "pretty decent about figuring out which leve\n"
				+ "ppend a file automatically to all conve\n" + "paragraphs and list items have\n" + "pted to be preserve\n" + "preserve\n"
				+ "play the correct OL properties with CSS so as to preserve\n";
		assertEquals(expected, result);
	}

	@Test
	public void testPipingWithGrepFullOption() {
		String input = "cat file1.txt | grep \"A\" file2.txt";
		String[] tokens = input.split(" ");

		String[] catArgs = Arrays.copyOfRange(tokens, 1, 1);
		CatTool ct = new CatTool(catArgs);
		String catToolOutput = ct.execute(new File(System.getProperty("user.dir")), "");

		String[] grepArgs = Arrays.copyOfRange(tokens, 4, tokens.length);
		grepArgs[0] = "A";

		GrepTool gt = new GrepTool(grepArgs);

		String result = gt.execute(new File(System.getProperty("user.dir")), catToolOutput);
		String expected = "^A^A^A\n" + "A\n" + "AA\n" + "A B\n" + "A\n" + "BAA\n" + "BBAA\n" + "B^A\n" + "BBB^A\n";
		assertEquals(expected, result);
	}

	private static void writeToFile(File file, String content) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.print(content);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	private static String readFile(String file, GrepToolTest gtt) throws FileNotFoundException {
		String path = gtt.getClass().getClassLoader().getResource(file).getPath();
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		StringBuilder content = new StringBuilder();
		try {
			// content += "Reading file: " + toRead.getName() + ": ";
			while ((line = reader.readLine()) != null) {
				content.append(line + "\n");
			}
			reader.close();
			return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}