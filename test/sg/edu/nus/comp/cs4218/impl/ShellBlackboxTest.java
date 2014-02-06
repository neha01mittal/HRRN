package sg.edu.nus.comp.cs4218.impl;

//import java.io.BufferedWriter;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class ShellBlackboxTest {
//	private static Path rootDirectory;
//	private static String rootDirectoryString;
//	private static List<File> testFileList;
//	private static Shell shell;
//	private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//
//	@BeforeClass
//	public static void before() throws IOException {
//
//		// create new dir and files inside
//		rootDirectoryString = System.getProperty("user.dir") + "/blackboxTest";
//
//		rootDirectory = Paths.get(rootDirectoryString);
//		Files.createDirectory(rootDirectory);
//
//		testFileList = new ArrayList<File>();
//
//		for (int i = 0; i < 10; i++) {
//			try {
//				String filePath = rootDirectoryString + "/testFile-" + i + ".txt";
//				File file = new File(filePath);
//
//				// if file doesn't exists, then create it
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//
//				FileWriter fw = new FileWriter(file.getAbsoluteFile());
//				BufferedWriter bw = new BufferedWriter(fw);
//				bw.write("testtext-" + i);
//				bw.close();
//
//				testFileList.add(file);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		System.setOut(new PrintStream(outContent));
//
//		shell = new Shell();
//		shell.start();
//	}
//
//	@AfterClass
//	public static void after() throws IOException {
//		for (int i = 0; i < testFileList.size(); i++) {
//			testFileList.get(i).delete();
//		}
//		Files.deleteIfExists(rootDirectory);
//		System.setOut(null);
//	}
//
//	@Test
//	public void test() {
//		ByteArrayInputStream in = new ByteArrayInputStream("test\r\n".getBytes());
//		System.setIn(in);
//
//		System.err.println(outContent.toString());
//
//		// System.out.print("hello");
//		// assertEquals("hello", outContent.toString());
//	}
// }
