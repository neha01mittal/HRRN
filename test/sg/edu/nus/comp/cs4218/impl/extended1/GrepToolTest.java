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

/**
 * @author Phuoc Truong Hoang
 */
public class GrepToolTest {

	private static File file1;
	private static File file2;

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
		String expected = "              could have inter-word spacing that lined up by accident.\n"
				+ "#Cell Size:#  If you have more than one line (as just above) then\n"
				+ "              you will simply get empty cells where the other column is empty.\n"
				+ "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "\n" + "BORDER\n";
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

	private static void writeToFile(File file, String content) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.println(content);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	private static String readFile(String file, GrepToolTest gtt) throws FileNotFoundException {
		String path = gtt.getClass().getClassLoader().getResource(file).getPath();
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		String content = "";
		try {
			// content += "Reading file: " + toRead.getName() + ": ";
			while ((line = reader.readLine()) != null) {
				content += line + "\n";
			}
			reader.close();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}