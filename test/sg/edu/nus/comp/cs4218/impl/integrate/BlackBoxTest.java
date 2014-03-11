package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
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

public class BlackBoxTest {

	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private final ByteArrayOutputStream	errContent	= new ByteArrayOutputStream();
	private static String				originalDirString;
	private static String				testDirString;
	private static List<File>			testDirFileList;
	private static Shell				shell;
	private static InputStream			originalStdin;
	private static PrintStream			originalStdout;
	private static PrintStream			originalStderr;

	@BeforeClass
	public static void beforeClass() {
		// cache system variables.
		originalDirString = System.getProperty("user.dir");
		originalStdin = System.in;
		originalStdout = System.out;
		originalStderr = System.err;

		// create new dir and files inside
		testDirString = originalDirString + File.separator + "data" + File.separator + "integrationTest";
		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(".txt")) {
						return true;
					}
				}
				return false;
			}
		};
		testDirFileList = Arrays.asList(new File(testDirString).listFiles(fileNameFilter));
	}

	@AfterClass
	public static void afterClass() {
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
		shell.executionFlag = false;

		// release std and clean the buffer
		System.setIn(originalStdin);
		System.setOut(originalStdout);
		System.setErr(originalStderr);
		System.setProperty("user.dir", originalDirString);

		// remove shell and set back system
		shell = null;
	}

	@Test
	public void testSimple() {

		String input = "ls\r\n ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();

		// generate expected string
		String expected = "";
		for (int i = 0; i < testDirFileList.size(); i++) {
			expected += testDirFileList.get(i).getName() + "\n";
		}

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChains1() {

		String input = "ls | grep test | grep 01 | cat - \r\n ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();

		// generate expected string
		String expected = "test_file_01.txt\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}
}