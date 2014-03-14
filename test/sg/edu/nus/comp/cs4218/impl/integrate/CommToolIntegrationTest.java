package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

public class CommToolIntegrationTest {
	// get two cases from pipe (aalr impl)

	private PipingTool		pipingTool;
	private static String	rootParent;

	@BeforeClass
	public static void init() {
		rootParent = System.getProperty("user.dir") + File.separator
				+ "testFiles";

	}

	@Test
	public void testCatTool() {
		String commandline = "cat commTestCase1a.txt | comm - commTestCase1b.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		pipingTool = new PipingTool(commandline.split("\\|"));
		File workingDir = new File(rootParent);

		String result = pipingTool.execute(workingDir, null);

		assertEquals(result, output);
	}

	@Test
	public void testPipeCatTool1() {
		String commandline = "cat commTestCase1a.txt | comm - nofile.txt ";
		String output = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		pipingTool = new PipingTool(commandline.split("\\|"));

		String result = pipingTool.execute(new File(rootParent), null);

		assertEquals(result, "");
	}

	@Test
	public void testLs() {
		String commandline = "ls | comm - commTestCase1b.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		System.out.println(output);
		String expectedOutput = "\t\taaa\ncommTestCase1a.txt\n\t\tbbb\ncommTestCase1b.txt\n\t\tffff\nlvl1folder\ntestCase_2.txt\ntestCase_3.txt\ntestCase_4.txt\ntestCase_5.txt";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testPwd() {
		String commandline = "pwd | comm - commTestCase1a.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = rootParent + "\n\t\taaa\n\t\tccc\n\t\teee\n\t\tgggggg";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testPwd2() {
		String commandline = "pwd | comm -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCd() {
		String commandline = "cd .. | comm - commTestCase1b.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCd2() {
		String commandline = "cd | comm - nofile.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testMove() {
		String commandline = "move testCase_2.txt lvl1folder | comm - testCase_2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);

	}

	@Test
	public void testMove2() {
		String commandline = "move testCase_3.txt | comm -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCopy() {
		String commandline = "copy testCase_2.txt lvl1folder | comm -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCopy2() {
		String commandline = "copy testCase_2.txt  | comm -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testDelete() throws IOException {
		String commandline = "delete testCase_5.txt | comm - testCase_2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
		File f = new File(rootParent, "testCase_5.txt");
		f.createNewFile();
		create(f.getAbsolutePath(), "apple\nban\nbanana\ncarrot\ncarro\nc");
	}

	@Test
	public void testDelete2() {
		String commandline = "delete | comm";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testEcho() {
		String commandline = "echo aab | comm - commTestCase1b.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "\t\taaa" + "\naab" + "\n\t\tbbb" + "\n\t\tffff";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testEcho2() {
		String commandline = "echo | comm -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testWc() {
		// breaking
		String commandline = "wc -l testCase_3.txt | comm - testCase_2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "5\ttestCase_3.txt" +
				"\n\t\tgek1517" +
				"\n\t\tACC1002X" +
				"\n\t\tsw2104" +
				"\n\t\tpc1141";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | comm - testCase_2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f -1 testCase_3.txt | comm - testCase_2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		System.out.println(output);
		String expectedOutput = "a\n\t\tgek1517\n\t\tACC1002X\nb\nc\n\t\tsw2104\n\t\tpc1141";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -1 test2.txt | comm ";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testPaste2() {
		String commandline = "paste commTestCase1a.txt | comm - commTestCase1b.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "\t\t\t\taaa" + "\n\t\tbbb" + "\nccc" + "\neee"
				+ "\n\t\tffff" + "\ngggggg";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCut2() {
		// check this one
		String commandline = "cut -f -c testCase_2.txt | comm - testCase_3.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testSort() {
		String commandline = "sort -c testCase_3.txt | comm - testCase_2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(new File(rootParent), null);
		String expectedOutput = "In order.\n\t\tgek1517\n\t\tACC1002X\n\t\tsw2104\n\t\tpc1141";
		assertEquals(expectedOutput, output);
	}

	public static void create(String filename, String content) {
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
