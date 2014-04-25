package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

/**
 * Lists all the files and subdirectories in the current directory or provided
 * class
 * 
 * @usage ls [-a | -R] [path]
 * @options ls : Lists all files in current directory. ls [path] : Lists all
 *          files in the given path. ls -a : Lists all files including hidden
 *          files in current directory. ls -a [path] : Lists all files including
 *          hidden files in the given path ls -R : Lists all the files (absolute
 *          path) in the current directory and subdirectories ls -R [path] :
 *          Lists all the files (absolute path) in the given path. ls -a -R :
 *          Combination of functions above ls file : Prints file path if it
 *          exists in the file system.
 * @note [path] could be either an absolute file or a relative path. If multiple
 *       path is given as arguments, only the first argument will be
 *       entertained. Ls tool does not allow std, thus can not be pipe to. The
 *       recursive variable is printed in absolute path to show the hierarchical
 *       structure.
 * @success return the list of file names or path in the
 * @exceptions invalid input path not exist retrieve file list error
 */
public class LsToolTest {

	private static Path			rootDirectory;
	private static Path			emptyFolder;
	private static String		rootDirectoryString;
	private static List<Path>	testFileList;
	private static List<Path>	testFileListAll;
	private static List<Path>	testFileListLevel0;
	private static List<Path>	testFileListLevel0All;
	private static int			nestedLevel;
	private static int			totalVisibleFile;
	private static int			totalInvisibleFile;
	private LsTool				lsTool;

	@BeforeClass
	public static void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") +  File.separator + "lsToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testFileList = new ArrayList<Path>();
		testFileListAll = new ArrayList<Path>();
		testFileListLevel0 = new ArrayList<Path>();
		testFileListLevel0All = new ArrayList<Path>();

		nestedLevel = 3;
		String subDirString = "";
		for (int j = 0; j < nestedLevel; j++) {
			totalInvisibleFile = 5;
			for (int i = 0; i < totalInvisibleFile; i++) {
				try {
					String filePath = rootDirectoryString + subDirString + File.separator + ".testFile-" + i + "";
					Path temp = FileSystems.getDefault().getPath(filePath);
					Files.createFile(temp);
					testFileListAll.add(temp);
					if (j == 0) {
						testFileListLevel0All.add(temp);
					}
					try {
						Process p = Runtime.getRuntime().exec("attrib +h " + filePath);
						p.waitFor();
					} catch (Exception e) {
						// whatever doesn't matter
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			totalVisibleFile = 10;
			for (int i = 0; i < totalVisibleFile; i++) {
				try {
					String filePath = rootDirectoryString + subDirString + File.separator + "testFile-" + i + "";
					Path temp = FileSystems.getDefault().getPath(filePath);
					Files.createFile(temp);
					testFileListAll.add(temp);
					testFileList.add(temp);
					if (j == 0) {
						testFileListLevel0.add(temp);
						testFileListLevel0All.add(temp);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			subDirString += File.separator + "zlevel-" + j;
			Path tempDir = FileSystems.getDefault().getPath(rootDirectoryString + subDirString);
			Files.createDirectory(tempDir);
			testFileListAll.add(tempDir);
			testFileList.add(tempDir);
			if (j == 0) {
				testFileListLevel0.add(tempDir);
				testFileListLevel0All.add(tempDir);
			} else if (j == nestedLevel - 1) {
				emptyFolder = tempDir;
			}
		}
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TestUtils.delete(new File(rootDirectoryString));
	}

	@After
	public void after() throws IOException {
		lsTool = null;
	}

	@Test
	public void testNoArgument() {
		String[] args = null;
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileListLevel0.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileListLevel0.size(); i++) {
			assertEquals(testFileListLevel0.get(i), FileSystems.getDefault().getPath(rootDirectoryString + File.separator + resultArray[i]));
		}
	}

	@Test
	public void testWithArgumentShowAll() {
		String[] args = new String[] { "-a" };
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileListLevel0All.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileListLevel0All.size(); i++) {
			assertEquals(testFileListLevel0All.get(i), FileSystems.getDefault().getPath(rootDirectoryString + File.separator + resultArray[i]));
		}
	}

	@Test
	public void testInvalidArguments() {
		String input = "ls lalal";

		String[] tokens = input.split(" ");
		String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(System.getProperty("user.dir")), null);
		String expected = "ls: lalal: No such file or directory";
		assertEquals(expected, result);
	}

	@Test
	public void testListFileWithAnotherWorkingDirectory() {
		String[] args = { "testFile-1" };
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String expected = "testFile-1";
		assertEquals(expected, result);
	}

	@Test
	public void testWithArgumentShowRecursive() {
		String[] args = new String[] { "-R" };
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileList.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileList.size(); i++) {
			assertEquals(testFileList.get(i), FileSystems.getDefault().getPath(resultArray[i]));
		}
	}

	@Test
	public void testWithArgumentShowAllShowRecursive() {
		String[] args = new String[] { "-a", "-R" };
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileListAll.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileListAll.size(); i++) {
			assertEquals(testFileListAll.get(i), FileSystems.getDefault().getPath(resultArray[i]));
		}
	}

	@Test
	public void testWithEmptyFolder() {
		lsTool = new LsTool(null);
		String result = lsTool.execute(emptyFolder.toFile(), null);

		// check for the number of files returned
		assertEquals(0, lsTool.getStatusCode());
		assertEquals(null, result);
	}

	@Test
	public void testWithPathToFolder() {
		String[] args = new String[] { rootDirectoryString };
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File("invalid"), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileListLevel0.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileListLevel0.size(); i++) {
			assertEquals(testFileListLevel0.get(i), FileSystems.getDefault().getPath(rootDirectoryString + File.separator + resultArray[i]));
		}
	}

	@Test
	public void testWithPathToInvalidFolder() {
		String[] args = new String[] { "invalid" };
		lsTool = new LsTool(args);
		lsTool.execute(new File(rootDirectoryString), null);

		assertEquals(1, lsTool.getStatusCode());
	}

	@Test
	public void testWithPathToFileRelativePath() {
		String[] args = new String[] { testFileListLevel0.get(0).toFile().getPath() };
		lsTool = new LsTool(args);
		String result = lsTool.execute(new File(rootDirectoryString), null);

		assertEquals(0, lsTool.getStatusCode());
		assertEquals(args[0], result);
	}

	@Test
	public void testWithPathToFileAbsolutePath() {
		String[] args = new String[] { testFileListLevel0.get(0).toFile().getAbsolutePath() };
		lsTool = new LsTool(args);
		String result = lsTool.execute(new File(rootDirectoryString), null);

		assertEquals(0, lsTool.getStatusCode());
		assertEquals(args[0], result);
	}

	@Test
	public void testWithOnlyStdin() {
		lsTool = new LsTool(null);

		String result = lsTool.execute(new File(rootDirectoryString), testFileListLevel0.get(0).toFile().getAbsolutePath());
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileListLevel0.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileListLevel0.size(); i++) {
			assertEquals(testFileListLevel0.get(i), FileSystems.getDefault().getPath(rootDirectoryString + File.separator + resultArray[i]));
		}
	}

	@Test
	public void testWithArgumentsAndStdin() {
		String[] args = new String[] { "-a" };
		lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), testFileListLevel0.get(0).toFile().getAbsolutePath());
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileListLevel0All.size(), resultArray.length);

		// check for filenames
		for (int i = 0; i < testFileListLevel0All.size(); i++) {
			assertEquals(testFileListLevel0All.get(i), FileSystems.getDefault().getPath(rootDirectoryString + File.separator + resultArray[i]));
		}
	}
}
