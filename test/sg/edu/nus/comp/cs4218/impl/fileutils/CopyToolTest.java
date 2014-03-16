package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

/**
 * copy a file/folder to a given location
 * 
 * @usage copy [path1] [path2] … [path to folder]
 * @options 
 * copy file1 file2 Copies file1 contents into file2 
 * copy /../file1 file2  Copies file1 contents into file2 
 * copy "file1" "file2" Copies file1 contents into file2 
 * copy file1 newfile Creates newfile and copies file1 contents into newfile 
 * copy file1 folder Copies file1 contents into folder 
 * copy file1 newfolder Creates newfolder and copies file1 contents into newfolder 
 * copy folder1 folder2 Copies contents of folder1 into folder2 
 * copy folder1 newfolder Creates newfolder and copies folder1 contents into newfolder 
 * copy file1 file2 file3 folder1 Copies/Replaces file1, file2, file3 in folder1
 * copy folder1 folder2 file3 newfolder - Creates and copies folder1, folder2, file3 into newfolder
 * @note
 * @success
 * @exceptions
 */
public class CopyToolTest {

	private static CopyTool copyTool;
	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<Path> testDirectoryList;
	private static List<String> testDirectoryListRelativeString;
	private static List<String> testDirectoryListAbsoluteString;
	private static File file1;
	private static File file2;

