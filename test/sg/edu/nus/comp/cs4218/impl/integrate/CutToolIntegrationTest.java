package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

public class CutToolIntegrationTest {

	// ls, pwd, cd, copy, delete, move, paste, comm, grep, wc, uniq, sort, copy

	private static File f1;
	private static File file1;
	private static File file2;

	@BeforeClass
	public static void init() {
		f1 = new File(System.getProperty("user.dir"));
		file1 = new File("input.txt");
		f1.mkdir();
	}


	@Test
	public void testLs() {
		String commandline = "ls | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}
	
	@Test
	public void testPwd() {
		String commandline = "pwd | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "Error: No such file or directory");
	}

	@Test
	public void testPwd2() {
		String commandline = "pwd rubbishvalue | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "Error: No such file or directory");
	}

	@Test
	public void testCd() {
		String commandline = "cd .. | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testCd2() {
		String commandline = "cd .. | cd .. | cut -c 2-4,1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1234\n1234\n1234\n1234");
	}

	@Test
	public void testMove() {
		String commandline = "move " + file1 + " " + f1 + "| cut -d 3 -f 1 " + file1; 
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}


	@Test
	public void testCopy() {
		String commandline = "copy  | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testCopy2() {
		String commandline = "copy " + file1 + " " + f1 + " | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testDelete() {
		String commandline = "delete " + file2 + "| cut -f 1-2,2-5 -d 3 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12345\n1234567\n123456789\n12345");
	}
		
	@Test
	public void testDelete2() {
		String commandline = "delete testFolder2 | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testEcho() {
		String commandline = "echo This is test| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "Error: No such file or directory");
	}

	@Test
	public void testEcho2() {
		String commandline = "echo " + file1 + " | cut -f 1-2,2-5 -d 3 " ;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12345\n1234567\n123456789\n12345");
	}

	@Test
	public void testCat() {
		String commandline = "cat " + file1 + "| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}

	@Test
	public void testCat2() {
		String commandline = "cat -" + file1 + "| cut -d 3 -f 1";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}
	
	@Test
	public void testWc() {
		String commandline = "wc -l "+file1+"| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | cut -d 3 -f 1 " + file1; 
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f "+file1+"| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -l "+file1+" | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testPaste() {
		String commandline = "paste " + file1 + " " + file2 + "| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	} 

	@Test
	public void testPaste2() {
		String commandline = "paste " + file1 + "| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}

	@Test
	public void testCut() {
		String commandline = "cut -c 1,2 "+file1+ "| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}

	@Test
	public void testCut2() {
		String commandline = "cut -f -c "+file1+ "| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testSort() {
		String commandline = "sort -c " + file1 + "| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}
	@Test
	public void testSort2() {
		String commandline = "sort | cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testComm() {
		String commandline = "comm "+file1+" "+file2+"| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}

	@Test
	public void testComm2() {
		String commandline = "comm -d "+file1+" "+file2+"| cut -d 3 -f 1 " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "12\n12\n12\n12");
	}
	
}
