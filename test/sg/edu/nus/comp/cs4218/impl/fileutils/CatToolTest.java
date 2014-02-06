package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Neha Mittal
 */
public class CatToolTest {

	private CatTool catTool;
	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<String> testFileListRelativeString;
	private static List<String> testFileListAbsoluteString;
	private static File root;
	private static List<File> testDirectories;
	private static final String[] extensions = { ".txt", ".doc" };

	@BeforeClass
	public static void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/catToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);
		root = new File(rootDirectoryString);
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
	public void catWithAbsolutePath() {
		for (int i = 0; i < 2; i++) {
			String[] filePath = { testFileListAbsoluteString.get(i) };
			String expectedOutput = "";
			catTool = new CatTool(filePath);
			File f = new File(filePath[0]);
			String fileContent = catTool.execute(root, null);

			assertEquals(0, catTool.getStatusCode());

			expectedOutput = readFile(f);
			assertEquals(fileContent, expectedOutput);
		}
	}

	@Test
	public void catWithRelativePath() {
		for (int i = 0; i < 2; i++) {
			String[] filePath = { testFileListRelativeString.get(0) };
			String expectedOutput = "";
			catTool = new CatTool(filePath);
			File f = new File(testFileListAbsoluteString.get(0));
			String fileContent = catTool.execute(root, null);

			assertEquals(catTool.getStatusCode(), 0);

			expectedOutput = readFile(f);
			assertEquals(fileContent, expectedOutput);
		}
	}

	@Test
	public void catWithMultipleArgs() {
		// reads the first file, ignores the rest
		String[] filePath = { testFileListRelativeString.get(0), testFileListRelativeString.get(1) };
		String expectedOutput = "";
		String expectedOutput2 = "";
		catTool = new CatTool(filePath);
		File f = new File(testFileListAbsoluteString.get(0));
		File f1 = new File(testFileListAbsoluteString.get(1));
		String fileContent = catTool.execute(root, null);

		assertEquals(0, catTool.getStatusCode());

		expectedOutput = readFile(f);
		expectedOutput2 = readFile(f1);
		expectedOutput += expectedOutput2;
		assertEquals(expectedOutput, fileContent);
	}

	@Test
	public void catWithInvalidArgs() {
		// reads the first file, ignores the rest
		String[] filePath = { "rubbish", "NonExisitingFile" };
		String expectedOutput = "cat: No such file exists\n";
		expectedOutput += expectedOutput;
		catTool = new CatTool(filePath);
		String fileContent = catTool.execute(root, null);

		assertNotEquals(0, catTool.getStatusCode());
		assertEquals(expectedOutput, fileContent);
	}

	@Test
	public void catWithNoArgs() {
		String expectedOutput = "";
		catTool = new CatTool(null);
		expectedOutput = null;
		catTool.execute(root, null);

		assertNotEquals(0, catTool.getStatusCode());
		assertEquals(null, expectedOutput);
	}

	@Test
	public void getStringForFileTest() {
		// reads the first file, ignores the rest
		String[] filePath = {};
		String expectedOutput = "";
		catTool = new CatTool(filePath);
		File f = new File(testFileListAbsoluteString.get(0));
		String fileContent = catTool.getStringForFile(f);
		expectedOutput = readFile(f);

		assertEquals(fileContent, expectedOutput);
	}

	private String readFile(File f) {
		String expectedOutput = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				expectedOutput += line + "\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expectedOutput;
	}
}
