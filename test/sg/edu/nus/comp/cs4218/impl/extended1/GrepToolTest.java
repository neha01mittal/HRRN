package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GrepToolTest {

	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<String> testFileListRelativeString;
	private static List<String> testFileListAbsoluteString;
	private static List<File> testDirectories;
	private static final String[] extensions = { ".txt", ".doc" };

	@BeforeClass
	public static void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/grepToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);
		testFileListRelativeString = new ArrayList<String>();
		testFileListAbsoluteString = new ArrayList<String>();
		testDirectories = new ArrayList<File>();

		String dirPath = "";

		for (int i = 0; i < 2; i++) {
			try {
				dirPath += "level-" + i;

				Path temp = FileSystems.getDefault().getPath(rootDirectoryString + "/" + dirPath);
				Files.createDirectory(temp);
				File f = new File(temp + File.separator + "test" + i + extensions[i]);
				f.createNewFile();
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
					out.println("I am present in file " + i + "\nThis is second (ine of #$ file");
					out.close();
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
				testDirectories.add(temp.toFile());
				testFileListRelativeString.add(dirPath + File.separator + "test" + i + extensions[i]);
				testFileListAbsoluteString.add(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@AfterClass
	public static void after() throws IOException {
		// catTool = null;

		for (int i = 0; i < testFileListAbsoluteString.size(); i++) {
			Path path = Paths.get(testFileListAbsoluteString.get(i));
			Files.deleteIfExists(path);
		}

		for (int i = 0; i < testDirectories.size(); i++) {
			Files.deleteIfExists(testDirectories.get(i).toPath());
		}
		Files.deleteIfExists(rootDirectory);
	}

	@Test
	public void test_01() {
		String input = "grep -A 2 \"temp\" " + testFileListAbsoluteString.get(0);

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[2] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.Parse();
		assertEquals(parsed.size(), 1);

		String pattern = gt.GetPatternFromInput();
		assertEquals("temp", pattern);

		Vector<String> fileList = gt.GetFileListFromInput();
		assertEquals(fileList.size(), 1);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "\n" + "BORDER\n";
		assertEquals(expected, result);
	}

	@Test
	public void test_02() {
		String input = "grep -A 2 -B 3 \"temp\" file1.txt file2.txt file3.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[4] = "temp";

		GrepTool gt = new GrepTool(args);
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.Parse();
		assertEquals(parsed.size(), 2);

		String pattern = gt.GetPatternFromInput();
		assertEquals(pattern, "temp");

		Vector<String> fileList = gt.GetFileListFromInput();
		assertEquals(fileList.size(), 3);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		// System.out.println(result);
		String expected = "              could have inter-word spacing that lined up by accident.\n"
				+ "#Cell Size:#  If you have more than one line (as just above) then\n"
				+ "              you will simply get empty cells where the other column is empty.\n"
				+ "#Alignment:#  Alignment of cells is attempted to be preserved.\n" + "\n" + "BORDER\n";
		assertEquals(expected, result);
	}

	@Test
	public void test_03() {
		String input = "grep \"\\^A\" file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[0] = "\\^A";

		GrepTool gt = new GrepTool(args);
		Map<String, ArrayList<String>> parsed = gt.Parse();
		assertEquals(0, parsed.size());

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "^A^A^A\n" + "B^A\n" + "BBB^A\n";
		assertEquals(expected, result);
	}

	@Test
	public void test_04() {
		String input = "grep -c \"^A\" file2.txt";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
		args[1] = "^A";

		GrepTool gt = new GrepTool(args);

		String result = gt.execute(new File(System.getProperty("user.dir")), "");
		String expected = "4";
		assertEquals(expected, result);
	}
}