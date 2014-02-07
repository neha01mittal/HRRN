package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PipingToolTest {

	private PipingTool pipingTool;
	private static Path rootDirectory;
	private static String rootDirectoryString;

	@BeforeClass
	public static void before() throws IOException {
		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + "/test";
		rootDirectory = Paths.get(rootDirectoryString);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		// TestUtils.delete(new File(rootDirectoryString));
	}

	@After
	public void after() throws IOException {
		pipingTool = null;
	}

	@Test
	public void test() {
		String commandline = "ls | grep .";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);
		String[] results = result.split("\n");

		assertEquals("file1.txt", results[0]);
		assertEquals("file2.txt", results[1]);
	}
}
