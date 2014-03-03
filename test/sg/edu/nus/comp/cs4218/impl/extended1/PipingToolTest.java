package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The pipe tools allows the output of one program to be sent to the input of
 * another program. With the help of pipe tool multiple small (and simple)
 * programs can be connected to accomplish large number of tasks.
 * 
 * Command Format - PROGRAM-1-STANDARD_OUTPUT | PROGRAM-2-STANDARD_INPUT Where
 * "|" is the pipe operator and PROGRAM-1-STANDARD_OUTPUT is the standard output
 * of program 1 and PROGRAM-2-STANDARD_INPUT is the standard input of program 2.
 * 
 */
public class PipingToolTest {

	private PipingTool pipingTool;
	private static Path rootDirectory;
	private static String rootParent;
	private static String rootDirectoryString;

	@BeforeClass
	public static void before() throws IOException {
		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir") + File.separator
				+ "test";
		rootParent = System.getProperty("user.dir");
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
	public void testLsPipeToGrep() {
		String commandline = "ls | grep .";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);
		String[] results = result.split("\n");

		assertEquals("file1.txt", results[0]);
		assertEquals("file2.txt", results[1]);
	}

	@Test
	public void testEchoPipeToCat() {
		String commandline = "echo 'print me' | cat ";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals("print me", result);
	}

	@Test
	public void tesPipeChaining() {
		String commandline = "echo 'print me' | cat | cat | cat ";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals("print me", result);
	}

	@Test
	public void testHandlePipeErrorAtFirst() {
		String commandline = "cd | cat | cat | cat ";
		pipingTool = new PipingTool(commandline.split("\\|"));

		pipingTool.execute(new File(rootDirectoryString), null);

		assertNotEquals(1, pipingTool.getStatusCode());
	}

	@Test
	public void testHandlePipeErrorInMiddle() {
		String commandline = "echo 'print me' | cat | cat a | cat ";
		pipingTool = new PipingTool(commandline.split("\\|"));

		pipingTool.execute(new File(rootDirectoryString), null);

		assertNotEquals(1, pipingTool.getStatusCode());
	}

	@Test
	public void testPipeToInvalidTool() {
		String commandline = "pwd | echo ";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(result, "");
	}

	@Test
	public void testPipeCommTool() {
		String commandline = "cat commTestCase1a.txt | comm - commTestCase1b.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootParent), null);

		assertEquals(result, output);
	}

	@Test
	public void testPipeCommTool1() {
		String commandline = "cat commTestCase1a.txt | comm - nofile.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootParent), null);

		assertEquals(result, "");
	}
	
	@Test
	public void testPipeStateWhenFirstPipeFail() {
		// The error is through before we check for the statestus code for to
		String commandline = "cd invalid | echo ";
		pipingTool = new PipingTool(commandline.split("\\|"));

		pipingTool.execute(new File(rootDirectoryString), null);

		assertNotEquals(1, pipingTool.getStatusCode());
	}

}
