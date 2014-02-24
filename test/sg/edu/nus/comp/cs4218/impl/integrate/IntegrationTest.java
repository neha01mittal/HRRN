package sg.edu.nus.comp.cs4218.impl.integrate;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class IntegrationTest {
	private static Path rootDirectory;
	private static String rootDirectoryString;
	private static List<File> testFileList;
	private static Shell shell;

	@BeforeClass
	public static void before() throws IOException {

		// create new dir and files inside
		rootDirectoryString = System.getProperty("user.dir")
				+ "/integrationTest";

		testFileList = Arrays.asList(new File(rootDirectoryString).listFiles());

		for (int i = 0; i < 10; i++) {
			try {
				String filePath = rootDirectoryString + "/testFile-" + i
						+ ".txt";
				File file = new File(filePath);

				// if file doesn't exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("This is temp test text-" + i + ".");
				bw.close();

				testFileList.add(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		shell = new Shell();
	}

	@AfterClass
	public static void after() throws IOException {  
		shell = null;
	}

	@Test
	public void test() { 
		// System.out.print("hello");
		// assertEquals("hello", outContent.toString());
	}
}