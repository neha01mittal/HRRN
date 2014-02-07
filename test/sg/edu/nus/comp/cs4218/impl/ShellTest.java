package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

public class ShellTest {

	private Shell shell;

	@Before
	public void before() {
		shell = new Shell();
	}

	@After
	public void after() {
		shell = null;
	}

	@Test
	public void testParseEmptyCommand() {
		String cmd = "";
		ITool result = shell.parse(cmd);

		assertNull(result);
	}

	@Test
	public void testParseInvalidCommand() {
		String cmd = "not valid";
		ITool result = shell.parse(cmd);

		assertNull(result);
	}

	@Test
	public void testParseCommandCat() {
		String cmd = "cat path/to/file1";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("CatTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandCd() {
		String cmd = "cd path/new";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("CdTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandCopy() {
		String cmd = "copy file1 file2";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("CopyTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandDelete() {
		String cmd = "delete file1";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("DeleteTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandEcho() {
		String cmd = "echo some word";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("EchoTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandLs() {
		String cmd = "ls";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("LsTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandMove() {
		String cmd = "move path1 path2";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("MoveTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandPWD() {
		String cmd = "pwd";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("PWDTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandMoveGrep() {
		String cmd = "grep test testfile.txt";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("GrepTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandMovePiping() {
		String cmd = "cmd-1 | cmd-2";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("PipingTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWithCapital() {
		String cmd = "eChO sOme wOrd";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("EchoTool", result.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWithInconsistentSpace() {
		String cmd = "  eChO   sOme   wOrd   ";
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("EchoTool", result.getClass().getSimpleName());
	}
}
