package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class MyFunctionsMoveToolTest {
	private MoveTool moveTool;
	private Path rootDirectory;
	private String rootDirectoryString;
	private List<Path> testDirectoryList;
	private List<String> testDirectoryListRelativeString;
	private List<String> testDirectoryListAbsoluteString;

	@Before
	public void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") +  File.separator + "moveToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		testDirectoryListRelativeString = new ArrayList<String>();
		testDirectoryListAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 5; i++) {
			try {
				dirPath += "level" + i;

				Path temp = FileSystems.getDefault().getPath(rootDirectoryString +  File.separator + dirPath);
				Files.createDirectory(temp);

				testDirectoryList.add(temp);
				testDirectoryListRelativeString.add(dirPath);
				testDirectoryListAbsoluteString.add(rootDirectoryString +  File.separator +  dirPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@After
	public void after() throws IOException {
		moveTool = null;
		TestUtils.delete(new File(rootDirectoryString));
	}

	@Test
	public void testMoveFile() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) +  File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "test2.txt");
		moveTool.recursivemove(f1, f2);
		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveFileWithRelativePath() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListRelativeString.get(0) +  File.separator + "test.txt");
		create(testDirectoryListRelativeString.get(0) +  File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListRelativeString.get(0) +  File.separator + "test2.txt");
		moveTool.recursivemove(f1, f2);
		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveIntoFileWithSameName() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) +  File.separator + "test1.txt", "something");

		moveTool.recursivemove(f1, f2);

		f2 = new File(testDirectoryListAbsoluteString.get(1) +  File.separator + "test1.txt");

		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveToExistingFolder() {
		moveTool = new MoveTool(null);
		File from = new File(testDirectoryListAbsoluteString.get(1));
		File f1 = new File(testDirectoryListAbsoluteString.get(1) +  File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(1) +  File.separator + "test1.txt", "something");

		File to = new File(testDirectoryListAbsoluteString.get(0));

		moveTool.recursivemove(from, to);

		assert ((!(from.exists())) && to.exists());
		f1 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "level0level1" +  File.separator + "test1.txt");
		f1.delete();
		f1 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "level0level1");
		f1.delete();
	}

	@Test
	public void testMoveToNewFolder() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(1) +  File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(1) +  File.separator + "test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(1) + File.separator + ".." +  File.separator + "newfolder");
		File f2 = new File(testDirectoryListAbsoluteString.get(1)  + File.separator + ".." +  File.separator + "newfolder" + File.separator + "test1.txt");
		moveTool.recursivemove(f1, to);
		assert ((!(f1.exists())) && to.exists());
		f2.delete();
		to.delete();

	}

	// Test moving folder into file
	@Test
	public void testMoveFolderIntoFile() {

		File f1 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) +  File.separator + "test.txt", "something");
		String a[] = { testDirectoryListAbsoluteString.get(0), f1.getPath().toString() };
		moveTool = new MoveTool(a);
		moveTool.recursivemove(rootDirectory.toFile(), f1);
		f1.delete();
		assert (moveTool.getStatusCode() == 1);
	}

	// Test file name with two extensions (.txt.txt)
	@Test
	public void testMoveFileWithTwoExtensions() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "test.txt.txt");
		create(testDirectoryListAbsoluteString.get(0) +  File.separator + "test.txt.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) +  File.separator + "test2.txt");
		moveTool.recursivemove(f1, f2);
		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	public void create(String filename, String content) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
			writer.write(content);
		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

}