package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;

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

	// test the functionality of pasting in serial for the single method and the
	// execute which calls the single method
	@Test
	public void pasteSerialTest() {
		assertEquals(pastetool.pasteSerial(fileArray1), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t");
		assertEquals(pastetool.pasteSerial(fileArray2), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" + "gek1517\tACC1002X\tsw2104\tpc1141\t");
		assertEquals(pastetool.pasteSerial(fileArray3), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
				+ "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\t");
		assertEquals(pastetool.pasteSerial(fileArray4), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
				+ "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\tapple\tban\tbanana\tcarrot\tcarro\tc\t");

		String[] args2 = { "-s", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t");
		pastetool2 = null;

		String[] args3 = { "-s", "testCase_1.txt", "testCase_2.txt" };
		pastetool3 = new PasteTool(args3);
		assertEquals(pastetool3.execute(null, null), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t" + "gek1517\tACC1002X\tsw2104\tpc1141\t");
		pastetool3 = null;

		assertEquals(pastetool.execute(f, "-s testCase_1.txt testCase_2.txt testCase_3.txt"), "1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
				+ "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\t");

		assertEquals(pastetool.execute(f, "-s testCase_1.txt testCase_2.txt testCase_3.txt testCase_4.txt"),
				"1. IBM\t2. Symantec\t3. Palantir\t10. hp\t11. ihis\t"
						+ "gek1517\tACC1002X\tsw2104\tpc1141\ta\tb\tb\tc\tc\tc\tapple\tban\tbanana\tcarrot\tcarro\tc\t");
	}

	// test the functionality of pasting with specified delimiter for the single
	// method and
	// the execute which calls the single method
	@Test
	public void pasteUseDelimeterTest() {
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray1), "1. IBM $ 2. Symantec $ " + "3. Palantir $ 10. hp $ 11. ihis $ ");
		assertEquals(pastetool.pasteUseDelimiter(" @ ", fileArray2), "1. IBM @ 2. Symantec @ "
				+ "3. Palantir @ 10. hp @ 11. ihis @ gek1517 @ ACC1002X @ sw2104 @ pc1141 @ ");
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray3), "1. IBM $ 2. Symantec $ "
				+ "3. Palantir $ 10. hp $ 11. ihis $ gek1517 $ ACC1002X $ sw2104 $ pc1141 $" + " a $ b $ b $ c $ c $ c $ ");
		assertEquals(pastetool.pasteUseDelimiter(" $ ", fileArray4), "1. IBM $ 2. Symantec $"
				+ " 3. Palantir $ 10. hp $ 11. ihis $ gek1517 $ ACC1002X $ sw2104 $ pc1141 $"
				+ " a $ b $ b $ c $ c $ c $ apple $ ban $ banana $ carrot $ carro $ c $ ");

		String[] args2 = { "-d", " $ ", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "1. IBM $ 2. Symantec $ " + "3. Palantir $ 10. hp $ 11. ihis $ ");
		pastetool2 = null;

		String[] args3 = { "-d", " @ ", "testCase_1.txt", "testCase_2.txt" };
		pastetool3 = new PasteTool(args3);
		assertEquals(pastetool3.execute(null, null), "1. IBM @ 2. Symantec @ " + "3. Palantir @ 10. hp @ 11. ihis @ gek1517 @ ACC1002X @ sw2104 @ pc1141 @ ");
		pastetool3 = null;

		assertEquals(pastetool.execute(f, "-d $$$ testCase_1.txt testCase_2.txt testCase_3.txt"), "1. IBM$$$2. Symantec$$$"
				+ "3. Palantir$$$10. hp$$$11. ihis$$$gek1517$$$ACC1002X$$$sw2104$$$pc1141$$$" + "a$$$b$$$b$$$c$$$c$$$c$$$");

		assertEquals(pastetool.execute(f, "-d $$$ testCase_1.txt testCase_2.txt testCase_3.txt testCase_4.txt"), "1. IBM$$$2. Symantec$$$"
				+ "3. Palantir$$$10. hp$$$11. ihis$$$gek1517$$$ACC1002X$$$sw2104$$$pc1141$$$"
				+ "a$$$b$$$b$$$c$$$c$$$c$$$apple$$$ban$$$banana$$$carrot$$$carro$$$c$$$");

	}

	// test the functionality of printing help message
	@Test
	public void getHelpTest() {
		assertEquals(pastetool.getHelp(), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");

		String[] args2 = { "-help" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");

		assertEquals(pastetool.execute(f, "-help"), "-s : paste one file at a time instead of in parallel\t"
				+ " -d DELIM: Use characters from the DELIM instead of TAB character\t" + " -help : Brief information about supported options");
	}

	// test if the invalid cases are being handled
	@Test
	public void validationTest() {
		String[] args2 = { "-x", "testCase_1.txt" };
		pastetool2 = new PasteTool(args2);
		assertEquals(pastetool2.execute(null, null), "Wrong command");
		pastetool3 = null;

		String[] args3 = { "-s", "-" };
		pastetool3 = new PasteTool(args3);
		assertEquals(pastetool3.execute(null, null), "Sorry, piping not implemented yet" + " hence we do not accept standard input.");

		String[] args4 = { "-d", "/", "-" };
		pastetool4 = new PasteTool(args4);
		assertEquals(pastetool4.execute(null, null), "Sorry, piping not implemented yet" + " hence we do not accept standard input.");

		assertEquals(pastetool.execute(f, null), "No arguments and no standard input.");

	}

}
