package sg.nus.edu.comp.cs4218.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.GrepTool;

public class TestGrepTool {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_01() {
		String input = "grep -A 2 \"temp\" file1.txt";
		
		GrepTool gt = new GrepTool();
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.Parse(input);
		assertEquals(parsed.size(), 1);
		
		String pattern = gt.GetPatternFromInput(input);
		assertEquals(pattern, "temp");
		
		Vector<String> fileList = gt.GetFileListFromInput(input);
		assertEquals(fileList.size(), 1);
		
		String result = gt.execute(new File("C:\\"), input);
		String expected = "#Alignment:#  Alignment of cells is attempted to be preserved.\n"+
				"\n"+
				"BORDER\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void test_02()
	{
		String input = "grep -A 2 -B 3 \"temp\" file1.txt file2.txt file3.txt";
		
		GrepTool gt = new GrepTool();
		gt.getCountOfMatchingLines("temp", input);
		Map<String, ArrayList<String>> parsed = gt.Parse(input);
		assertEquals(parsed.size(), 2);
		
		String pattern = gt.GetPatternFromInput(input);
		assertEquals(pattern, "temp");
		
		Vector<String> fileList = gt.GetFileListFromInput(input);
		assertEquals(fileList.size(), 3);
		
		String result = gt.execute(new File("C:\\"), input);
		//System.out.println(result);
		String expected = "              could have inter-word spacing that lined up by accident.\n"+ 
				"#Cell Size:#  If you have more than one line (as just above) then\n"+
	             "              you will simply get empty cells where the other column is empty.\n"+
	              	"#Alignment:#  Alignment of cells is attempted to be preserved.\n"+
	              	"\n"+
					"BORDER\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void test_03()
	{
		String input = "grep \"\\^A\" file2.txt";
		
		GrepTool gt = new GrepTool();
		Map<String, ArrayList<String> > parsed = gt.Parse(input);
		assertEquals(0, parsed.size());
		
		String result = gt.execute(new File("C:\\"), input);
		String expected = "^A^A^A\n"+
						"B^A\n"+
						"BBB^A\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void test_04()
	{
		String input = "grep -c \"^A\" file2.txt";
		GrepTool gt = new GrepTool();
		
		String result = gt.execute(new File("C:\\"), input);
		String expected = "3";
		assertEquals(expected, result);
	}
}
