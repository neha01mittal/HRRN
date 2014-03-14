package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

public class WcToolIntegrationTest {

	private static File rootParent;	

	@BeforeClass
	public static void init() {
		rootParent = new File(System.getProperty("user.dir"));
	}

	@AfterClass
	public static void afterClass() {
	}

	@Test
	public void testCat() {
		String commandline = "cat testCase_1.txt | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "5";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testLs() {
		String commandline = "ls | wc -l";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "23";
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
	
	@Test
	public void testGeneralOptionWithPipe() {
		String commandline = "cat testCase_4.txt | wc";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "\t6\t6\t32";
		assertEquals(expectedOutput, output);
	}
}