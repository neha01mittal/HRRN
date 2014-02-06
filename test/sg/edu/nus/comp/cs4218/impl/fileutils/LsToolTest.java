package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

/**
 * @author Zhang Haoqiang
 */
public class LsToolTest {

	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<Path> testFileList;
	private static int totalVisibleFile;

	@BeforeClass
	public static void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/lsToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testFileList = new ArrayList<Path>();

		for (int i = 0; i < 10; i++) {
			try {
				String filePath = rootDirectoryString + "/testFile-" + i + "";
				Path temp = FileSystems.getDefault().getPath(filePath);
				Files.createFile(temp);
				testFileList.add(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		totalVisibleFile = 5;
		for (int i = 0; i < totalVisibleFile; i++) {
			try {
				String filePath = rootDirectoryString + "/.testFile-" + i + "";
				Path temp = FileSystems.getDefault().getPath(filePath);
				Files.createFile(temp);
				testFileList.add(temp);

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
	}

	@AfterClass
	public static void after() throws IOException {
		for (int i = 0; i < testFileList.size(); i++) {
			Files.deleteIfExists(testFileList.get(i));
		}
		Files.deleteIfExists(rootDirectory);
	}

	@Test
	public void testNoArgument() {
		String[] args = null;
		LsTool lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(testFileList.size() - totalVisibleFile, resultArray.length);

		// check for filenames
		for (String currentFile : resultArray) {
			assertTrue(testFileList.contains(FileSystems.getDefault().getPath(rootDirectoryString + "/" + currentFile)));
		}
	}

	@Test
	public void testWithArgumentShowAll() {
		String[] args = new String[] { "-a" };
		LsTool lsTool = new LsTool(args);

		String result = lsTool.execute(new File(rootDirectoryString), null);
		String[] resultArray = result.split("\n");

		assertEquals(0, lsTool.getStatusCode());

		// check for the number of files returned
		assertEquals(resultArray.length, testFileList.size());

		// check for filenames
		for (String currentFile : resultArray) {
			assertTrue(testFileList.contains(FileSystems.getDefault().getPath(rootDirectoryString + "/" + currentFile)));
		}
	}
}
