package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;

public class SortToolTest {
	private ISortTool sorttool;
	private ISortTool sorttool2;
	private ISortTool sorttool3;
	private ISortTool sorttool4;

	private final File f = new File(System.getProperty("user.dir"));

	@Before
	public void before() {
		sorttool = new SortTool(null);
	}

	@After
	public void after() {
		sorttool = null;
	}

	// test the functionality of checking if the content in a file is sorted for
	// the single method and
	// the execute which calls the single method
	@Test
	public void checkIfSortedTest() {
		assertEquals(sorttool.checkIfSorted("testCase_1.txt"), "10. hp");
		assertEquals(sorttool.checkIfSorted("testCase_2.txt"), "ACC1002X");
		assertEquals(sorttool.checkIfSorted("testCase_3.txt"), "In order.");
		assertEquals(sorttool.checkIfSorted("testCase_4.txt"), "carro");
		assertEquals(sorttool.checkIfSorted("testCase_5.txt"), "ban");

		String[] args2 = { "-c", "testCase_1.txt" };
		sorttool2 = new SortTool(args2);
		assertEquals(sorttool2.execute(null, null), "10. hp");
		sorttool2 = null;

		String[] args3 = { "-c", "testCase_2.txt" };
		sorttool3 = new SortTool(args3);
		assertEquals(sorttool3.execute(null, null), "ACC1002X");
		sorttool3 = null;

		String[] args4 = { "-c", "testCase_3.txt" };
		sorttool4 = new SortTool(args4);
		assertEquals(sorttool4.execute(null, null), "In order.");
		sorttool4 = null;

		assertEquals(sorttool.execute(f, "-c testCase_4.txt"), "carro");

		assertEquals(sorttool.execute(f, "-c testCase_5.txt"), "ban");

	}

	// test the functionality of sorting the content in a file for the single
	// method and
	// the execute which calls the single method
	@Test
	public void sortFileTest() {
		assertEquals(sorttool.sortFile("testCase_1.txt"), "1. IBM\n10. hp\n11. ihis\n2. Symantec\n3. Palantir\n");
		assertEquals(sorttool.sortFile("testCase_2.txt"), "ACC1002X\ngek1517\npc1141\nsw2104\n");
		assertEquals(sorttool.sortFile("testCase_4.txt"), "apple\nban\nbanana\nc\ncarro\ncarrot\n");
		assertEquals(sorttool.sortFile("testCase_5.txt"), "apple\nban\nbanana\ncar\ncarrot\n");

		String[] args2 = { "testCase_1.txt" };
		sorttool2 = new SortTool(args2);
		assertEquals(sorttool2.execute(null, null), "1. IBM\n10. hp\n11. ihis\n2. Symantec\n3. Palantir\n");

		String[] args3 = { "testCase_2.txt" };
		sorttool3 = new SortTool(args3);
		assertEquals(sorttool3.execute(null, null), "ACC1002X\ngek1517\npc1141\nsw2104\n");

		assertEquals(sorttool.execute(f, "testCase_4.txt"), "apple\nban\nbanana\nc\ncarro\ncarrot\n");

		assertEquals(sorttool.execute(f, "testCase_5.txt"), "apple\nban\nbanana\ncar\ncarrot\n");
	}

	// test the functionality of help
	@Test
	public void testHelp() {
		String[] args2 = { "-help" };
		sorttool2 = new SortTool(args2);
		assertEquals(sorttool2.execute(null, null), "-c : Check whether the given file is already sorted, if it is not all sorted, print a\n"
				+ " diagnostic containing the first line that is out of order\n" + " -help : Brief information about supported options");

		assertEquals(sorttool.getHelp(), "-c : Check whether the given file is already sorted, if it is not all sorted, print a\n"
				+ " diagnostic containing the first line that is out of order\n" + " -help : Brief information about supported options");
		assertEquals(sorttool.execute(f, "-help"), "-c : Check whether the given file is already sorted, if it is not all sorted, print a\n"
				+ " diagnostic containing the first line that is out of order\n" + " -help : Brief information about supported options");
	}

	// test if the invalid cases are being handled
	@Test
	public void validationTest() {
		String[] args2 = { "notExist.txt" };
		sorttool2 = new SortTool(args2);
		assertEquals(sorttool2.execute(null, null), "sort: open failed: notExist.txt: No such file or directory.");
		sorttool2 = null;

		String[] args3 = { "-c", "notExist.txt" };
		sorttool3 = new SortTool(args3);
		assertEquals(sorttool3.execute(null, null), "sort: open failed: notExist.txt: No such file or directory.");
		sorttool3 = null;

		String[] args4 = { "help", "testCase_1.txt" };
		sorttool4 = new SortTool(args4);
		assertEquals(sorttool4.execute(null, null), "Wrong command.");
		sorttool4 = null;

		assertEquals(sorttool.execute(f, null), "No arguments and no standard input.");
	}

}
