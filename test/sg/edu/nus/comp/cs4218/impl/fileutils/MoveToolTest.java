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

public class MoveToolTest {
	private MoveTool		moveTool;
	private Path			rootDirectory;
	private String			rootDirectoryString;
	private List<Path>		testDirectoryList;
	private List<String>	testDirectoryListRelativeString;
	private List<String>	testDirectoryListAbsoluteString;

	@Before
	public void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/moveToolTest";
		System.setProperty("user.dir", rootDirectoryString);

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		testDirectoryListRelativeString = new ArrayList<String>();
		testDirectoryListAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 5; i++) {
			try {
				dirPath += "level" + i;

				Path temp = FileSystems.getDefault().getPath(rootDirectoryString + "/" + dirPath);
				Files.createDirectory(temp);

				testDirectoryList.add(temp);
				testDirectoryListRelativeString.add(dirPath);
				testDirectoryListAbsoluteString.add(rootDirectoryString + "/" + dirPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@After
	public void after() throws IOException {
		moveTool = null;
		for (int i = 0; i < testDirectoryList.size(); i++) {
			Files.deleteIfExists(testDirectoryList.get(i));
		}
		for (int i = 0; i < testDirectoryListAbsoluteString.size(); i++) {
			Path path = Paths.get(testDirectoryListAbsoluteString.get(i));
			Files.deleteIfExists(path);
		}
		Files.deleteIfExists(rootDirectory);
		System.setProperty("user.dir", rootDirectory.getParent().toString());
	}

	@Test
	public void testMoveFile() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		moveTool.move(f1, f2);
		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveFileWithRelativePath() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListRelativeString.get(0) + "//test.txt");
		create(testDirectoryListRelativeString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListRelativeString.get(0) + "//test2.txt");
		moveTool.move(f1, f2);
		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveMultipleFiles() {
		// copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + "//test.txt", testDirectoryListAbsoluteString.get(0) + "//test2.txt",
				testDirectoryListAbsoluteString.get(1) };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(testDirectoryListAbsoluteString.get(1) + "//test.txt");
		File f4 = new File(testDirectoryListAbsoluteString.get(1) + "//test2.txt");
		assert ((!(f1.exists() || f2.exists())) && f3.exists() && f4.exists());
		f3.delete();
		f4.delete();

	}

	@Test
	public void testMoveIntoFileWithSameName() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + "//test1.txt", "something");

		moveTool.move(f1, f2);

		f2 = new File(testDirectoryListAbsoluteString.get(1) + "//test1.txt");

		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveToExistingFolder() {
		moveTool = new MoveTool(null);
		File from = new File(testDirectoryListAbsoluteString.get(1));
		File f1 = new File(testDirectoryListAbsoluteString.get(1) + "//test1.txt");
		create(testDirectoryListAbsoluteString.get(1) + "//test1.txt", "something");

		File to = new File(testDirectoryListAbsoluteString.get(0));

		moveTool.move(from, to);

		assert ((!(from.exists())) && to.exists());
		f1 = new File(testDirectoryListAbsoluteString.get(0) + "/level0level1/test1.txt");
		f1.delete();
		f1 = new File(testDirectoryListAbsoluteString.get(0) + "/level0level1");
		f1.delete();
	}

	@Test
	public void testMoveToNewFolder() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(1) + "//test1.txt");
		create(testDirectoryListAbsoluteString.get(1) + "//test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(1) + "//..//newfolder");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + "//..//newfolder//test1.txt");
		moveTool.move(f1, to);
		assert ((!(f1.exists())) && to.exists());
		f2.delete();
		to.delete();

	}

	public void create(String filename, String content) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "utf-8"));
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