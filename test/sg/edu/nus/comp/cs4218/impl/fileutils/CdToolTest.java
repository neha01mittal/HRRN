package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
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
 * @author Zhang Haoqiang
 */
public class CdToolTest {

	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<Path> testDirectoryList;
	private static List<String> tdRelativeString;
	private static List<String> tdAbsoluteString;
	private static String originalPath;
	private static File testFile;
	private CdTool cdTool;

	@BeforeClass
	public static void before() throws IOException {
		// create new dir and files inside
		originalPath = System.getProperty("user.dir");
		rootDirectoryString = System.getProperty("user.dir") + File.separator + "cdToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		tdRelativeString = new ArrayList<String>();
		tdAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 3; i++) {
			try {
				dirPath += "/level-" + i;
				Path temp = FileSystems.getDefault().getPath(rootDirectoryString + dirPath);
				Files.createDirectory(temp);
				testDirectoryList.add(temp);
				tdRelativeString.add(dirPath.substring(1));
				tdAbsoluteString.add(rootDirectoryString + dirPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			String filePath = rootDirectoryString + "/testFile";
			testFile = new File(filePath);
			testFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TestUtils.delete(new File(rootDirectoryString));
		System.setProperty("user.dir", originalPath);
	}

	@After
	public void after() throws IOException {
		cdTool = null;
	}

	@Test
	public void testCdWithInvalidArguments() {
		// Test error-handling 1
		// Reference non-existing file
		String[] args = new String[] { "invalid" };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), null);

		assertNotEquals(0, cdTool.getStatusCode());
	}

	@Test
	public void testCdWithNoArguments() {
		// Test error-handling 2
		cdTool = new CdTool(null);
		cdTool.execute(new File(rootDirectoryString), null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(normalizePath(System.getProperty("user.home")), normalizePath(System.getProperty("user.dir")));
	}

	@Test
	public void testCdWithAFile() {
		// Test error-handling 3
		String[] args = new String[] { rootDirectoryString + "/testFile" };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), null);

		assertNotEquals(0, cdTool.getStatusCode());
	}

	@Test
	public void testCdWithStdin() {
		String[] args = null;
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), tdRelativeString.get(0));

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(normalizePath(System.getProperty("user.home")), normalizePath(System.getProperty("user.dir")));
	}

	@Test
	public void testCdWithRelativePath() {
		String[] args = new String[] { tdRelativeString.get(0) };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(normalizePath(tdAbsoluteString.get(0)), normalizePath(System.getProperty("user.dir")));
	}

	@Test
	public void testCdWithAbsolutePath() {
		String[] args = new String[] { tdAbsoluteString.get(0) };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(normalizePath(tdAbsoluteString.get(0)), normalizePath(System.getProperty("user.dir")));
	}

	@Test
	public void testCdWithDoubleDotNotation() {
		String[] args = new String[] { "../.." };
		cdTool = new CdTool(args);
		cdTool.execute(new File(tdAbsoluteString.get(2)), null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(normalizePath(tdAbsoluteString.get(0)), normalizePath(System.getProperty("user.dir")));
	}

	@Test
	public void testCdWithSingleDotNotation() {
		String[] args = new String[] { "." };
		cdTool = new CdTool(args);
		cdTool.execute(new File(tdAbsoluteString.get(0)), null);

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(normalizePath(tdAbsoluteString.get(0)), normalizePath(System.getProperty("user.dir")));
	}

	@Test
	public void testChangeDirectoryWithAbsolutePath() {
		cdTool = new CdTool(null);

		// change to the testing directory
		File result = cdTool.changeDirectory(tdAbsoluteString.get(0));

		assertEquals(new File(tdAbsoluteString.get(0)), result);
	}

	public String normalizePath(String input) {
		return input.replaceAll("\\\\", "/").toLowerCase();
	}
}
