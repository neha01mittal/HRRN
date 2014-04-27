package sg.edu.nus.comp.cs4218.impl.hackathon;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;

public class CutToolBugFixTest {

	private CutTool	cutTool;

	@Before
	public void before() {
		cutTool = new CutTool(null);
	}

	@After
	public void after() {
		cutTool = null;
	}

	/**
	 * 
	 * @BUG_ID: 21
	 * 
	 * @fix location: Shell.java
	 * 
	 * class Shell line 324
	 */
	@Test
	public void executeUnsortedListF0Test() {
		String[] args = { "-d", " ", "-f", "3", "file4.txt" };
		cutTool = new CutTool(args);
		String workingDir = System.getProperty("user.dir");

		File f = new File(workingDir);
		assertEquals(cutTool.execute(f, null), "brown");
		assertEquals(cutTool.getStatusCode(), 0);

	}

}
