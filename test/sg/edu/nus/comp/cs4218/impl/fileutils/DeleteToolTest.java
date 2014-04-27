package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 * Delete a file or folder
 * 
 * @usage delete [path]
 * @options delete file1 - Deletes file1 delete relativepath1 - Converts
 *          relativepath to absolutepath and deletes file1 delete /../file1 -
 *          Deletes file1 delete "file1" - Deletes file1 delete newfile - Does
 *          nothing delete folder1 - Deletes folder and all its contents
 * @note
 * @success
 * @exceptions
 * 
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
		rootDirectoryString = System.getProperty("user.dir") + File.separator
				+ "deleteToolTest";

		rootDirectory = Paths.get(rootDirectoryString);
		Files.createDirectory(rootDirectory);

		testDirectoryList = new ArrayList<Path>();
		testDirectoryListRelativeString = new ArrayList<String>();
		testDirectoryListAbsoluteString = new ArrayList<String>();

		String dirPath = "";
		for (int i = 0; i < 5; i++) {
			try {
				dirPath += "level-" + i;

				Path temp = FileSystems.getDefault().getPath(
						rootDirectoryString + File.separator + dirPath);
				Files.createDirectory(temp);

				testDirectoryList.add(temp);
				testDirectoryListRelativeString.add(dirPath);
				testDirectoryListAbsoluteString.add(rootDirectoryString
						+ File.separator + dirPath);
			} catch (IOException e) {
				// catch
			}
		}
	}

	@After
	public void afterClass() throws IOException {
		deleteTool = null;
		TestUtils.delete(new File(rootDirectoryString));
	}

	@Test
	public void testDeleteAbsolutePathFile() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListAbsoluteString.get(0)
				+ File.separator + "test1.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator
				+ "test1.txt", "something");
		String a[] = { f1.toString() };
		deleteTool = new DeleteTool(a);
		deleteTool.execute(rootDirectory.toFile(), "");
		assertFalse(f1.exists());
		assertEquals(deleteTool.getStatusCode(), 0);
	}

	@Test
	public void testDeleteRelativePathFile() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0), "test1.txt");
		create(testDirectoryListAbsoluteString.get(0) + File.separator
				+ "test1.txt", "something");
		String a[] = { f1.toString() };
		deleteTool = new DeleteTool(a);
		deleteTool.execute(rootDirectory.toFile(), "");
		assertFalse(f1.exists());
		assertEquals(deleteTool.getStatusCode(), 0);

		// // Delete Again and Check for StatusCode = 1
		// deleteTool.execute(rootDirectory.toFile(), "");
		// assert (!(f1.exists()));
		// assertEquals(deleteTool.getStatusCode(), 1);
	}

	@Test
	public void testDeleteNonExistingFile() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(testDirectoryListRelativeString.get(0)
				+ File.separator + "test1.txt");
		deleteTool.delete(f1);
		assertFalse(f1.exists());
		assertEquals(deleteTool.getStatusCode(), 1);
	}

	@Test
	public void testDeleteFolder() {
		deleteTool = new DeleteTool(null);

		File f1 = new File(rootDirectoryString + File.separator
				+ testDirectoryListRelativeString.get(0), "new");
		f1.mkdir();
		String a[] = { f1.toString() };
		deleteTool = new DeleteTool(a);
		deleteTool.execute(rootDirectory.toFile(), "");
		assertFalse(f1.exists());
		assertEquals(deleteTool.getStatusCode(), 0);
	}

	@Test
	public void testDeleteFolderWithContents() {
		File f1 = new File(rootDirectoryString + File.separator
				+ testDirectoryListRelativeString.get(0), "new");
		f1.mkdir();
		File f2 = new File(f1, "test1.txt");
		try {
			f2.createNewFile();
		} catch (IOException e) {

		}
		create(testDirectoryListRelativeString.get(0) + File.separator + "new"
				+ File.separator + "test1.txt", "something");
		String a[] = { f1.toString() };
		deleteTool = new DeleteTool(a);
		deleteTool.execute(rootDirectory.toFile(), "");
		assertFalse(f1.exists());
		assertEquals(deleteTool.getStatusCode(), 0);

	}

	@Test
	public void testDeleteNonExistingFolder() {
		File f1 = new File(rootDirectoryString + File.separator
				+ testDirectoryListRelativeString.get(0) + File.separator
				+ "new");
		String a[] = { f1.toString() };
		deleteTool = new DeleteTool(a);
		deleteTool.execute(rootDirectory.toFile(), "");
		assertFalse(f1.exists());
		assertEquals(deleteTool.getStatusCode(), 0);
	}

	@Test
	public void testDeleteInvalidCommand() {
		deleteTool = new DeleteTool(null);
		deleteTool.execute(rootDirectory.toFile(), "");
		assertEquals(deleteTool.getStatusCode(), 1);
	}

	public void create(String filename, String content) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "utf-8"));
			writer.write(content);
			writer.close();
		} catch (IOException ex) {
			// report
		}
	}

}