package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

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

/**
 * Move a file/folder to a given location
 * 
 * @usage move [path] [path] … [path to folder]
 * @options move file1 file2 - Moves file1 contents into file2 
 * move /../file1 file2 - Moves file1 contents into file2 
 * move "file1" "file2" - Moves file1 contents into file2 
 * move file1 newfile - Creates newfile and moves file1 contents into newfile 
 * move file1 folder - Moves file1 into folder 
 * move file1 newfolder - Moves file1 into newfolder 
 * move folder1 folder2 - Moves contents of folder1 into folder2 
 * move folder1 newfolder - Moves folder1 into newfolder
 * move file1 file2 file3 folder1 - Moves file1, file2, file3 in folder1 
 * move folder1 folder2 file3 newfolder - Moves folder1, folder2, file3 into newfolder
 * @note
 * @success
 * @exceptions
 */

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
		rootDirectoryString = System.getProperty("user.dir") + File.separator + "moveToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		testDirectoryListRelativeString = new ArrayList<String>();
		testDirectoryListAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 5; i++) {
			try {
				dirPath += "level" + i;

				Path temp = FileSystems.getDefault().getPath(rootDirectoryString + File.separator + dirPath);
				Files.createDirectory(temp);

				testDirectoryList.add(temp);
				testDirectoryListRelativeString.add(dirPath);
				testDirectoryListAbsoluteString.add(rootDirectoryString + File.separator + dirPath);
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
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator +"test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		moveTool.move(f1, f2);
		assertFalse (f1.exists()) ;assertTrue( f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveFileWithRelativePath() {
		moveTool = new MoveTool(null);
		File f1 = new File("test.txt");
		create(rootDirectoryString + File.separator + "test.txt", "something");
		File f2 = new File("test2.txt");
		String a[] = { f1.toString(), f2.toString() };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(rootDirectoryString ,"test.txt");
		File f4 = new File(rootDirectoryString , "test2.txt");
		assertFalse( f3.exists());
		assertTrue( f4.exists());
		f4.delete();
	}

	@Test
	public void testMoveIntoFileWithSameName() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt", "something");

		moveTool.move(f1, f2);

		assertFalse (f1.exists()) ;
		assertTrue( f2.exists());
		f2.delete();
	}

	@Test
	public void testMoveToExistingFolder() {
		moveTool = new MoveTool(null);
		File from = new File(testDirectoryListAbsoluteString.get(1));
		File f1 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt", "something");

		File to = new File(testDirectoryListAbsoluteString.get(0));

		moveTool.move(from, to);

		assertFalse (from.exists()) ;assertTrue( to.exists());
		f1 = new File(testDirectoryListAbsoluteString.get(0) + "/level0level1/test1.txt");
		f1.delete();
		f1 = new File(testDirectoryListAbsoluteString.get(0) + "/level0level1");
		f1.delete();
	}

	@Test
	public void testMoveToNewFolder() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(1) + File.separator + ".." + File.separator + "newfolder");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + ".." + File.separator + "newfolder" + File.separator + "test1.txt");
		moveTool.move(f1, to);
		assertFalse (f1.exists()) ;assertTrue( to.exists());
		f2.delete();
		to.delete();

	}

	// Test moving file into same folder
	@Test
	public void testMoveFileIntoSameFolder() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		String a[] = { f1.getPath().toString(), testDirectoryListAbsoluteString.get(0) };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assertEquals (moveTool.getStatusCode(), 0);
		f1.delete();
	}

	// Test moving folder into file
	@Test
	public void testMoveFolderIntoFile() {

		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		String a[] = { testDirectoryListAbsoluteString.get(0), f1.getPath().toString() };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		f1.delete();
		assertEquals (moveTool.getStatusCode(), 1);
	}

	// Test moving folder to invalid path
	@Test
	public void testMoveFolderIntoInvalidPath() {
		moveTool = new MoveTool(null);
		String a[] = { testDirectoryListAbsoluteString.get(0), "invalid" };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assertEquals (moveTool.getStatusCode(), 1);
	}

	// Test moving folder into its parent folder (does it replace)
	@Test
	public void testMoveFolderIntoParentFolder() {
		moveTool = new MoveTool(null);
		String a[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(0) + File.separator  + ".." };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assertEquals (moveTool.getStatusCode(), 1);

	}

	// Test moving multiple files into a folder
	@Test
	public void testMoveMultipleFiles() {
		// moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt",
				testDirectoryListAbsoluteString.get(1) };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(testDirectoryListAbsoluteString.get(1) + File.separator  + "test.txt");
		File f4 = new File(testDirectoryListAbsoluteString.get(1) + File.separator  + "test2.txt");
		assertFalse (f1.exists()) ;assertFalse( f2.exists());
		assertTrue (f3.exists()) ;assertTrue( f4.exists());
		f3.delete();
		f4.delete();
	}

	// Test moving multiple files into existing file - the last move operation
	// overwrites old contents
	@Test
	public void testMoveMultipleFilesintoExisitingFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt", "something2");
		File f = new File(testDirectoryListAbsoluteString.get(1) + File.separator  + "a.txt");
		create(testDirectoryListAbsoluteString.get(1) + File.separator  + "a.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt",
				testDirectoryListAbsoluteString.get(1) + File.separator  + "a.txt" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		assertFalse (f1.exists()) ;assertFalse( f2.exists());
		assertTrue (f.exists()) ;
		f.delete();
	}

	// Test moving file into invalid path
	@Test
	public void testMoveFileIntoInvalidPath() {
		moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		String a[] = { f1.getPath().toString(), testDirectoryListAbsoluteString.get(0) + File.separator  + "blah//test.txt" };
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
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt");
		moveTool.move(f1, f2);
		assertFalse (f1.exists()) ;assertTrue( f2.exists());
		f2.delete();
	}

	// Test file name with invalid characters
	@Test
	public void testMoveFileWithInvalidCharacters() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test@#22.xt");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		String a[] = { f1.getPath().toString(), f2.getPath().toString() };
		moveTool = new MoveTool(a);
		moveTool.execute(rootDirectory.toFile(), "");
		assertEquals (moveTool.getStatusCode(), 1);
	}

	// Test moving multiple files into a new file - move operations happens in
	// pipe
	@Test
	public void testMoveMultipleFilesintoNewFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt",
				testDirectoryListAbsoluteString.get(1) + File.separator  + "a.txt" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f = new File(testDirectoryListAbsoluteString.get(1) + File.separator  + "a.txt");
		assertFalse (f1.exists()) ;assertFalse( f2.exists());
		assertTrue (f.exists()) ;
	}

	// Test moving multiple folders into a file -Test if it creates a folder
	// with destination file name
	@Test
	public void testMoveMultipleFoldersintoFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator  + "test2.txt");
		create(testDirectoryListAbsoluteString.get(1) + File.separator  + "test2.txt", "something2");
		File f = new File(testDirectoryListAbsoluteString.get(2) + File.separator  + "a.txt");
		create(testDirectoryListAbsoluteString.get(2) + File.separator  + "a.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(1), testDirectoryListAbsoluteString.get(2) + File.separator  + "a.txt" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		assertEquals (moveTool.getStatusCode() , 1);
		f1.delete();
		f2.delete();
		f.delete();

	}

	// Test moving multiple files into a folder where one file does not exist
	@Test
	public void testMoveMultipleFileWithOneInvalidFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(2) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(2) + File.separator  + "test.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", testDirectoryListAbsoluteString.get(1) + File.separator  + "test.txt",
				testDirectoryListAbsoluteString.get(2) };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		assertEquals (moveTool.getStatusCode() , 1);
		assertFalse (f1.exists()) ;assertTrue( f2.exists());
		f1.delete();
		f2.delete();
	}

	// Test moving multiple files into new folder
	@Test
	public void testMoveMultipleFilesintoNewFolder() {
		// moveTool = new MoveTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt", "something2");
		File f = new File(rootDirectoryString + File.separator + "new");
		f.mkdir();
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator  + "test.txt", testDirectoryListAbsoluteString.get(0) + File.separator  + "test2.txt",
				rootDirectoryString + File.separator + "new" };
		moveTool = new MoveTool(arg);
		moveTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(rootDirectoryString + File.separator + "new" + File.separator + "test.txt");
		File f4 = new File(rootDirectoryString + File.separator + "new" + File.separator + "test2.txt");
		assertFalse (f1.exists()) ;assertFalse( f2.exists());
		assertTrue (f3.exists()) ;assertTrue( f4.exists());
		f3.delete();
		f4.delete();
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
		File f3 = new File(testDirectoryListAbsoluteString.get(2) + File.separator  + "level0");
		File f4 = new File(testDirectoryListAbsoluteString.get(2) + File.separator  + "level0level1");

		assertFalse (f1.exists()) ;assertFalse( f2.exists());
		assertTrue (f3.exists()) ;assertTrue( f4.exists());
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

}