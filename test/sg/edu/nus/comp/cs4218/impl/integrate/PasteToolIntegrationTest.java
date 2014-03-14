package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;

public class PasteToolIntegrationTest {

	// ls, pwd, cd, copy, delete, move, paste, comm, grep, wc, uniq, sort, copy

	private static File f1;
	private static File file1;
	private static File file2;

	@BeforeClass
	public static void init() {
		f1 = new File(System.getProperty("user.dir"));
		file1 = new File("testCase_1.txt");
		file2 = new File("testCase_2.txt");
		f1.mkdir();
	}


	@Test
	public void testLs() {
		String commandline = "ls | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}
	
	@Test
	public void testPwd() {
		String commandline = "pwd | paste ";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "Error: No such file or directory");
	}


	@Test
	public void testCd() {
		String commandline = "cd .. | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testCd2() {
		String commandline = "cd .. | cd .. | paste -s " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141");
	}

	@Test
	public void testMove() {
		String commandline = "move " + file1 + " " + f1 + "| paste " + file1 + " " + file2; 
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}


	@Test
	public void testCopy() {
		String commandline = "copy  | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testCopy2() {
		File file3 = new File("test.txt");
		String commandline = "copy " + file1 + " " + file3 + " | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
		file3.delete();
	}

	@Test
	public void testDelete() {
		File file3 = new File("test.txt");
		String commandline = "delete " + file3 + "| paste -d ' @ ' " + file1 + " " + file2 ;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM @ gek1517\n" + "2. Symantec @ ACC1002X\n" + "3. Palantir @ sw2104\n" + "10. hp @ pc1141\n" + "11. ihis");
	}
		
	@Test
	public void testDelete2() {
		String commandline = "delete testFolder2 | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testEcho() {
		String commandline = "echo This is test| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testEcho2() {
		String commandline = "echo " + file2 + " | paste -d @ " + file1 + " - " ;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\n" + "2. Symantec\n" + "3. Palantir\n" + "10. hp\n" + "11. ihis");
	}
	
	@Test
	public void testEcho3() {
		String commandline = "echo " + file1 + " " + file2 + " | paste -d @ " ;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "");
	}
	
	@Test
	public void testCat() {
		String commandline = "cat " + file1 + "| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testCat2() {
		String commandline = "cat " + file1 + "| cut -d 3 -f 1 - ";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\n" + "2. Symantec\n" + "3. Palantir\n" + "10. hp\n" + "11. ihis\n");
	}
	
	@Test
	public void testWc() {
		String commandline = "wc -l "+file1+"| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | paste " + file1 + " " + file2; 
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f "+file1+"| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -l "+file1+" | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testPaste() {
		String commandline = "paste " + file1 + " " + file2 + "| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	} 

	@Test
	public void testPaste2() {
		String commandline = "paste " + file1 + "| paste " + file1 + " - " ;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\n" + "2. Symantec\n" + "3. Palantir\n" + "10. hp\n" + "11. ihis\t" + "1. IBM\n" + "2. Symantec\n" + "3. Palantir\n" + "10. hp\n" + "11. ihis");
	}

	@Test
	public void testCut() {
		String commandline = "cut -c 1,2 "+file1+ "| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testCut2() {
		String commandline = "cut -f -c "+file1+ "| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testSort() {
		String commandline = "sort -c " + file1 + "| paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}
	@Test
	public void testSort2() {
		String commandline = "sort | paste " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testComm() {
		String commandline = "comm "+file1+" "+file2+"| paste " + file1 + " - ";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		assertEquals(output, "1. IBM\n\t\tgek1517\n2. Symantec\n\t\tACC1002X\n3. Palantir\n\t\tsw2104\n10. hp\n\t\tpc1141\n11. ihis\t1. IBM\n2. Symantec\n3. Palantir\n10. hp\n11. ihis");
	}


	
}
