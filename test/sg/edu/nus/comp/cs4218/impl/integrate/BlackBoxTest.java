package sg.edu.nus.comp.cs4218.impl.integrate;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.utils.TestUtils;

public class BlackBoxTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private static String originalDirString;
	private static String testDirString;
	private static List<File> testDirFileList;
	private static Shell shell;
	private static InputStream originalStdin;
	private static PrintStream originalStdout;
	private static PrintStream originalStderr;

	@BeforeClass
	public static void beforeClass() {
		// cache system variables.
		originalDirString = System.getProperty("user.dir");
		originalStdin = System.in;
		originalStdout = System.out;
		originalStderr = System.err;
		TestUtils t = new TestUtils();

		testDirString = originalDirString + File.separator + "data"
				+ File.separator + "integrationTest2";

		// create files if not exist
		for (int i = 1; i < 7; i++) {
			File file = new File(testDirString + File.separator + "testFolder"
					+ File.separator + "tempFile_0" + i + ".txt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// record the files inside
		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// just ignore the hidden files
				if (name.lastIndexOf('.') != 0) {
					return true;
				}
				return false;
			}
		};
		testDirFileList = Arrays.asList(new File(testDirString)
				.listFiles(fileNameFilter));
	}

	@AfterClass
	public static void afterClass() {
		System.setProperty("user.dir", originalDirString);
	}

	@Before
	public void before() {
		System.setProperty("user.dir", testDirString);
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		shell = new Shell();
	}

	@After
	public void after() {
		shell.executionFlag = false;

		// release std and clean the buffer
		System.setIn(originalStdin);
		System.setOut(originalStdout);
		System.setErr(originalStderr);

		// remove shell and set back system
		shell = null;
	}

	@Test
	public void testSimple() {
		String input = "ls" + System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "";
		for (int i = 0; i < testDirFileList.size(); i++) {
			expected += testDirFileList.get(i).getName() + "\n";
		}

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands1() {
		String input = "ls | grep test | grep 01 | cat - "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "testFile_01.txt\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands2() {
		String input = "echo testFile_01.txt | cat | cat - testFile_01.txt "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "testFile_01.txtsausage\nblubber\npencil\ncloud\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsofCommands3() {
		String input = "echo testFile_01.txt | cat testFile_02.txt - | cut -c 1-4 - | grep o "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "clou\nmoon\ncomp\nscho\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands4() {
		String input = "sort testFile_03.txt testFile_02.txt | uniq - | grep -A 2 -B 2 moon "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "computer\nhammer\nmoon\nnetwork\npencil\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands5() {
		String input = "cat testFile_05.txt testFile_02.txt | grep ^c | grep -c -v a "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "2\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands6() {
		String input = "cat testFile_01.txt testFile_03.txt | sort - | uniq - | wc -l "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "\t5\n";
		expected = "\t4\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands7() {
		String input = "comm testFile_01.txt testFile_03.txt | grep sa | paste testFile_01.txt - "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "sausage\t\t\t\t\tsausage\nblubber\t\t\tsausage\npencil\ncloud\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands8() {
		String input = "paste -s testFile_01.txt | cat | cat - testFile_02.txt | grep b "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "sausage\tblubber\tpencil\tcloudsausage\nblubber\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands9() {
		String input = "cut -c 2-4 testFile_01.txt | sort - | paste -s - | cat - "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "aus\tenc\tlou\tlub\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsOfCommands10() {
		String input = "echo testFile_01.txt | cat testFile_02.txt - | cut -c 1-4 - | grep [a\\|b] | grep sa "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "saus\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsNegtive1() {
		String input = "echo testFile_01.txt | cat | cat - testFile_01.txt | echo - "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "-\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsNegtive2() {
		String input = "cd .. | cd .. | cd .. | pwd "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = System.getProperty("user.dir") + "\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsNegtive3() {
		String input = "cat invalid | cat - testFile_01.txt | grep invalid "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChainsNegtive4() {
		String input = "cat invalid | cat - testFile_01.txt | grep sa "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "sausage\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChange1() {
		String input = "pwd " + System.getProperty("line.separator")
				+ " cd testFolder" + File.separator + "testFolder_01 "
				+ System.getProperty("line.separator") + " pwd "
				+ System.getProperty("line.separator") + " ls "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";
		String newHeader = System.getProperty("user.dir") + File.separator
				+ "testFolder" + File.separator + "testFolder_01" + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = header.replace(" $: ", "") + "\n"
				+ newHeader.replace(" $: ", "") + "\n" + "words_random.txt"
				+ "\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replace(newHeader, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChange2() {
		String input = " ls | grep tempFile_01 "
				+ System.getProperty("line.separator") + " move testFolder"
				+ File.separator + "tempFile_01.txt "
				+ System.getProperty("user.dir") + File.separator + " "
				+ System.getProperty("line.separator")
				+ " ls | grep tempFile_01 "
				+ System.getProperty("line.separator")
				+ " delete tempFile_01.txt " + " "
				+ System.getProperty("line.separator")
				+ " ls | grep tempFile_01 "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "\ntempFile_01.txt\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChange3() {
		String input = " ls | grep tempFile_03 "
				+ System.getProperty("line.separator") + " copy testFolder"
				+ File.separator + "tempFile_03.txt "
				+ System.getProperty("user.dir") + " "
				+ System.getProperty("line.separator")
				+ " ls | grep tempFile_03 "
				+ System.getProperty("line.separator") + " "
				+ "delete tempFile_03.txt "
				+ System.getProperty("line.separator")
				+ " ls | grep tempFile_03 "
				+ System.getProperty("line.separator") + " ";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "\ntempFile_03.txt\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChange4() {
		String input = "copy testFile_01.txt testFolder "
				+ System.getProperty("line.separator") + " "
				+ " ls testFolder | grep testFile_01 "
				+ System.getProperty("line.separator") + " " + "cat testFolder"
				+ File.separator + "testFile_01.txt "
				+ System.getProperty("line.separator") + "";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "testFile_01.txt\nsausage\nblubber\npencil\ncloud\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChange5() {
		String input = "echo lala " + System.getProperty("line.separator")
				+ " copy testFile_01.txt testFile_01_copy.txt "
				+ System.getProperty("line.separator") + "" + " echo mummy "
				+ System.getProperty("line.separator")
				+ " ls | grep testFile_01 "
				+ System.getProperty("line.separator") + " "
				+ " delete testFile_01_copy.txt "
				+ System.getProperty("line.separator")
				+ " ls | grep testFile_01 "
				+ System.getProperty("line.separator") + "";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "lala\nmummy\ntestFile_01.txt\ntestFile_01_copy.txt\ntestFile_01.txt\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChangeNegtive1() {
		String input = "delete invalid.txt "
				+ System.getProperty("line.separator") + " ls "
				+ System.getProperty("line.separator") + "";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "";
		for (int i = 0; i < testDirFileList.size(); i++) {
			expected += testDirFileList.get(i).getName() + "\n";
		}

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testStateChangeNegtive2() {
		String input = "copy invalid.txt invalid2.txt "
				+ System.getProperty("line.separator") + " ls "
				+ System.getProperty("line.separator") + "";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		String header = System.getProperty("user.dir") + " $: ";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				shell.start();
			}
		});
		t.run();
		// generally wait for a while
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// generate expected string
		String expected = "";
		for (int i = 0; i < testDirFileList.size(); i++) {
			expected += testDirFileList.get(i).getName() + "\n";
		}

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "")
				.replaceAll("\r", "");
		assertEquals(expected, result);
	}
}