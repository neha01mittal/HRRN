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
public class MyFunctionsGrepToolTest {

	private static File file1;
	private static File file2;

	@BeforeClass
	public static void before() throws IOException {
		// create file1.txt and file2.txt in current directory
		MyFunctionsGrepToolTest gtt = new MyFunctionsGrepToolTest();
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

	private static void writeToFile(File file, String content) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
			out.print(content);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	private static String readFile(String file, MyFunctionsGrepToolTest gtt) throws FileNotFoundException {
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