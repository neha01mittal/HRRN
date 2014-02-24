package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertNotEquals;

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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

/**
 * @author Neha Mittal
 */
public class MyFunctionsCopyToolTest {

	private static CopyTool copyTool;
	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<Path> testDirectoryList;
	private static List<String> testDirectoryListRelativeString;
	private static List<String> testDirectoryListAbsoluteString;

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

	@AfterClass
	public static void after() throws IOException {
		copyTool = null;
		TestUtils.delete(new File(rootDirectoryString));
	}

	// Test copying file into a new file
	@Test
	public void testRecursiveCopyWithAbsolutePath() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		copyTool.recursivecopy(f1, f2);
		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	// Test copying file with relative path into a new file
	@Test
	public void testRecursiveCopyWithRelativePath() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListRelativeString.get(0) + File.separator + "test.txt");
		create(testDirectoryListRelativeString.get(0) + File.separator + "test.txt", "something");
		File f2 = new File(testDirectoryListRelativeString.get(0) + File.separator + "test2.txt");
		copyTool.recursivecopy(f1, f2);
		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	// Test file name with two extensions (.txt.txt)
	@Test
	public void testRecursiveCopyWithTwoExtensions() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test2.txt");
		copyTool.recursivecopy(f1, f2);
		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	// Test file name with invalid characters
	@Test
	public void testRecursiveCopyWithInvalidCharacters() {
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test@#22.xt");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		String a[] = { f1.getPath().toString(), f2.getPath().toString() };
		copyTool = new CopyTool(a);
		copyTool.recursivecopy(f1, f2);

		assertNotEquals(0, copyTool.getStatusCode());
	}

	// Test copying file into invalid path
	// @Test
	public void testRecursiveCopyIntoInvalidPath() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		String a[] = { f1.getPath().toString(), testDirectoryListAbsoluteString.get(0) + "/blah/test.txt" };
		copyTool = new CopyTool(a);
		copyTool.recursivecopy(rootDirectory.toFile(), f1);
		f1.delete();
		assertNotEquals(0, copyTool.getStatusCode());
	}

	// Test copying file into an existing file
	@Test
	public void testCopyAndReplaceExistingFile() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt", "something");

		copyTool.copy(f1, f2);

		f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");

		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	// // Test copying folder into file
	@Test
	public void testCopyFolderIntoFile() {

		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test.txt", "something");
		String a[] = { testDirectoryListAbsoluteString.get(0), f1.getPath().toString() };
		copyTool = new CopyTool(a);
		copyTool.execute(rootDirectory.toFile(), "");

		assertNotEquals(0, copyTool.getStatusCode());
		f1.delete();
	}

	// Test copying one folder to another folder
	@Test
	public void testCopyToExistingFolder() {
		copyTool = new CopyTool(null);
		String result = "true";

		File from = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(0));
		copyTool.recursivecopy(from, to);
		String[] fList = from.list();

		for (int index = 0; index < fList.length; index++) {
			File dest = new File(to, fList[index]);
			File source = new File(from, fList[index]);
			if (compare(dest, source) == false) {
				result = "false";
			}
		}
		assert (result == "true");
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + File.separator + "test1.txt");
		f1.delete();
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		f2.delete();
	}

	// Test copying file into new folder
	@Test
	public void testCopyToNewFolder() {
		copyTool = new CopyTool(null);
		create(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(1) + File.separator + " .." + File.separator + "newfolder");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + "test1.txt");
		copyTool.recursivecopy(f2, to);
		File f1 = new File(testDirectoryListAbsoluteString.get(1) + File.separator + " .." + File.separator + "newfolder" + File.separator + "test1.txt");

		assert (compare(f1, f2));
		f1.delete();
		to.delete();
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

}