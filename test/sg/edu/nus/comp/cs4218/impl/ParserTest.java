package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

/**
 * The Shell is used to interpret and execute user's commands. Following
 * sequence explains how a basic shell can be implemented in Java
 * 
 * DIFFERENT OPTIONS commands $ invalid command - prints error message $ NOTE:
 * 
 * @usage
 * @options $: ls - lists current directory $: copy path1 path2 - copies
 *          contents of file/folder from path1 to path2 $: move path1 path2 -
 *          moves file/folder from path1 to path2 $: delete path1 - deletes
 *          file/folder $: pwd - displays present working directory $: echo
 *          input - displays input $: cat file - prints contents of
 *          file/standard input $: grep options “pattern” file - searches for a
 *          pattern in file(s) $: p1 | p2 - send stdout of p1 to p2 to execute
 * @note The input arguments or path can be relative or absolute, in quotes or
 *       without quotes, with backslash or front slash, with escaped space. But
 *       other escaped like \” or \’ characters are not yet handled. But you can
 *       still escape “ by surrounding it with ‘, vise versa. The termination
 *       function is either ctrl-z to terminate a running tool, the char has to
 *       be input with enter key hit. The shell will try it’s best to stop the
 *       running thread, if it’s really not able to stop the thread, the program
 *       will stop for safety. User are not allowed to use ‘~’ to denote home
 *       directory.
 * @success It will print the returning string, or nothing if returned null, or
 *          a blank line if returned empty string.
 * @exceptions IOException for reading user input Tool returned status code not
 *             0, if so shell will print out the error code.
 * 
 */
public class ParserTest {

	private static Shell	shell;

	@BeforeClass
	public static void before() {
		shell = new Shell();
	}

	@AfterClass
	public static void after() {
		shell = null;
	}

	@Test
	public void testParseEmptyCommand() {
		String cmd = "";
		ITool resultTool = shell.parse(cmd);

		assertNull(resultTool);
	}

	@Test
	public void testParseInvalidCommand() {
		String cmd = "not valid";
		ITool resultTool = shell.parse(cmd);

		assertNull(resultTool);
	}

	@Test
	public void testParseCommandCat() {
		String cmd = "cat path/to/file1";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("CatTool", resultTool.getClass().getSimpleName());
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
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("CopyTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandDelete() {
		String cmd = "delete file1";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("DeleteTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandEcho() {
		String cmd = "echo some word";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("EchoTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandLs() {
		String cmd = "ls";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("LsTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandMove() {
		String cmd = "move path1 path2";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("MoveTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandPWD() {
		String cmd = "pwd";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("PWDTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandGrep() {
		String cmd = "grep test testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("GrepTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandPiping() {
		String cmd = "cmd-1 | cmd-2";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("PipingTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWithEscapedPipe1() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"one | \" two' | 'three four\\|";
		ITool resultTool = shell.parse(cmd);

		String[] expected = { "one |", "two | three", "four|" };
		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandWithEscapedPipe2() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "cat testCase_3.txt | grep [a|b]";
		ITool resultTool = shell.parse(cmd);

		String[] expected = { "cat testCase_3.txt", "grep [a", "b]" };
		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandComm() {
		String cmd = "comm test testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("CommTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandCut() {
		String cmd = "cut testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("CutTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandPaste() {
		String cmd = "paste testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("PasteTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandSort() {
		String cmd = "sort testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("SortTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandUniq() {
		String cmd = "uniq testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("UniqTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWc() {
		String cmd = "wc testfile.txt";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("WcTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWithCapital() {
		String cmd = "eChO sOme wOrd";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("EchoTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWithInconsistentSpace() {
		String cmd = "  eChO      sOme   wOrd   ";
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("EchoTool", resultTool.getClass().getSimpleName());
	}

	@Test
	public void testParseCommandWithQuotes() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "ls \"normal\" 'test's";
		String[] expected = { "normal", "tests" };
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);

		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandWithQuotesComplex() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'r'we\"d'sd's ddsa";
		String[] expected = { "normal", "asas'r'wedsds", "ddsa" };
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);

		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandWithQuotesWithSpaceInside() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'r'  we\"d'sd 's ddsa";
		String[] expected = { "normal", "asas'r'  wedsd s", "ddsa" };
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);

		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandWithEscapedSpace() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo  normal\\ text no\\ space";
		String[] expected = { "normal text", "no space" };
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);

		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandWithMixedQuotesandEscapedSpace() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'\\ r 'w e\"ds dd\\ sa";
		String[] expected = { "normal", "asas'\\ r 'w eds", "dd sa" };
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);

		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testParseCommandWithMixedQuotesandEscapedSpaceandDuplicateMatch() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'\\ r\\$'w\\ e\"d's d's dd\\ sa\\$";
		String[] expected = { "normal", "asas'\\ r$'w\\ eds ds", "dd sa$" };
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);

		Field fields[] = resultTool.getClass().getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("args")) {
				field.setAccessible(true);
				List<String> args = Arrays.asList((String[]) field.get(resultTool));
				for (int i = 0; i < expected.length; i++) {
					assertEquals(expected[i], args.get(i));
				}
			}
		}
	}

	@Test
	public void testGetCmdWithQuotes() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "ls \"normal\" 'test's";
		String[] expected = { "normal", "tests" };

		String[] results = Shell.getArgsArray(cmd);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], results[i]);
		}
	}

	@Test
	public void testGetCmdWithQuotesComplex() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'r'we\"d'sd's ddsa";
		String[] expected = { "normal", "asas'r'wedsds", "ddsa" };

		String[] results = Shell.getArgsArray(cmd);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], results[i]);
		}
	}

	@Test
	public void testGetCmdWithQuotesWithSpaceInside() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'r'  we\"d'sd 's ddsa";
		String[] expected = { "normal", "asas'r'  wedsd s", "ddsa" };

		String[] results = Shell.getArgsArray(cmd);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], results[i]);
		}
	}

	@Test
	public void testGetCmdWithEscapedSpace() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo  normal\\ text no\\ space";
		String[] expected = { "normal text", "no space" };

		String[] results = Shell.getArgsArray(cmd);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], results[i]);
		}
	}

	@Test
	public void testGetCmdWithMixedQuotesandEscapedSpace() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'\\ r 'w e\"ds dd\\ sa";
		String[] expected = { "normal", "asas'\\ r 'w eds", "dd sa" };

		String[] results = Shell.getArgsArray(cmd);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], results[i]);
		}
	}

	@Test
	public void testGetCmdWithMixedQuotesandEscapedSpaceandDuplicateMatch() throws IllegalArgumentException, IllegalAccessException {
		String cmd = "echo \"normal\" as\"as'\\ r\\$'w\\ e\"d's d's dd\\ sa\\$";
		String[] expected = { "normal", "asas'\\ r$'w\\ eds ds", "dd sa$" };

		String[] results = Shell.getArgsArray(cmd);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], results[i]);
		}
	}
}
