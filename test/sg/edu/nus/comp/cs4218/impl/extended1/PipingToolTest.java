package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
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

	private PipingTool		pipingTool;
	private static Path		rootDirectory;
	private static String	rootDirectoryString;

	@BeforeClass
	public static void before() throws IOException {
		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir");
		rootDirectory = Paths.get(rootDirectoryString);
	}

	@After
	public void after() throws IOException {
		pipingTool = null;
	}

	@Test
	public void testPipeToNothing() {
		String commandline = "|";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("Invalid arguments", result);
	}

	@Test
	public void testInvalidPipeTo1() {
		String commandline = "ls | a";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("-bash: a: command not found", result);
	}

	@Test
	public void testInvalidPipeTo2() {
		String commandline = "ls | b";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("-bash: b: command not found", result);
	}

	@Test
	public void testPipeWithInvalidInMiddle() {
		String commandline = "ls | ls | c | cat";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(1, pipingTool.getStatusCode());
		assertEquals("-bash: c: command not found", result);
	}

	@Test
	public void testLsPipeToGrep() {
		String commandline = "ls | grep file";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals("file1.txt\nfile2.txt\nfile3.txt\nfile4.txt", result);
	}

	@Test
	public void testEchoPipeToCat() {
		String commandline = "echo 'print me' | cat ";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals("print me", result);
	}

	@Test
	public void tesPipeChaining() {
		String commandline = "echo 'print me' | cat | cat | cat ";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals("print me", result);
	}

	@Test
	public void testPipeToInvalidTool() {
		String commandline = "pwd | echo ";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(result, "");
	}

	@Test
	public void testPipeCommTool() {
		String commandline = "cat commTestCase1a.txt | comm - commTestCase1b.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(result, output);
	}

	@Test
	public void testPipeCommTool1() {
		String commandline = "cat commTestCase1a.txt | comm - nofile.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(result, "");
	}

	@Test
	public void testPipeCdTool() {
		String commandline = "cd .. | cd .. ";
		String output = "";
		pipingTool = new PipingTool(commandline.split("\\s\\|\\s"));

		String result = pipingTool.execute(new File(rootDirectoryString), null);

		assertEquals(output, result);
	}
}
