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

	@Test
	public void wordCountTestEmpty() {
		String[] args = {"-l", "emptyFile.txt"};
		WcTool wt = new WcTool(args);
		String result = wt.getWordCount("");
		String expectedOutput = "0";
		assertEquals(result, expectedOutput);
	}
	
	@Test
	public void wordCountNullInput() {
		String[] args = {"-l", "emptyFile.txt"};
		WcTool wt = new WcTool(args);
		String result = wt.getWordCount(null);
		String expectedOutput = "0";
		assertEquals(result, expectedOutput);
	}
	
	@Test
	public void wordCountCharacterCountNullInput() {
		String[] args = {"-m", "emptyFile.txt"};
		WcTool wt = new WcTool(args);
		String result = wt.getCharacterCount(null);
		String expectedOutput = "0";
		assertEquals(result, expectedOutput);
	}
}
