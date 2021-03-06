package sg.edu.nus.comp.cs4218.impl.extended2;

/**
 * @Corrected
 * Changes Made
 * 
 * Separated functions so that each one has only one assert statement
 * The default case with no option was not returning the correct output. It expected contents of files printed sequentially instead of in parallel
 * The -d option was not returning the correct output. It expected contents of files printed sequentially instead of in parallel
 * The -s option had a trailing \t instead of a newline
 * Added absolute paths to all the file arrays since the PasteSerial function receives files without the directory
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
	
	private final File f = new File(System.getProperty("user.dir"));
	String[] fileArray1 = { f + File.separator + "testCase_1.txt" };
	String[] fileArray2 = { f + File.separator +  "testCase_1.txt",  f + File.separator + "testCase_2.txt" };
	String[] fileArray3 = {  f + File.separator + "testCase_1.txt",  f + File.separator + "testCase_2.txt",  f + File.separator + "testCase_3.txt" };
	String[] fileArray4 = {  f + File.separator + "testCase_1.txt", f + File.separator +  "testCase_2.txt", f + File.separator +  "testCase_3.txt",  f + File.separator + "testCase_4.txt" };


	@Before
	public void before() {
		pastetool = new PasteTool(null);
	}

	@After
	public void after() {
		pastetool = null;
	}

	@Test
	public void testPasteDefaultTest() {
		pastetool = new PasteTool(fileArray1);
		assertEquals(pastetool.execute(null, null), "1. IBM\n2. Symantec\n3. Palantir\n10. hp\n11. ihis");

	}

	@Test
	public void testPasteDefaultTest2() {
		pastetool = new PasteTool(fileArray2);
		assertEquals(pastetool.execute(null, null), "1. IBM\tgek1517\n" + "2. Symantec\tACC1002X\n" + "3. Palantir\tsw2104\n" + "10. hp\tpc1141\n" + "11. ihis");
	}

	@Test
	public void testPasteDefaultTest3() {
		pastetool = new PasteTool(fileArray3);
		assertEquals(pastetool.execute(null, null), "1. IBM\tgek1517\ta\n" + "2. Symantec\tACC1002X\tb\n" + "3. Palantir\tsw2104\tb\n" + "10. hp\tpc1141\tc\n"
				+ "11. ihis\tc\n" + "c");
	}

	@Test
	public void testPasteDefaultTest4() {
		pastetool = new PasteTool(fileArray4);
		assertEquals(pastetool.execute(null, null), "1. IBM\tgek1517\ta\tapple\n" + "2. Symantec\tACC1002X\tb\tban\n" + "3. Palantir\tsw2104\tb\tbanana\n"
				+ "10. hp\tpc1141\tc\tcarrot\n" + "11. ihis\tc\tcarro\n" + "c\tc");

	}

	/**
	 * @Corrected Reasons on top of file
	 */
	// test the functionality of pasting in serial for the single method and the
	// execute which calls the single method
	@Test
	public void testPasteSerialTest() {
		// assertEquals(pastetool.pasteSerial(fileArray1),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t");
		assertEquals(pastetool.pasteSerial(fileArray1), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void pasteSerialTest2() {
		// assertEquals(pastetool.pasteSerial(fileArray2),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\t");
		assertEquals(pastetool.pasteSerial(fileArray2), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteSerialTest3() {
		// assertEquals(pastetool.pasteSerial(fileArray3),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\t");
		assertEquals(pastetool.pasteSerial(fileArray3), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141\n"
				+ "a\tb\tb\tc\tc\tc");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteSerialTest4() {
		// assertEquals(pastetool.pasteSerial(fileArray4),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\tapple\tban\tbanana\tcarrot\tcarro\tc\t");
		assertEquals(pastetool.pasteSerial(fileArray4), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141\n"
				+ "a\tb\tb\tc\tc\tc\n" + "apple\tban\tbanana\tcarrot\tcarro\tc");

	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void pasteSerialTest5() {
		String[] args2 = { "-s", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		// assertEquals(pastetool2.execute(null, null),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t");
			assertEquals(pastetool2.execute(f, null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteSerialTest6() {
		String[] args3 = { "-s", "testCase_1.txt", "testCase_2.txt" };
		pastetool3 = new PasteTool(args3);
		// assertEquals(pastetool3.execute(null, null),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" +
		// "gek1517\tACC1002X\tsw2104\tpc1141\t");
		assertEquals(pastetool3.execute(f, null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141");

	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteSerialTest7() {
		pastetool3 = new PasteTool(null);
		// assertEquals(pastetool.execute(f,
		// "-s testCase_1.txt testCase_2.txt testCase_3.txt"),
		// "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
		// + "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\t");
		String[] args = {"-s", "testCase_1.txt", "testCase_2.txt", "testCase_3.txt"};
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f,null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n"
				+ "gek1517\tACC1002X\tsw2104\tpc1141\n" + "a\tb\tb\tc\tc\tc");

	}

	@Test
	public void testPasteSerialTest8() {
		pastetool3 = new PasteTool(null);
			String[] args = {"-s", "testCase_1.txt", "testCase_2.txt", "testCase_3.txt", "testCase_4.txt"};
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, null),
				"1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\n" + "gek1517\tACC1002X\tsw2104\tpc1141\n" + "a\tb\tb\tc\tc\tc\n"
						+ "apple\tban\tbanana\tcarrot\tcarro\tc");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	// test the functionality of pasting with specified delimiter for the single
	// method and
	// the execute which calls the single method
	@Test
	public void testPasteUseDelimeterTest() {
		
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray1), "1. IBM\n2. Symantec\n" + "3. Palantir\n10. hp\n11. ihis");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteUseDelimeterTest2() {
		assertEquals(pastetool.pasteUseDelimiter(" @ ", fileArray2), "1. IBM @ gek1517\n" + "2. Symantec @ ACC1002X\n" + "3. Palantir @ sw2104\n"
				+ "10. hp @ pc1141\n" + "11. ihis");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteUseDelimeterTest3() {
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray3), "1. IBM $ gek1517 $ a\n" + "2. Symantec $ ACC1002X $ b\n" + "3. Palantir $ sw2104 $ b\n"
				+ "10. hp $ pc1141 $ c\n" + "11. ihis $ c\n" + "c");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteUseDelimeterTest4() {
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray4), "1. IBM $ gek1517 $ a $ apple\n" + "2. Symantec $ ACC1002X $ b $ ban\n"
				+ "3. Palantir $ sw2104 $ b $ banana\n" + "10. hp $ pc1141 $ c $ carrot\n" + "11. ihis $ c $ carro\n" + "c $ c");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteUseDelimeterTest5() {
		String[] args2 = { "-d", " $ ", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "1. IBM\n2. Symantec\n" + "3. Palantir\n10. hp\n11. ihis");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteUseDelimeterTest6() {
		String[] args3 = { "-d", " @ ", "testCase_1.txt", "testCase_2.txt" };
		pastetool3 = new PasteTool(args3);
			assertEquals(pastetool3.execute(null, null), "1. IBM @ gek1517\n" + "2. Symantec @ ACC1002X\n" + "3. Palantir @ sw2104\n" + "10. hp @ pc1141\n"
				+ "11. ihis");
	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testPasteUseDelimeterTest7() {
		String[] args = {"-d","$$$", "testCase_1.txt", "testCase_2.txt", "testCase_3.txt"};
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, null), "1. IBM$$$gek1517$$$a\n" + "2. Symantec$$$ACC1002X$$$b\n"
				+ "3. Palantir$$$sw2104$$$b\n" + "10. hp$$$pc1141$$$c\n" + "11. ihis$$$c\n" + "c");

	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void pasteUseDelimeterTest8() {
			String[] args = {"-d","$$$", "testCase_1.txt", "testCase_2.txt", "testCase_3.txt", "testCase_4.txt"};
		pastetool = new PasteTool(args);
		
		assertEquals(pastetool.execute(f, null ), "1. IBM$$$gek1517$$$a$$$apple\n"
				+ "2. Symantec$$$ACC1002X$$$b$$$ban\n" + "3. Palantir$$$sw2104$$$b$$$banana\n" + "10. hp$$$pc1141$$$c$$$carrot\n" + "11. ihis$$$c$$$carro\n"
				+ "c$$$c");

	}

	/**
	 * @Corrected Reasons on top of file
	 */
	// test the functionality of printing help message
	@Test
	public void testGetHelpTest() {
		assertEquals(pastetool.getHelp(), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");

	}

	@Test
	public void testGetHelpTest2() {
		String[] args2 = { "-help" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");
	}

	@Test
	public void testGetHelpTest3() {
		String[] args = {"-help"};
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, null), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");
	}

	// test if the invalid cases are being handled
	@Test
	public void testValidationTest() {
		String[] args2 = { "-x", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "Wrong command");

	}

	/**
	 * @Corrected Reasons on top of file
	 */
	@Test
	public void testValidationTestNoStdinOrDirectory() {

		String[] args3 = { "-s", "-" };
		pastetool3 = new PasteTool(args3);
		// assertEquals(pastetool3.execute(null, null),
		// "Sorry, piping not implemented yet" +
		// " hence we do not accept standard input.");
		assertEquals(pastetool3.execute(null, null), "Error: No such file or directory\n");
	}

	/**
	 * piping is implemented now
	 */
	@Test
	public void testValidationTestNotStdin() {
		String[] args4 = { "-d", "/", "-" };
		pastetool4 = new PasteTool(args4);
		// assertEquals(pastetool4.execute(null, null),
		// "Sorry, piping not implemented yet" +
		// " hence we do not accept standard input.");
		assertEquals(pastetool4.execute(null, null), "Error: No such file or directory\n");
	}
	
	@Test
	public void testValidationTestNoArgsOrStdin() {
		assertEquals(pastetool.execute(f, null), "No arguments and no standard input.");

	}
	
	@Test
	public void testStdinValidationTestWithOptionS() {
		String[] args = { "-s", "-" };
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, "1234\n1234"), "1234\t1234");
	}
	
	@Test
	public void testStdinValidationTestWithDelimiter() {
		String[] args = { "-d", "$", "-" };
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, "1234\n1234"), "1234\n1234");
	}

	@Test
	public void testStdinValidationTestMulitpleFile() {
		String[] args = { "-d", "@",  "testCase_1.txt", "-" };
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, "1234\n1234"), "1. IBM@1234\n" + "2. Symantec@1234\n" + "3. Palantir\n" + "10. hp\n" + "11. ihis");
	}
	
	@Test
	public void testStdinValidationTestNoDash() {
		String[] args = { "-s"};
		pastetool = new PasteTool(args);
		assertEquals(pastetool.execute(f, "1234\n1234"), "1234\t1234");
	}
	
	@Test
	public void testStdinValidationTest() {
		pastetool = new PasteTool(null);
		assertEquals(pastetool.execute(f, "1234\n1234"), "1234\n1234");
	}
	
	
}
