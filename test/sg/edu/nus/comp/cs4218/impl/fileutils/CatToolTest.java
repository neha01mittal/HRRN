package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

/**
 * @usage cat [ - ] [string | path]
 * @options Cat [filename] contents of file Cat [filename1] [filename2] contents
 *          of both files Cat - “text text” output- text text, Anything preceded
 *          by ‘-’ is printed as it is
 * @note If files extensions are such that they not readable properly (eg: pdf),
 *       it might display garbage values. ls | cat and ls | cat - will print as
 *       expected (contents of that directory or in other words standard input
 *       it receives from the ‘from’ tool). Prioritizes args over stdin i.e. if
 *       there are args in front, it will execute them and ignore stdin eg: ls |
 *       cat file1.txt will print the contents of file1 and not the contents of
 *       current directory@note
 * @success
 * @exceptions
 **/
public class CatToolTest {

	private CatTool					catTool;
	private static Path				rootDirectory;
	private static String			rootDirectoryString;
	private static List<String>		testFileListRelativeString;
	private static List<String>		testFileListAbsoluteString;
	private static File				root;
	private static List<File>		testDirectories;
	private static final String[]	EXTENSIONS	= { ".txt", ".doc" };

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
				File f = new File(temp + File.separator + "test" + i + EXTENSIONS[i]);
				f.createNewFile();
				try {
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
					out.println("I am present in file " + i + "\nThis is second (ine of #$ file");
					out.close();
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
				testDirectories.add(temp.toFile());
				testFileListRelativeString.add(dirPath + File.separator + "test" + i + EXTENSIONS[i]);
				testFileListAbsoluteString.add(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TestUtils.delete(new File(rootDirectoryString));
	}

	@After
	public void after() throws IOException {
		catTool = null;
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
	public void catWithStdin() {
		String stdin = "This is stdin text";
		catTool = new CatTool(null);
		String actualOutput = catTool.execute(root, stdin);
		assertEquals(0, catTool.getStatusCode());
		assertTrue(stdin.equals(actualOutput));
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
		String expectedOutput = "Error: No such file or directory\n";
		expectedOutput += expectedOutput;
		catTool = new CatTool(filePath);
		String fileContent = catTool.execute(root, null);

		// assertNotEquals(0, catTool.getStatusCode());
		assertEquals(expectedOutput, fileContent);
	}

	@Test
	public void catWithNoArgs() {
		catTool = new CatTool(null);
		catTool.execute(root, null);
		assertNotEquals(0, catTool.getStatusCode());
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
