package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class CopyToolIntegrationTest {

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
		f3 = new File(f2, "testFolder3");
		f1.mkdir();
		f2.mkdir();
		f3.mkdir();
	}

	@AfterClass
	public static void afterClass() throws IOException {
		TestUtils.delete(f1);
	}

	@Before
	public void beforeFunction() throws IOException {
		file1 = new File(f1, "test1.txt");
		file2 = new File(f1, "test2.txt");
		create(file1.toString(), "Something");
	}

	@After
	public void afterFunction() throws IOException {
		file1.delete();
		file2.delete();
	}

	@Test
	public void testLs() {
		String commandline = "ls | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testPwd() {
		String commandline = "pwd | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testPwd2() {
		String commandline = "pwd rubbishvalue | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCd() {
		String commandline = "cd .. | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCd2() {
		String commandline = "cd " + f2 + "| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testMove() {
		String commandline = "move " + file1 + " " + file2 + "| copy " + file2
				+ " " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testMove2() {
		String commandline = "move " + file1 + "| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCopy() {
		String commandline = "copy  | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCopy2() {
		String commandline = "copy " + file2 + " " + file1 + " | copy " + file1
				+ " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testDelete() {
		String commandline = "delete " + file2 + "| copy " + file1 + " "
				+ file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testDelete2() {
		String commandline = "delete testFolder2 | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
		f2.mkdirs();
	}

	@Test
	public void testEcho() {
		String commandline = "echo This is test| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testEcho2() {
		String commandline = "echo | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCat() {
		String commandline = "cat " + file1 + "| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCat2() {
		String commandline = "cat -" + file1 + "| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testWc() {
		String commandline = "wc -l " + file1 + "| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f " + file1 + "| copy " + file1 + " "
				+ file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -l " + file1 + " | copy " + file1 + " "
				+ file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testPaste() {
		String commandline = "paste " + file1 + " " + file2 + "| copy " + file1
				+ " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testPaste2() {
		String commandline = "paste " + file1 + "| copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCut() {
		String commandline = "cut -c 1,2 " + file1 + "| copy " + file1 + " "
				+ file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testCut2() {
		String commandline = "cut -f -c " + file1 + "| copy " + file1 + " "
				+ file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testSort() {
		String commandline = "sort -c " + file1 + "| copy " + file1 + " "
				+ file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testSort2() {
		String commandline = "sort | copy " + file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testComm() {
		String commandline = "comm " + file1 + " " + file2 + "| copy " + file1
				+ " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	@Test
	public void testComm2() {
		String commandline = "comm -d " + file1 + " " + file2 + "| copy "
				+ file1 + " " + file2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertTrue(compare(file1, file2));
	}

	public void create(String filename, String content) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "utf-8"));
			writer.write(content);
			writer.close();
		} catch (IOException ex) {
			// report
		}
	}

	public boolean compare(File file1, File file2) {

		String s1 = "";
		String s3 = "";
		String y = "", z = "";
		try {
			@SuppressWarnings("resource")
			BufferedReader bfr = new BufferedReader(new FileReader(file1));
			@SuppressWarnings("resource")
			BufferedReader bfr1 = new BufferedReader(new FileReader(file2));
			while ((z = bfr1.readLine()) != null)
				s3 += z;

			while ((y = bfr.readLine()) != null)
				s1 += y;
		} catch (Exception e) {
			return false;
		}

		if (s3.equals(s1)) {
			return true;
		} else {
			return false;
		}

	}
}
