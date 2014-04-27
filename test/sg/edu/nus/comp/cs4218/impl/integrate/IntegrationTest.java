package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class IntegrationTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private static String originalDirString;
	private static String testDirString;
	private static List<File> testDirFileList;
	private Shell shell;
	private static InputStream originalStdin;
	private static PrintStream originalStdout;
	private static PrintStream originalStderr;

	@BeforeClass
	public static void beforeClass() {
		// cache system variables.
		originalDirString = System.getProperty("user.dir");
		originalStdin = System.in;
		originalStdout = System.out;
		originalStderr = System.err;

		// create new dir and files inside
		testDirString = originalDirString + File.separator + "data"
				+ File.separator + "integrationTest2";
		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// just ignore the hidden files
				if (name.lastIndexOf('.') != 0) {
					return true;
				}
				return false;
			}
		};
		testDirFileList = Arrays.asList(new File(testDirString)
				.listFiles(fileNameFilter));
	}

	@AfterClass
	public static void afterClass() {
		// remove shell and set back system
		System.setProperty("user.dir", originalDirString);
	}

	@Before
	public void before() {
		System.setProperty("user.dir", testDirString);
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		shell = new Shell();
	}

	@After
	public void after() {
		// release std and clean the buffer
		System.setIn(originalStdin);
		System.setOut(originalStdout);
		System.setErr(originalStderr);
		shell = null;
	}

	//
	// Step 1: Test shell against each single tool
	//

	@Test
	public void testSimpleEmpty() {
		String input = "";
		shell.executeInput(input, false);

		assertEquals("", outContent.toString());
	}

	@Test
	public void testSimpleInvalid() {
		String input = "invalid";
		shell.executeInput(input, false);

		assertEquals("", outContent.toString());
	}

	@Test
	public void testSimpleLs() {
		String input = "ls";
		shell.executeInput(input, false);

		String expected = "";
		for (int i = 0; i < testDirFileList.size(); i++) {
			expected += testDirFileList.get(i).getName() + "\n";
		}
		assertEquals(expected, outContent.toString().replaceAll("\r", ""));
	}
}