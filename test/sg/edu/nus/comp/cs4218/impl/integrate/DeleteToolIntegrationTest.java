package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertFalse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class DeleteToolIntegrationTest {

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
		create(file1.toString(), "Something");
	}

	@Test
	public void testLs() {
		String commandline = "ls | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testPwd() {
		String commandline = "pwd | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testPwd2() {
		String commandline = "pwd rubbishvalue | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCd() {
		String commandline = "cd .. | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCd2() {
		String commandline = "cd " + f2 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testMove() {
		String commandline = "move " + file1 + " " + f2 + "| delete " + f2;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(f2.exists());
		f2.mkdirs();
	}

	@Test
	public void testMove2() {
		String commandline = "move " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCopy() {
		String commandline = "copy  | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCopy2() {
		file2 = new File(f1, "test2.txt");
		create(file1.toString(), "Something");
		String commandline = "copy " + file2 + " " + file1 + " | delete "
				+ file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
		file2.delete();
	}

	@Test
	public void testDelete() {
		file2 = new File(f1, "test2.txt");
		create(file1.toString(), "Something");
		String commandline = "delete " + file2 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testDelete2() {
		String commandline = "delete testFolder2 | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
		f2.mkdirs();
	}

	@Test
	public void testEcho() {
		String commandline = "echo This is test| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testEcho2() {
		String commandline = "echo | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCat() {
		String commandline = "cat " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCat2() {
		String commandline = "cat -" + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testWc() {
		String commandline = "wc -l " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testWc2() {
		String commandline = "wc -x test1.txt | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testUniq() {
		String commandline = "uniq -f " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testUniq2() {
		String commandline = "uniq -f -l " + file1 + " | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testPaste() {
		String commandline = "paste " + file1 + " " + file2 + "| delete "
				+ file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testPaste2() {
		String commandline = "paste " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCut() {
		String commandline = "cut -c 1,2 " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testCut2() {
		String commandline = "cut -f -c " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testSort() {
		String commandline = "sort -c " + file1 + "| delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testSort2() {
		String commandline = "sort | delete " + file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testComm() {
		String commandline = "comm " + file1 + " " + file2 + "| delete "
				+ file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
	}

	@Test
	public void testComm2() {
		String commandline = "comm -d " + file1 + " " + file2 + "| delete "
				+ file1;
		PipingTool pipingTool = new PipingTool(commandline.split("\\|"));
		pipingTool.execute(f1, null);
		assertFalse(file1.exists());
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
}
