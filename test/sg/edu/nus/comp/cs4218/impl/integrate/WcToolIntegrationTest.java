package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class WcToolIntegrationTest {

	private static File rootParent;
	private static File f1;
	private static File f2;
	private static File file1;
	private static File file2;

	@BeforeClass
	public static void init() {
		rootParent = new File(System.getProperty("user.dir"));
//		f1 = new File(rootParent, "testFolder");
//		f2 = new File(f1, "testFolder2");
//		file1 = new File(f1, "test1.txt");
//		file2 = new File(f1, "test2.txt");
//		f1.mkdir();
//		f2.mkdir();
//		file1.mkdir();
//		file2.mkdir();
	}

	@AfterClass
	public static void afterClass() throws IOException {
		//TestUtils.delete(f1);
	}

	@Test
	public void testCat() {
		String commandline = "cat test1.txt | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "19";
	}
	
	@Test
	public void testLs() {
		String commandline = "ls | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "24";
		assertEquals(expectedOutput, output);		
	}

	@Test
	public void testEcho() {
		String commandline = "echo \"aoihoq asdjqowij\" | wc -m";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "16";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testCut() {
		String commandline = "cut -c 2 testCase_5.txt | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "4";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testPaste() {
		String commandline = "paste testCase_4.txt testCase_5.txt | wc -m";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "59";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testSort() {
		String commandline = "sort testCase_5.txt | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "4";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testUniq() {
		String commandline = "uniq testCase_1.txt | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "4";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testWc() {
		String commandline = "ls | wc -l | wc -m";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "2";
		assertEquals(expectedOutput, output);
	}
}