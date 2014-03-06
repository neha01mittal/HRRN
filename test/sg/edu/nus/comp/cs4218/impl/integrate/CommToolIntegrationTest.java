package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

public class CommToolIntegrationTest {
 // get two cases from pipe (aalr impl)
	
	private PipingTool pipingTool;
	private static String rootParent;
	
	@BeforeClass
	public static void init() {
		rootParent = System.getProperty("user.dir");
		
	}
	@Test
	public void testPipeCommTool() {
		String commandline = "cat commTestCase1a.txt | comm - commTestCase1b.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
	    pipingTool = new PipingTool(commandline.split("\\|"));
	    File workingDir = new File(rootParent);

		String result = pipingTool.execute(workingDir, null);

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
}
