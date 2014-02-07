package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

<<<<<<< HEAD
=======
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

<<<<<<< HEAD
=======
/**
 * @author Zhang Haoqiang
 */
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
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
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNull(result);
=======
		ITool resultTool = shell.parse(cmd);

		assertNull(resultTool);
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseInvalidCommand() {
		String cmd = "not valid";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNull(result);
=======
		ITool resultTool = shell.parse(cmd);

		assertNull(resultTool);
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandCat() {
		String cmd = "cat path/to/file1";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("CatTool", result.getClass().getSimpleName());
=======
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("CatTool", resultTool.getClass().getSimpleName());
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandCd() {
		String cmd = "cd path/new";
<<<<<<< HEAD
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
=======
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("CdTool", resultTool.getClass().getSimpleName());
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
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandPWD() {
		String cmd = "pwd";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("PWDTool", result.getClass().getSimpleName());
=======
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("PWDTool", resultTool.getClass().getSimpleName());
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandMoveGrep() {
		String cmd = "grep test testfile.txt";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("GrepTool", result.getClass().getSimpleName());
=======
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("GrepTool", resultTool.getClass().getSimpleName());
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandMovePiping() {
		String cmd = "cmd-1 | cmd-2";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("PipingTool", result.getClass().getSimpleName());
=======
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("PipingTool", resultTool.getClass().getSimpleName());
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandWithCapital() {
		String cmd = "eChO sOme wOrd";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("EchoTool", result.getClass().getSimpleName());
=======
		ITool resultTool = shell.parse(cmd);

		assertNotNull(resultTool);
		assertEquals("EchoTool", resultTool.getClass().getSimpleName());
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}

	@Test
	public void testParseCommandWithInconsistentSpace() {
		String cmd = "  eChO   sOme   wOrd   ";
<<<<<<< HEAD
		ITool result = shell.parse(cmd);

		assertNotNull(result);
		assertEquals("EchoTool", result.getClass().getSimpleName());
=======
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
		String cmd = "echo \"normal\" as\"as'\\ r'w e\"d's d's dd\\ sa";
		String[] expected = { "normal", "asas'\\ r'w eds ds", "dd sa" };
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
	public void testExecutionReturned() {
		String cmd = "pwd";
		Runnable resultTool = shell.execute(shell.parse(cmd));

		assertNotNull(resultTool);
>>>>>>> 618882f06038e0c227af0a6163712d8259dfbfca
	}
}
