package sg.edu.nus.comp.cs4218.impl.branchcoverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;

public class BranchImprovementTest {

	private CommTool commTool;
	private static final String INVALID_COMMAND = "Invalid command";
	private static final String NOT_SORTED = "Not Sorted!";

	@Before
	public void before() {
		commTool = new CommTool(null);
	}

	@After
	public void after() {
		commTool = null;
	}
	
	// BRANCH COVERAGE 
	@Test
	public void testcompareFilesCheckSortStatusWithNullInputs() {
		String result = commTool.compareFilesCheckSortStatus(null, null);
		assertEquals("-1",result);
		
	}
	
	@Test
	public void testcompareFilesCheckSortStatusWithNullInput1AndSmalInput2() {
		commTool.currentLine2= "xyz";
		String result = commTool.compareFilesCheckSortStatus(null, "abc");
		assertEquals(NOT_SORTED,result);
	}

	@Test
	public void testcompareFilesCheckSortStatusWithNullInput2AndSmalInput1() {
		commTool.currentLine1= "xyz";
		String result = commTool.compareFilesCheckSortStatus("abc", null);
		assertEquals(NOT_SORTED,result);
	}

	@Test
	public void testExecuteWithNullArgs() {
		CommTool ct = new CommTool(null);
		String workingDir = System.getProperty("user.dir");
		File f = new File(workingDir);
		assertEquals(INVALID_COMMAND,ct.execute(f, null));
	}
	
	@Test
	public void testdecodeParsedOperationForF2() {
		String[] args = { "commTestCase1a.txt","commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "f2");
		assertEquals(true,commTool.flag2);
		assertEquals(stdin, commTool.file2);
		assertEquals(args[0],commTool.file1);
	}
	
	@Test
	public void testdecodeParsedOperationFor3Args() {
		String[] args = { "-c","commTestCase1a.txt","commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "c:f2");
		assertEquals(true,commTool.flag2);
		assertEquals(stdin, commTool.file2);
		assertEquals(args[1],commTool.file1);
		assertEquals(true, commTool.sortFlag);
	}
	
	@Test
	public void testdecodeParsedOperationFor3ArgsWithF1c() {
		String[] args = { "-c","commTestCase1a.txt","commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "c:f1");
		assertEquals(true,commTool.flag1);
		assertEquals(stdin, commTool.file1);
		assertEquals(args[2],commTool.file2);
		assertEquals(true, commTool.sortFlag);
	}
	
	@Test
	public void testdecodeParsedOperationFor3ArgsWithF2d() {
		String[] args = { "-d","commTestCase1a.txt","commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "d:f2");
		assertEquals(true,commTool.flag2);
		assertEquals(args[1], commTool.file1);
		assertEquals(stdin,commTool.file2);
		assertEquals(false, commTool.sortFlag);
	}
	
	@Test
	public void testdecodeParsedOperationFor3ArgsWithF1d() {
		String[] args = { "-d","commTestCase1a.txt","commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		commTool.decodeParsedOperation(stdin, "d:f1");
		assertEquals(true,commTool.flag1);
		assertEquals(stdin, commTool.file1);
		assertEquals(args[2],commTool.file2);
		assertEquals(false, commTool.sortFlag);
	}
	
	@Test
	public void testBuildParseCodeForCountGreaterThanOne() {
		String[] args = { "-d","commTestCase1a.txt","commTestCase2a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		String result = commTool.buildParseCode("this is parsecode", 3);
		assertEquals(INVALID_COMMAND,result);
	}
	

	@Test
	public void testBuildParseCodeForF2() {
		String[] args = { "-d", "-","commTestCase1a.txt" };
		commTool = new CommTool(args);
		String stdin = "This is text";
		String result = commTool.buildParseCode("this is parsecode", 0);
		assertEquals("f2",result);
	}
}
