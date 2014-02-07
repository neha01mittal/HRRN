package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;

/**
 * Show the current directory
 * 
 * @usage pwd
 * @note pwd tool does not take in any arguments or stdin, thus it could not be
 *       piped to. It will set the initial path to the directory where the
 *       program start.
 * @success return a string showing the absolute path of current working
 *          directory.
 * @exceptions Cannot find working directory
 */
public class PWDToolTest {

	private IPwdTool pwdtool;

	@Before
	public void before() {
		pwdtool = new PWDTool();
	}

	@After
	public void after() {
		pwdtool = null;
	}

	@Test
	public void getStringForDirectoryTest() throws IOException {
		// Test expected behavior
		// Create a tmp-file and get (existing) parent directory
		String existsDirString = File.createTempFile("exists", "tmp").getParent();
		File existsDir = new File(existsDirString);

		String dirString = pwdtool.getStringForDirectory(existsDir);

		assertEquals(0, pwdtool.getStatusCode());
		assertTrue(dirString.equals(existsDirString));
	}

	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException {
		// Test error-handling 1
		// Reference non-existing file
		File notExistsDir = new File("notexists");
		pwdtool.getStringForDirectory(notExistsDir);

		assertNotEquals(0, pwdtool.getStatusCode());
	}

	@Test
	public void getStringForNullDirectoryTest() throws IOException {
		// Test error-handling 2
		pwdtool.getStringForDirectory(null);

		assertNotEquals(0, pwdtool.getStatusCode());
	}

}
