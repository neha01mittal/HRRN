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

/**
 * @author Ranjini Aravind
 */
public class DeleteToolTest {
	private DeleteTool deleteTool;
	private Path rootDirectory;
	private String rootDirectoryString;
	private List<Path> testDirectoryList;
	private List<String> testDirectoryListRelativeString;
	private List<String> testDirectoryListAbsoluteString;

	@Before
	public void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/deleteToolTest";
		System.setProperty("user.dir", rootDirectoryString);

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		testDirectoryListRelativeString = new ArrayList<String>();
		testDirectoryListAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 5; i++) {
			try {
				dirPath += "level-" + i;

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
		deleteTool = null;

		for (int i = 0; i < testDirectoryList.size(); i++) {
			Files.deleteIfExists(testDirectoryList.get(i));
		}
		for (int i = 0; i < testDirectoryListAbsoluteString.size(); i++) {
			Path path = Paths.get(testDirectoryListAbsoluteString.get(i));
			Files.deleteIfExists(path);
		}
		// System.out.println("ROOT" + rootDirectory.getParent().toString());

		Files.deleteIfExists(rootDirectory);
		System.setProperty("user.dir", rootDirectory.getParent().toString());

	}

	@Test
	public void testDeleteAbsolutePathFile() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListAbsoluteString.get(0) + "//test1.txt");
		create(testDirectoryListAbsoluteString.get(0) + "//test1.txt", "something");

		deleteTool.delete(f1);
		assert (!(f1.exists()));
	}

	@Test
	public void testDeleteRelativePathFile() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0) + "//test1.txt");
		create(testDirectoryListRelativeString.get(0) + "//test1.txt", "something");

		deleteTool.delete(f1);
		assert (!(f1.exists()));
	}

	@Test
	public void testDeleteNonExistingFile() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0) + "//test1.txt");
		deleteTool.delete(f1);
		deleteTool.delete(f1);
		assert (f1.exists());

	}

	@Test
	public void testDeleteFolder() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0) + "/new");
		deleteTool.delete(f1);
		assert (!(f1.exists()));

	}

	@Test
	public void testDeleteFolderWithContents() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0) + "/new");
		File f2 = new File(testDirectoryListRelativeString.get(0) + "/new/test1.txt");
		create(testDirectoryListRelativeString.get(0) + "/new/test1.txt", "something");
		deleteTool.delete(f1);
		assert (!(f1.exists() || f2.exists()));

	}

	@Test
	public void testDeleteNonExistingFolder() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0) + "/new");
		deleteTool.delete(f1);
		deleteTool.delete(f1);
		assert (f1.exists());

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