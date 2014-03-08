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
	private static File f1;
	private static File f2;
	private static File f3;
	private static File file1;
	private static File file2;

	@BeforeClass
	public static void init() {
		rootParent = new File(System.getProperty("user.dir"));
		f1 = new File(rootParent, "testFolder");
		f2 = new File(f1, "testFolder2");
		file1 = new File(f1, "test1.txt");
		file2 = new File(f1, "test2.txt");
		f3 = new File(f2,"testFolder3");
		f1.mkdir();
		f2.mkdir();
		f3.mkdir();
		file1.mkdir();
		file2.mkdir();
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TestUtils.delete(f1);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testLs() {
		String commandline = "ls | grep *.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "test\n"
				+ "testCase_1.txt\n"
				+ "testCase_2.txt\n"
				+ "testCase_3.txt\n"
				+ "testCase_4.txt\n"
				+ "testCase_5.txt\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testPwd() {
		String commandline = "pwd | grep HRRN";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = System.getProperty("user.dir");
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testCat() {
		String commandline = "cat testCase_5.txt | grep an";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "ban\n" + "banana\n" ;
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testEcho() {
		String commandline = "echo \"abcdef \\n \\n \\n ghijklmn\" | grep \\n";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "abcdef \\n \\n \\n ghijklmn";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testCut() {
		String commandline = "cut -c 2 testCase_5.txt | grep a";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "a\n" + "a\n" + "a\n" + "a\n";
		assertEquals(expectedOutput, output);
		
	}
	
	@Test
	public void testPaste() {
		String commandline = "paste testCase_4.txt testCase_5.txt | grep a";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "apple\tapple\n" + "ban\tcar\n" +
							"banana\tcarrot\n" + "carrot\tban\n" + "carro\tbanana\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testSort() {
		String commandline = "sort testCase_5.txt | grep a";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "apple\n" + "ban\n" + "banana\n" + "car\n" + "carrot\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testUniq() {
		String commandline = "uniq testCase_4.txt | grep HRRN";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "apple\n" + "ban\n" + "banana\n" + "carrot\n" + "car\n" + "c\n";
		assertEquals(expectedOutput, output);
	}
}
