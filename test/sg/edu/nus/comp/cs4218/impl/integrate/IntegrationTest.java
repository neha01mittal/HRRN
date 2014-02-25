package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private final ByteArrayOutputStream	errContent	= new ByteArrayOutputStream();
	private static String				rootDirString;
	private static String				testDirString;
	private static List<File>			orignalFileList;
	private static Shell				shell;

	@BeforeClass
	public static void beforeClass() {
		// create new dir and files inside
		rootDirString = System.getProperty("user.dir") + File.separator;
		testDirString = "data" + File.separator + "integrationTest";
		orignalFileList = Arrays.asList(new File(rootDirString + testDirString).listFiles());

		shell = new Shell();
	}

	@AfterClass
	public static void afterClass() {
		shell = null;
	}

	@Before
	public void before() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void after() {
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void testEmptyPiping() {
		String cmd = "|||";
		shell.executeInputOnce(cmd);
		assertEquals("[1]+  Stopped                 PipingTool\n", outContent.toString());
	}

	@Test
	public void testPipingNormalInputs() {
		String cmd = "cd " + testDirString + " | pwd | ls";
		shell.executeInputOnce(cmd);

		String expected = rootDirString + testDirString;
		for (int i = 0; i < orignalFileList.size(); i++) {
			expected += orignalFileList.get(i).getName() + "\n";
		}
		assertEquals(expected, outContent.toString());
	}
}