	@BeforeClass
	public static void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/copyToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		testDirectoryListRelativeString = new ArrayList<String>();
		testDirectoryListAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 3; i++) {
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

	@AfterClass
	public static void after() throws IOException {
		copyTool = null;
		recursivedelete(rootDirectory.toFile());
	}
	
	@Before
	public void beforeFunction() throws IOException {
		file1 = new File(rootDirectoryString, "test1.txt");
		file2 = new File(rootDirectoryString, "test2.txt");
	}
	
	@After
	public void afterFunction() throws IOException {
		file1.delete();
		file2.delete();
	}

	// Test copying file into a new file
	@Test
	public void testCopyFile() {
		String a[] = {file1.toString(), file2.toString()};
		create(file1.toString(), "Something");
		copyTool = new CopyTool(a);
		
		copyTool.execute(rootDirectory.toFile(), null);
		assertTrue (compare(file1, file2));
	}

	// Test copying file with relative path into a new file
	@Test
	public void testCopyFileWithRelativePath() {
		copyTool = new CopyTool(null);
		create(file1.toString(), "Something");
		String a[] = {"test1.txt", "test2.txt" };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");
		assertEquals (copyTool.getStatusCode(), 0);
	}

	// Test file name with two extensions (.txt.txt)
	@Test
	public void testCopyFileWithTwoExtensions() {
		copyTool = new CopyTool(null);
		file1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt.txt");
		create(file1.toString(), "Something");
		file2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		copyTool.copy(file1, file2);
		assertFalse (compare(file1, file2));
	}

	// Test file name with invalid characters
	@Test
	public void testCopyFileWithInvalidCharacters() {
		file1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test@#22.xt");
		file2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		String a[] = { file1.toString(), file2.toString() };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");

		assertEquals(copyTool.getStatusCode(), 1);
	}

	// Test with no file names (just "copy" command)
	@Test
	public void testCopyCommandWithNoArguments() {
		String a[] = {};
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");

		assertEquals( copyTool.getStatusCode(), 1);
	}

	// Test copying file into invalid path
	// @Test
	public void testCopyFileIntoInvalidPath() {
		copyTool = new CopyTool(null);
		create(file1.toString(), "Something");
		String a[] = { file1.toString(), testDirectoryListAbsoluteString.get(0) + File.separator + "123**@1/^" };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");
		assertEquals( copyTool.getStatusCode(), 1);
	}

	// Test copying file into same folder
	@Test
	public void testCopyFileIntoSameFolder() {
		copyTool = new CopyTool(null);
		create(file1.toString(), "Something");
		String[] a = { file1.toString(), rootDirectoryString };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");
		assertEquals(0, copyTool.getStatusCode());
	}

	// Test copying file into an existing file
	@Test
	public void testCopyAndReplaceExistingFile() {
		copyTool = new CopyTool(null);
		file1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt");
		file2 = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt", "something");

		copyTool.copy(file1, file2);

		file2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");

		assertTrue (compare(file1, file2));
		
	}

	// // Test copying folder into file
	@Test
	public void testCopyFolderIntoFile() {
		create(file1.toString(), "Something");
		
		String[] a = { testDirectoryListAbsoluteString.get(0), file1.toString() };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");

		assertEquals(1, copyTool.getStatusCode());
	}

	// Test copying folder to invalid path
	@Test
	public void testCopyFolderIntoInvalidPath() {
		copyTool = new CopyTool(null);
		String a[] = { testDirectoryListAbsoluteString.get(0), "R!!df$/..*&*" };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");
		assertEquals(1, copyTool.getStatusCode());
	}

	// Test copying folder into its parent folder (does it replace or rename)
	@Test
	public void testCopyFolderIntoParentFolder() {
		copyTool = new CopyTool(null);
		String[] a = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(0) + "//.." };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");
		assertEquals(0, copyTool.getStatusCode());

	}

	// Test copying multiple files into a folder
	@Test
	public void testCopyMultipleFiles() {
		// copyTool = new CopyTool(null);
		file1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		file2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt", "something2");
		String[] arg = { testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt",
				testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt", testDirectoryListAbsoluteString.get(1) };
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test.txt");
		File f4 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test2.txt");
		assertTrue (compare(file1, f3) && compare(file2, f4));
		f3.delete();
		f4.delete();
	}

	// Test copying multiple files into existing file - the last copy operation
	// overwrites old contents
	@Test
	public void testCopyMultipleFilesintoExisitingFile() {
		File f1 = new File(rootDirectoryString + File.separator + "test1.txt");
		create(rootDirectoryString + File.separator + "test1.txt", "something");
		File f2 = new File(rootDirectoryString + File.separator + "test2.txt");
		create(rootDirectoryString + File.separator + "test2.txt", "something2");
		File f = new File(rootDirectoryString + File.separator + "a.txt");
		create(rootDirectoryString + File.separator + "a.txt", "something33");
		String[] arg = { f1.toString(), f2.toString(), f.toString()};
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		assertTrue (compare(f2, f));
		f1.delete();
		f2.delete();
		TestUtils.delete(f);

	}

	// Test copying multiple files into a new file - the last copy operation
	// overwrites previous ones
	@Test
	public void testCopyMultipleFilesintoNewFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt",
				testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt", testDirectoryListAbsoluteString.get(1) + File.separator + "a.txt" };
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		File f = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "a.txt");
		assertTrue (compare(f2, f));
		f1.delete();
		f2.delete();
		TestUtils.delete(f);
	}

	// Test copying multiple folders into a file -Test if it creates a folder
	// with destination file name
	@Test
	public void testCopyMultipleFoldersintoFile() {
		File f = new File(testDirectoryListAbsoluteString.get(2) + File.separator + "a.txt");
		create(testDirectoryListAbsoluteString.get(2) + File.separator + "a.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(1), testDirectoryListAbsoluteString.get(2) + "//a.txt" };
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		TestUtils.delete(f);
		assertEquals (copyTool.getStatusCode(), 1);
		

	}

	// Test copying multiple files into a folder where one file does not exist
	@Test
	public void testCopyMultipleFileWithOneInvalidFile() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(2) + File.separator + "test.txt");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt",
				"blahblah.txt", testDirectoryListAbsoluteString.get(2) };
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		f1.delete();
		f2.delete();
		assertEquals (copyTool.getStatusCode() , 1);
	}

	// Test copying multiple files into new folder
	@Test
	public void testCopyMultipleFilesintoNewFolder() {
		// copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt", "something2");
		File f = new File(rootDirectory + File.separator + "newFolder");
		f.mkdir();
		String arg[] = {f1.toString(), f2.toString(), f.toString()};
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(f, "test.txt");
		File f4 = new File(f, "test2.txt");
		assertTrue (compare(f1, f3) && compare(f2, f4));
		
		f1.delete();
		f2.delete();
		TestUtils.delete(f);
	}

	// Test copying multiple folders into a folder
	@Test
	public void testCopyMultipleFoldersintoFolder() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test2.txt");
		create(testDirectoryListAbsoluteString.get(1) + File.separator + "test2.txt", "something2");
		File f = new File(testDirectoryListAbsoluteString.get(2) + File.separator + "a.txt");
		create(testDirectoryListAbsoluteString.get(2) + File.separator + "a.txt", "something33");
		String arg[] = { testDirectoryListAbsoluteString.get(0), testDirectoryListAbsoluteString.get(1), testDirectoryListAbsoluteString.get(2) };
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(testDirectoryListAbsoluteString.get(2) + File.separator + "test.txt");
		File f4 = new File(testDirectoryListAbsoluteString.get(2) + File.separator + "test2.txt");
		assertTrue (compare(f1, f3) && compare(f2, f4));
		f1.delete();
		f2.delete();
		TestUtils.delete(f);
		f2.delete();
		f3.delete();
		f4.delete();

	}

	// Test copying one folder to another folder
	@Test
	public void testCopyToExistingFolder() {
		copyTool = new CopyTool(null);
		String result = "true";

		File from = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(0));
		copyTool.copy(from, to);
		String[] fList = from.list();

		for (int index = 0; index < fList.length; index++) {
			File dest = new File(to, fList[index]);
			File source = new File(from, fList[index]);
			if (compare(dest, source) == false) {
				result = "false";
			}
		}
		assertTrue (result == "true");
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		f1.delete();
		f2.delete();
	}

	// Test copying file into new folder
	@Test
	public void testCopyToNewFolder() {
		File to = new File(rootDirectoryString + File.separator + "newfolder");
		to.mkdir();
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt", "something");
		
		File f1 = new File(to, "test1.txt");
		String[] a = {f2.toString(), to.toString()};
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");
		assertTrue (compare(f1, f2));
		f1.delete();
		f2.delete();
		to.delete();

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

	public boolean compare(File file1, File file2) {

		String s1 = "";
		String s3 = "";
		String y = "", z = "";
		try {
			@SuppressWarnings("resource")
			BufferedReader bfr = new BufferedReader(new FileReader(file1));
			@SuppressWarnings("resource")
			BufferedReader bfr1 = new BufferedReader(new FileReader(file2));
			while ((z = bfr1.readLine()) != null)
				s3 += z;

			while ((y = bfr.readLine()) != null)
				s1 += y;
		} catch (Exception e) {
			return false;
		}

		if (s3.equals(s1)) {
			return true;
		} else {
			return false;
		}

	}
	
	public static void recursivedelete(File file) {
		try {
				String[] fList = file.list();
				for (int index = 0; index < fList.length; index++) {
					File source = new File(file, fList[index]);
					// Recursion call take place here
					recursivedelete(source);
				}
				// Delete the source folders
				for (int i = 0; i < fList.length; i++) {
					File source = new File(file, fList[i]);
					Files.deleteIfExists(source.toPath());
				}
					Files.deleteIfExists(file.toPath());
		} catch (Exception ex) {
		}
	}
}
	