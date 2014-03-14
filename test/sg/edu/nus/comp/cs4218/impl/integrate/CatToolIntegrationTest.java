package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class CatToolIntegrationTest {

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
		try {
			file1.createNewFile();
			file2.createNewFile();
			create(file2.getAbsolutePath(),"This is file 2 content");
			create(file1.getAbsolutePath(),"This is test content and more");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TestUtils.delete(f1);
	}

	@Test
	public void testLs() {
		String commandline = "ls | cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "test1.txt\ntest2.txt\ntestFolder2";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testPwd() {
		String commandline = "pwd | cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = f1.getAbsolutePath();
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testPwd2() {
		String commandline = "pwd rubbishvalue | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output= pipingTool.execute(f1, null);
		String expectedOutput = f1.getAbsolutePath();
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCd() {
		String commandline = "cd .. | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output=pipingTool.execute(f2, null);
		System.out.println(output);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCd2() {
		String commandline = "cd "+f2+"| cat test1.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output=pipingTool.execute(f1, null);
		String expectedOutput = "This is test content and more\n";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testMove() throws IOException {
		String commandline = "move " + file1 + " " + f1 + "| cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output=pipingTool.execute(f1, null);
		System.out.println(output);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
		file1.createNewFile();
	}

	@Test
	public void testMove2() {
		String commandline = "move " + file1 +  "| cat test1.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is test content and more\n";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCopy() {
		String commandline = "copy  | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCopy2() {
		String commandline = "copy test1.txt testFolder2 | cat test2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is file 2 content\n";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testDelete() throws IOException {
		String commandline = "delete " + file1.getName() + "| cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f3, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
		file1.createNewFile();
		create(file1.getAbsolutePath(),"This is test content and more\n");
	}
	@Test
	public void testDelete2() {
		String commandline = "delete testFolder2 | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
		f2.mkdirs();
	}

	@Test
	public void testEcho() {
		String commandline = "echo This is test | cat test2.txt";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is file 2 content\n";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testEcho2() {
		String commandline = "echo | cat - repeat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "Error: No such file or directory";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCat() {
		String commandline = "cat " + file2 + "| cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is file 2 content"+ "\n";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCat2() {
		String commandline = "cat "+ file1 +" "+ file2+" | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is test content and more\nThis is file 2 content\n";
		assertEquals(expectedOutput, output);
	}
	
	@Test
	public void testWc() {
		// breaking
		String commandline = "wc -l test1.txt | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		System.out.println(output);
		String expectedOutput = "0 test1.txt";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f -1 test1.txt | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		System.out.println(output);
		String expectedOutput = "This is test content and more";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -1 test2.txt | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is file 2 content";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testPaste() {
		String commandline = "paste " + file1 + " " + file2 + "| cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is test content and more	This is file 2 content";
		assertEquals(expectedOutput, output);
	} 

	@Test
	public void testPaste2() {
		String commandline = "paste " + file1 + "| cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "This is test content and more";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCut() {
		String commandline = "cut -f "+file1+ "| cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testCut2() {
		String commandline = "cut -f -c "+file1+ "| cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testSort() {
		String commandline = "sort -c " + file1 + "| cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "In order.";
		assertEquals(expectedOutput, output);
	}
	@Test
	public void testSort2() {
		String commandline = "sort | cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		String expectedOutput = "";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testComm() {
		String commandline = "comm test1.txt test2.txt | cat -";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		System.out.println(output);
		String expectedOutput = "\t\tThis is file 2 content\nThis is test content and more";
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testComm2() {
		String commandline = "comm -d test1.txt test2.txt | cat";
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		String output = pipingTool.execute(f1, null);
		System.out.println(output);
		String expectedOutput = "\t\tThis is file 2 content\nThis is test content and more";
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
