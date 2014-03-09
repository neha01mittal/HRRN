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

public class GrepIntegrationTest {

	private static File rootParent;

	@BeforeClass
	public static void init() {
		rootParent = new File(System.getProperty("user.dir"));
	}

	@AfterClass
	public static void afterClass() throws IOException {
	}
	
	@Test
	public void testLs() {
		String commandline = "ls | grep txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "commTestCase1a.txt\n"
				+ "commTestCase1b.txt\n"
				+ "commTestCase2a.txt\n"
				+ "commTestCase2b.txt\n"
				+ "input.txt\n"
				+ "orderdText.txt\n"
				+ "testCase_1.txt\n"
				+ "testCase_2.txt\n"
				+ "testCase_3.txt\n"
				+ "testCase_4.txt\n"
				+ "testCase_5.txt\n"
				+ "uniqTestCase.txt\n"
				+ "uniqTestCase1.txt\n"
				+ "uniqTestCase2.txt\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testPwd() {
		String commandline = "pwd | grep HRRN";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = System.getProperty("user.dir") + "\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testCat() {
		String commandline = "cat testCase_5.txt | grep an";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "ban\n" + "banana\n" ;
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testEcho() {
		String commandline = "echo \"abcdef ghijklmn\" | grep ab";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "abcdef ghijklmn\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testCut() {
		String commandline = "cut -c 2 testCase_5.txt | grep a";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "a\n" + "a\n" + "a\n" + "a\n";
		assertEquals(expectedOutput, output);
		
	}
	
	@Test
	public void testPaste() {
		String commandline = "paste testCase_4.txt testCase_5.txt | grep a";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "apple\tapple\n" + "ban\tcar\n" +
							"banana\tcarrot\n" + "carrot\tban\n" + "carro\tbanana\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testSort() {
		String commandline = "sort testCase_5.txt | grep a";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "apple\n" + "ban\n" + "banana\n" + "car\n" + "carrot\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testUniq() {
		String commandline = "uniq testCase_4.txt | grep HRRN";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(rootParent, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}
}
