package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertNotEquals;

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
	private MoveTool moveTool;
	private Path rootDirectory;
	private String rootDirectoryString;
	private List<Path> testDirectoryList;
	private List<String> testDirectoryListRelativeString;
	private List<String> testDirectoryListAbsoluteString;

	@Before
	public void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/moveToolTest";

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
		delete(new File(rootDirectoryString));
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

	// Test moving file into same folder
	@Test
	public void testMoveFileIntoSameFolder() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		String a[] = { f1.getPath().toString(), testDirectoryListAbsoluteString.get(0) };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (moveTool.getStatusCode() == 0);
		f1.delete();
	}

	// Test moving folder into file
	@Test
	public void testMoveFolderIntoFile() {

		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		String a[] = { testDirectoryListAbsoluteString.get(0), f1.getPath().toString() };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		f1.delete();
		assert (moveTool.getStatusCode() == 1);
	}

	// Test moving folder to invalid path
	@Test
	public void testMoveFolderIntoInvalidPath() {
		moveTool = new MoveTool(null);
		String a[] = { testDirectoryListAbsoluteString.get(0), "invalid" };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (moveTool.getStatusCode() == 1);
	}

	// Test moving folder into its parent folder (does it replace)
	@Test
	public void testMoveFolderIntoParentFolder() {
		moveTool = new MoveTool(null);
		String a[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(0) + "//.." };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (moveTool.getStatusCode() == 1);

	}

	// Test moving multiple files into a folder
	@Test
	public void testMoveMultipleFiles() {
		// moveTool = new MoveTool(null);
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
		assert (!(f1.exists() || f2.exists()) && f3.exists() && f4.exists());
		f3.delete();
		f4.delete();
	}

	// Test moving multiple files into existing file - the last move operation
	// overwrites old contents
	@Test
	public void testMoveMultipleFilesintoExisitingFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test2.txt", "something2");
		File f = new File(testDirectoryListAbsoluteString.get(1) + "//a.txt");
		create(testDirectoryListAbsoluteString.get(1) + "//a.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + "//test.txt", testDirectoryListAbsoluteString.get(0) + "//test2.txt",
				testDirectoryListAbsoluteString.get(1) + "//a.txt" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (!(f1.exists() || f2.exists()) && f.exists());
		f.delete();
	}

	// Test moving file into invalid path
	@Test
	public void testMoveFileIntoInvalidPath() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		String a[] = { f1.getPath().toString(), testDirectoryListAbsoluteString.get(0) + "//blah//test.txt" };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		f1.delete();

		assertNotEquals(0, moveTool.getStatusCode());
	}

	// Test with no file names (just "move" command)
	@Test
	public void testMoveCommandWithNoArguments() {
		String a[] = {};
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assertNotEquals(0, moveTool.getStatusCode());
	}

	// Test file name with two extensions (.txt.txt)
	@Test
	public void testMoveFileWithTwoExtensions() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		moveTool.move(f1, f2);
		assert ((!(f1.exists())) && f2.exists());
		f2.delete();
	}

	// Test file name with invalid characters
	@Test
	public void testMoveFileWithInvalidCharacters() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test@#22.xt");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		String a[] = { f1.getPath().toString(), f2.getPath().toString() };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (moveTool.getStatusCode() == 1);
	}

	// Test moving multiple files into a new file - move operations happens in
	// pipe
	@Test
	public void testMoveMultipleFilesintoNewFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + "//test.txt", testDirectoryListAbsoluteString.get(0) + "//test2.txt",
				testDirectoryListAbsoluteString.get(1) + "//a.txt" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f = new File(testDirectoryListAbsoluteString.get(1) + "//a.txt");
		assert (!(f1.exists() || f2.exists()) && f.exists());
		f.delete();
	}

	// Test moving multiple folders into a file -Test if it creates a folder
	// with destination file name
	@Test
	public void testMoveMultipleFoldersintoFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + "//test2.txt");
		create(testDirectoryListAbsoluteString.get(1) + "//test2.txt", "something2");
		File f = new File(testDirectoryListAbsoluteString.get(2) + "//a.txt");
		create(testDirectoryListAbsoluteString.get(2) + "//a.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(1), testDirectoryListAbsoluteString.get(2) + "//a.txt" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (moveTool.getStatusCode() == 1);
		f1.delete();
		f2.delete();
		f.delete();

	}

	// Test moving multiple files into a folder where one file does not exist
	@Test
	public void testMoveMultipleFileWithOneInvalidFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(2) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(2) + "//test.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + "//test.txt", testDirectoryListAbsoluteString.get(1) + "//test.txt",
				testDirectoryListAbsoluteString.get(2) };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		assert (moveTool.getStatusCode() == 1 && !(f1.exists()) && f2.exists());
		f1.delete();
		f2.delete();
	}

	// Test moving multiple files into new folder
	@Test
	public void testMoveMultipleFilesintoNewFolder() {
		// moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + "//test.txt", testDirectoryListAbsoluteString.get(0) + "//test2.txt",
				testDirectoryListAbsoluteString.get(0) + "//..//new" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(testDirectoryListAbsoluteString.get(1) + "//test.txt");
		File f4 = new File(testDirectoryListAbsoluteString.get(1) + "//test2.txt");
		assert (!(f1.exists() || f2.exists()) && f3.exists() && f4.exists());
		f3.delete();
		f4.delete();

		File f = new File(testDirectoryListAbsoluteString.get(1) + "//..//new");
		f.delete();
	}

	// Test moving multiple folders into a folder
	@Test
	public void testMoveMultipleFoldersintoFolder() {
		String arg[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(1), testDirectoryListAbsoluteString.get(2) };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f1 = new File(testDirectoryListAbsoluteString.get(0));
		File f2 = new File(testDirectoryListAbsoluteString.get(1));
		File f3 = new File(testDirectoryListAbsoluteString.get(2) + "//level0");
		File f4 = new File(testDirectoryListAbsoluteString.get(2) + "//level0level1");

		assert (!(f1.exists() || f2.exists()) && f3.exists() && f4.exists());
		f3.delete();
		f4.delete();
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

	public static void delete(File file) {
		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}
}