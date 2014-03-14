package sg.edu.nus.comp.cs4218.impl.extended2;

/*
 * Changes Made
 * 
 * Separated functions so that each one has only one assert statement
 * The default case with no option was not returning the correct output. It expected contents of files printed sequentially instead of in parallel
 * The -d option was not returning the correct output. It expected contents of files printed sequentially instead of in parallel
 * The -s option had a trailing \t instead of a newline
 */
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;

public class PasteToolTest {
	private IPasteTool pastetool;
	private IPasteTool pastetool2;
	private IPasteTool pastetool3;
	private IPasteTool pastetool4;
	String[] fileArray1 = { "testCase_1.txt" };
	String[] fileArray2 = { "testCase_1.txt", "testCase_2.txt" };
	String[] fileArray3 = { "testCase_1.txt", "testCase_2.txt", "testCase_3.txt" };
	String[] fileArray4 = { "testCase_1.txt", "testCase_2.txt", "testCase_3.txt", "testCase_4.txt" };

	private final File f = new File(System.getProperty("user.dir"));

	@Before
	public void before() {
		pastetool = new PasteTool(null);
	}

	@After
	public void after() {
		pastetool = null;
	}

	@Test
	public void pasteDefaultTest() {
		pastetool = new PasteTool(fileArray1);
		assertEquals(pastetool.execute(null, null), "1. IBM\n2. Symantec\n3. Palantir\n10. hp\n11. ihis");

	}

