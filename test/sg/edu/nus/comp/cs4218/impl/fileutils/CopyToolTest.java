package sg.edu.nus.comp.cs4218.impl.fileutils;

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
import org.junit.Before;
import org.junit.Test;

public class CopyToolTest {
	private CopyTool		copyTool;
	private Path			rootDirectory;
	private String			rootDirectoryString;
	private List<Path>		testDirectoryList;
	private List<String>	testDirectoryListRelativeString;
	private List<String>	testDirectoryListAbsoluteString;

	@Before
	public void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/copyToolTest";
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
		copyTool = null;
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
	public void testCopyFile() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		copyTool.copy(f1, f2);
		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	@Test
	public void testCopyFileWithRelativePath() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListRelativeString.get(0) + "//test.txt");
		create(testDirectoryListRelativeString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListRelativeString.get(0) + "//test2.txt");
		copyTool.copy(f1, f2);
		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	@Test
	public void testCopyMultipleFiles() {
		// copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test.txt", "something");
		File f2 = new File(testDirectoryListAbsoluteString.get(0) + "//test2.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test2.txt", "something2");
		String arg[] = { testDirectoryListAbsoluteString.get(0) + "//test.txt", testDirectoryListAbsoluteString.get(0) + "//test2.txt",
				testDirectoryListAbsoluteString.get(1) };
		copyTool = new CopyTool(arg);
		copyTool.execute(rootDirectory.toFile(), "");
		File f3 = new File(testDirectoryListAbsoluteString.get(1) + "//test.txt");
		File f4 = new File(testDirectoryListAbsoluteString.get(1) + "//test2.txt");
		assert (compare(f1, f3) && compare(f2, f4));
		f1.delete();
		f2.delete();
		f3.delete();
		f4.delete();
	}

	@Test
	public void testCopyAndReplaceExistingFile() {
		copyTool = new CopyTool(null);
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + "//test1.txt", "something");

		copyTool.copy(f1, f2);

		f2 = new File(testDirectoryListAbsoluteString.get(1) + "//test1.txt");

		assert (compare(f1, f2));
		f1.delete();
		f2.delete();
	}

	@Test
	public void testCopyToExistingFolder() {
		copyTool = new
				CopyTool(null);
		String result = "true";

		File from = new File(testDirectoryListAbsoluteString.get(1));
		create(testDirectoryListAbsoluteString.get(0) + "//test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(0));
		copyTool.copy(from, to);
		String[] fList = from.list();

		for (int index = 0; index < fList.length; index++) {
			File dest = new
					File(to, fList[index]);
			File source = new File(from, fList[index]);
			if (compare(dest, source) == false) {
				result = "false";
			}
		}
		assert (result == "true");
		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test1.txt");
		f1.delete();
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + "//test1.txt");
		f2.delete();
	}

	@Test
	public void testCopyToNewFolder() {
		copyTool = new CopyTool(null);
		create(testDirectoryListAbsoluteString.get(1) + "//test1.txt", "something");
		File to = new File(testDirectoryListAbsoluteString.get(1) + "//..//newfolder");
		File f1 = new File(testDirectoryListAbsoluteString.get(1) + "//..//newfolder//test1.txt");
		File f2 = new File(testDirectoryListAbsoluteString.get(1) + "//test1.txt");
		copyTool.copy(f1, to);

		assert (compare(f1, f2));
		f1.delete();
		to.delete();
		f2.delete();

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

		System.out.println();

		System.out.println(s3);

		if (s3.equals(s1)) {

			System.out.println("Content of both files are same");
			return true;
		} else {

			System.out.println("Content of both files are not same");
			return false;
		}

	}

}