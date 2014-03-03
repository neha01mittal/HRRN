package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertTrue;

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
	private static InputStream			stdin;

	@BeforeClass
	public static void beforeClass() {
		stdin = System.in;
		// create new dir and files inside
		originalDirString = System.getProperty("user.dir");
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
		System.setProperty("user.dir", testDirString);
	}

	@AfterClass
	public static void afterClass() {
		System.setProperty("user.dir", originalDirString);
	}

	@Before
	public void before() {
		// seize the std content
		shell = new Shell();
	}

	@After
	public void after() {
		shell.executionFlag = false;

		// release std and clean the buffer
		System.setIn(stdin);
		System.setOut(null);
		System.setErr(null);

		// remove shell and set back system
		// shell = null;
	}

	@Test
	public void testSimpleLs() {
		String input = "ls\r\n ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();

		String expected = "";
		for (int i = 0; i < testDirFileList.size(); i++) {
			expected += testDirFileList.get(i).getName() + "\n";
		}
		assertTrue(outContent.toString().replaceAll("\r", "").toLowerCase().contains(expected.toLowerCase()));
	}
}