	@Test
	public void pasteDefaultTest2() {
		pastetool = new PasteTool(fileArray2);
		assertEquals(pastetool.execute(null, null), "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void pasteDefaultTest3() {
		pastetool = new PasteTool(fileArray3);
		assertEquals(pastetool.execute(null, null), "1. IBM\tgek1517\ta\n" + "2. Symantec\tACC1002X\tb\n" + "3. Palantir\tsw2104\tb\n" + "10. hp\tpc1141\tc\n"
				+ "11. ihis\tc\n" + "c");
	}

	@Test
	public void pasteDefaultTest4() {
		pastetool = new PasteTool(fileArray4);
		assertEquals(pastetool.execute(null, null), "1. IBM\tgek1517\ta\tapple\n" + "2. Symantec\tACC1002X\tb\tban\n" + "3. Palantir\tsw2104\tb\tbanana\n"
				+ "10. hp\tpc1141\tc\tcarrot\n" + "11. ihis\tc\tcarro\n" + "c\tc");

	}

	// test the functionality of pasting in serial for the single method and the
	// execute which calls the single method
	@Test
	public void pasteSerialTest() {
		// assertEquals(pastetool.pasteSerial(fileArray1),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t");
		assertEquals(pastetool.pasteSerial(fileArray1), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis");

	}

	@Test
	public void pasteSerialTest2() {
		// assertEquals(pastetool.pasteSerial(fileArray2),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\t");
		assertEquals(pastetool.pasteSerial(fileArray2), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141");
	}

	@Test
	public void pasteSerialTest3() {
		// assertEquals(pastetool.pasteSerial(fileArray3),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\t");
		assertEquals(pastetool.pasteSerial(fileArray3), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141\n"
				+ "a\tb\tb\tc\tc\tc");
	}

	@Test
	public void pasteSerialTest4() {
		// assertEquals(pastetool.pasteSerial(fileArray4),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\tapple\tban\tbanana\tcarrot\tcarro\tc\t");
		assertEquals(pastetool.pasteSerial(fileArray4), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141\n"
				+ "a\tb\tb\tc\tc\tc\n" + "apple\tban\tbanana\tcarrot\tcarro\tc");

	}

	@Test
	public void pasteSerialTest5() {
		String[] args2 = { "-s", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		// assertEquals(pastetool2.execute(null, null),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t");
		assertEquals(pastetool2.execute(null, null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis");
	}

	@Test
	public void pasteSerialTest6() {
		String[] args3 = { "-s", "testCase_1.txt", "testCase_2.txt" };
		pastetool3 = new PasteTool(args3);
		// assertEquals(pastetool3.execute(null, null),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\t");
		assertEquals(pastetool3.execute(null, null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141");

	}

	@Test
	public void pasteSerialTest7() {
		pastetool3 = new PasteTool(null);
		// assertEquals(pastetool.execute(f,
		// "-s testCase_1.txt testCase_2.txt testCase_3.txt"),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
		// + "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\t");
		assertEquals(pastetool.execute(f, "-s testCase_1.txt testCase_2.txt testCase_3.txt"), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n"
				+ "gek1517\tACC1002X\tsw2104\tpc1141\n" + "a\tb\tb\tc\tc\tc");

	}

	@Test
	public void pasteSerialTest8() {
		pastetool3 = new PasteTool(null);
		// assertEquals(pastetool.execute(f,
		// "-s testCase_1.txt testCase_2.txt testCase_3.txt testCase_4.txt"),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
		// +
		// "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\tapple\tban\tbanana\tcarrot\tcarro\tc\t");
		assertEquals(pastetool.execute(f, "-s testCase_1.txt testCase_2.txt testCase_3.txt testCase_4.txt"),
				"1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141\n" + "a\tb\tb\tc\tc\tc\n"
						+ "apple\tban\tbanana\tcarrot\tcarro\tc");
	}

	// test the functionality of pasting with specified delimiter for the single
	// method and
	// the execute which calls the single method
	@Test
	public void pasteUseDelimeterTest() {
		// assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray1),
		// "1. IBM $ 2. Symantec $ " + "3. Palantir $ 10. hp $ 11. ihis $ ");
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray1), "1. IBM\n2. Symantec\n" + "3. Palantir\n10. hp\n11. ihis");
	}

	@Test
	public void pasteUseDelimeterTest2() {
		// assertEquals(pastetool.pasteUseDelimiter(" @ ", fileArray2),
		// "1. IBM @ 2. Symantec @ "
		// +
		// "3. Palantir @ 10. hp @ 11. ihis @ gek1517 @ ACC1002X @ sw2104 @ pc1141 @ ");
		assertEquals(pastetool.pasteUseDelimiter(" @ ", fileArray2), "1. IBM @ gek1517\n" + "2. Symantec @ ACC1002X\n" + "3. Palantir @ sw2104\n"
				+ "10. hp @ pc1141\n" + "11. ihis");
	}

	@Test
	public void pasteUseDelimeterTest3() {
		// assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray3),
		// "1. IBM $ 2. Symantec $ "
		// +
		// "3. Palantir $ 10. hp $ 11. ihis $ gek1517 $ ACC1002X $ sw2104 $ pc1141 $"
		// + " a $ b $ b $ c $ c $ c $ ");
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray3), "1. IBM $ gek1517 $ a\n" + "2. Symantec $ ACC1002X $ b\n" + "3. Palantir $ sw2104 $ b\n"
				+ "10. hp $ pc1141 $ c\n" + "11. ihis $ c\n" + "c");

	}

	@Test
	public void pasteUseDelimeterTest4() {
		// assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray4),
		// "1. IBM $ 2. Symantec $"
		// +
		// " 3. Palantir $ 10. hp $ 11. ihis $ gek1517 $ ACC1002X $ sw2104 $ pc1141 $"
		// +
		// " a $ b $ b $ c $ c $ c $ apple $ ban $ banana $ carrot $ carro $ c $ ");
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray4), "1. IBM $ gek1517 $ a $ apple\n" + "2. Symantec $ ACC1002X $ b $ ban\n"
				+ "3. Palantir $ sw2104 $ b $ banana\n" + "10. hp $ pc1141 $ c $ carrot\n" + "11. ihis $ c $ carro\n" + "c $ c");
	}

	@Test
	public void pasteUseDelimeterTest5() {
		String[] args2 = { "-d", " $ ", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		// assertEquals(pastetool2.execute(null, null),
		// "1. IBM $ 2. Symantec $ " + "3. Palantir $ 10. hp $ 11. ihis $ ");
		assertEquals(pastetool2.execute(null, null), "1. IBM\n2. Symantec\n" + "3. Palantir\n10. hp\n11. ihis");
	}

	@Test
	public void pasteUseDelimeterTest6() {
		String[] args3 = { "-d", " @ ", "testCase_1.txt", "testCase_2.txt" };
		pastetool3 = new PasteTool(args3);
		// assertEquals(pastetool3.execute(null, null),
		// "1. IBM @ 2. Symantec @ " +
		// "3. Palantir @ 10. hp @ 11. ihis @ gek1517 @ ACC1002X @ sw2104 @ pc1141 @ ");
		assertEquals(pastetool3.execute(null, null), "1. IBM @ gek1517\n" + "2. Symantec @ ACC1002X\n" + "3. Palantir @ sw2104\n" + "10. hp @ pc1141\n"
				+ "11. ihis");
	}

	@Test
	public void pasteUseDelimeterTest7() {
		// assertEquals(pastetool.execute(f,
		// "-d $$$ testCase_1.txt testCase_2.txt testCase_3.txt"),
		// "1. IBM$$$2. Symantec$$$"
		// +
		// "3. Palantir$$$10. hp$$$11. ihis$$$gek1517$$$ACC1002X$$$sw2104$$$pc1141$$$"
		// + "a$$$b$$$b$$$c$$$c$$$c$$$");
		assertEquals(pastetool.execute(f, "-d $$$ testCase_1.txt testCase_2.txt testCase_3.txt"), "1. IBM$$$gek1517$$$a\n" + "2. Symantec$$$ACC1002X$$$b\n"
				+ "3. Palantir$$$sw2104$$$b\n" + "10. hp$$$pc1141$$$c\n" + "11. ihis$$$c\n" + "c");

	}

	@Test
	public void pasteUseDelimeterTest8() {
		// assertEquals(pastetool.execute(f,
		// "-d $$$ testCase_1.txt testCase_2.txt testCase_3.txt testCase_4.txt"),
		// "1. IBM$$$2. Symantec$$$"
		// +
		// "3. Palantir$$$10. hp$$$11. ihis$$$gek1517$$$ACC1002X$$$sw2104$$$pc1141$$$"
		// +
		// "a$$$b$$$b$$$c$$$c$$$c$$$apple$$$ban$$$banana$$$carrot$$$carro$$$c$$$");
		assertEquals(pastetool.execute(f, "-d $$$ testCase_1.txt testCase_2.txt testCase_3.txt testCase_4.txt"), "1. IBM$$$gek1517$$$a$$$apple\n"
				+ "2. Symantec$$$ACC1002X$$$b$$$ban\n" + "3. Palantir$$$sw2104$$$b$$$banana\n" + "10. hp$$$pc1141$$$c$$$carrot\n" + "11. ihis$$$c$$$carro\n"
				+ "c$$$c");

	}

	// test the functionality of printing help message
	@Test
	public void getHelpTest() {
		assertEquals(pastetool.getHelp(), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");

	}

	@Test
	public void getHelpTest2() {
		String[] args2 = { "-help" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");
	}

	@Test
	public void getHelpTest3() {

		assertEquals(pastetool.execute(f, "-help"), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");
	}

	// test if the invalid cases are being handled
	@Test
	public void validationTest() {
		String[] args2 = { "-x", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "Wrong command");

	}

	@Test
	public void validationTest2() {

		String[] args3 = { "-s", "-" };
		pastetool3 = new PasteTool(args3);
		// assertEquals(pastetool3.execute(null, null),
		// "Sorry, piping not implemented yet" +
		// " hence we do not accept standard input.");
		assertEquals(pastetool3.execute(null, null), "");
	}

	@Test
	public void validationTest3() {
		String[] args4 = { "-d", "/", "-" };
		pastetool4 = new PasteTool(args4);
		// assertEquals(pastetool4.execute(null, null),
		// "Sorry, piping not implemented yet" +
		// " hence we do not accept standard input.");
		assertEquals(pastetool4.execute(null, null), "");
	}

	@Test
	public void validationTest4() {
		assertEquals(pastetool.execute(f, null), "No arguments and no standard input.");

	}

}
