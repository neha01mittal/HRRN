package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class CdToolIntegrationTest {

	// ls, pwd, cd, copy, delete, move, paste, comm, grep, wc, uniq, sort, copy

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
	public void testLs() {
		String commandline = "ls | cd testFolder2";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		String expectedOutput = f2.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}
	
	@Test
	public void testPwd() {
		String commandline = "pwd | cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testPwd2() {
		String commandline = "pwd rubbishvalue | cd "+f1.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		String expectedOutput = f1.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCd() {
		String commandline = "cd .. | cd ..";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f2, null);
		String expectedOutput = f1.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCd2() {
		String commandline = "cd "+f2+"| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testMove() {
		String commandline = "move " + file1 + " " + f1 + "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
		file1.mkdir();
	}

	@Test
	public void testMove2() {
		String commandline = "move " + file1 +  "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCopy() {
		String commandline = "copy  | cd "+f2.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f2.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCopy2() {
		String commandline = "copy test1.txt testFolder2 | cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testDelete() {
		String commandline = "delete " + file1 + "| cd ..";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f3, null);
		String expectedOutput = f2.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
		file1.mkdir();
	}
	@Test
	public void testDelete2() {
		String commandline = "delete testFolder2 | cd "+f1.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f1.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
		f2.mkdirs();
	}

	@Test
	public void testEcho() {
		String commandline = "echo This is test| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testEcho2() {
		String commandline = "echo | cd "+f2.getName();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f2.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCat() {
		String commandline = "cat " + file1 + "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCat2() {
		String commandline = "cat " + file1 + file2+"| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}
	
	@Test
	public void testWc() {
		String commandline = "wc -l "+file1+"| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f "+file1+"| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -l "+file1+" | cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testPaste() {
		String commandline = "paste " + file1 + " " + file2 + "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	} 

	@Test
	public void testPaste2() {
		String commandline = "paste " + file1 + "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCut() {
		String commandline = "cut -f "+file1+ "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testCut2() {
		String commandline = "cut -f -c "+file1+ "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testSort() {
		String commandline = "sort -c " + file1 + "| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}
	@Test
	public void testSort2() {
		String commandline = "sort | cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testComm() {
		String commandline = "comm "+file1+" "+file2+"| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}

	@Test
	public void testComm2() {
		String commandline = "comm -d "+file1+" "+file2+"| cd "+f3.getAbsolutePath();
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f3.getAbsolutePath();
		assertEquals(expectedOutput, System.getProperty("user.dir"));
	}
}
