package sg.edu.nus.comp.cs4218.impl.hackathon;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;

public class WcToolBugFixTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/* Bug ID: 42
	 * Fix locations: WcTool.java, line 62
	 */
	@Test
	public void wordCountTestEmpty() {
		String[] args = {"-w", "emptyFile.txt"};
		WcTool wt = new WcTool(args);
		String result = wt.getWordCount("");
		String expectedOutput = "0";
		assertEquals(result, expectedOutput);
	}
}
