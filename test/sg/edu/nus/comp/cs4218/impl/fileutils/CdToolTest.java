package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdToolTest {

	private CdTool			cdTool;
	private Path			rootDirectory;
	private String			rootDirectoryString;
	private List<Path>		testDirectoryList;
	private List<String>	tdRelativeString;
	private List<String>	tdAbsoluteString;
	private String			originalPath;

	@BeforeClass
	public void before() throws IOException {
		// create new dir and files inside
		originalPath = System.getProperty("user.dir");
		rootDirectoryString = System.getProperty("user.dir") + "/cdToolTest";

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
	}

	@AfterClass
	public void after() throws IOException {
		cdTool = null;
		for (int i = testDirectoryList.size() - 1; i >= 0; i--) {
			Files.deleteIfExists(testDirectoryList.get(i));
		}
		System.setProperty("user.dir", originalPath);
		Files.deleteIfExists(rootDirectory);
	}

	@Test
	public void testCdWithNoArguments() {
		cdTool = new CdTool(null);
		cdTool.execute(new File(rootDirectoryString), "");

		// should return an error
		assertEquals(1, cdTool.getStatusCode());
	}

	@Test
	public void testCdWithInvalidArguments() {
		String[] args = new String[] { "invalid" };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), "");

		// should return an error
		assertEquals(1, cdTool.getStatusCode());
	}

	@Test
	public void testCdWithRelativePath() {
		String[] args = new String[] { tdRelativeString.get(0) };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), "");

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(tdAbsoluteString.get(0).toLowerCase(), System.getProperty("user.dir").toLowerCase());
	}

	@Test
	public void testCdWithAbsolutePath() {
		String[] args = new String[] { tdAbsoluteString.get(0) };
		cdTool = new CdTool(args);
		cdTool.execute(new File(rootDirectoryString), "");

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(tdAbsoluteString.get(0).toLowerCase(), System.getProperty("user.dir").toLowerCase());
	}

	@Test
	public void testCdWithDoubleDotNotation() {
		String[] args = new String[] { "../.." };
		cdTool = new CdTool(args);
		cdTool.execute(new File(tdAbsoluteString.get(2)), "");

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(tdAbsoluteString.get(0).toLowerCase(), System.getProperty("user.dir").toLowerCase());
	}

	@Test
	public void testCdWithSingleDotNotation() {
		String[] args = new String[] { "." };
		cdTool = new CdTool(args);
		cdTool.execute(new File(tdAbsoluteString.get(0)), "");

		assertEquals(0, cdTool.getStatusCode());
		assertEquals(tdAbsoluteString.get(0).toLowerCase(), System.getProperty("user.dir").toLowerCase());
	}

	@Test
	public void testChangeDirectoryWithAbsolutePath() {
		cdTool = new CdTool(null);

		// change to the testing directory
		File result = cdTool.changeDirectory(tdAbsoluteString.get(0));

		assertEquals(new File(tdAbsoluteString.get(0)), result);
	}
}
