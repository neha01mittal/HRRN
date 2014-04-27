package sg.edu.nus.comp.cs4218.impl.hackathon;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;

public class UniqToolBugFixTest {

	private UniqTool uniqTool;

	@Before
	public void before() {
		uniqTool = new UniqTool(null);
	}

	@After
	public void after() {
		uniqTool = null;
	}

	/**
	 * 
	 * @BUG_ID: 12
	 * 
	 * @fix location: UniqTool.java
	 * 
	 *      class UniqTool line 255
	 */
	@Test
	public void testUniqToolForBugInHackthon1() {

		String stdin = readFile("uniqTestCase3.txt");
		uniqTool = new UniqTool(new String[] { "-i", "-" });
		String workingDir = System.getProperty("user.dir");
		String output = "a\nb\nc\na\nb\nc";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, stdin), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	/**
	 * 
	 * @BUG_ID: 12
	 * 
	 * @fix location: UniqTool.java
	 * 
	 *      class UniqTool line 290
	 */
	@Test
	public void testUniqToolforBugInHackthon2() {

		String stdin = readFile("uniqTestCase3.txt");
		uniqTool = new UniqTool(new String[] { "-i", "-f", "1", "-" });
		String workingDir = System.getProperty("user.dir");
		String output = "a\na\nb\nc\na\nb\nc";
		File f = new File(workingDir);
		assertEquals(uniqTool.execute(f, stdin), output);
		assertEquals(uniqTool.getStatusCode(), 0);
	}

	private String readFile(String path) {
		File newFile = new File(path);
		String fullText = "";
		try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				fullText += sCurrentLine + "\n";
			}
		} catch (IOException e) {
			return null;
		}
		return fullText;
	}

}
