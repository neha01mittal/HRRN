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

public class BlackBoxTest {

	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private final ByteArrayOutputStream	errContent	= new ByteArrayOutputStream();
	private static String				originalDirString;
	private static String				testDirString;
	private static List<File>			testDirFileList;
	private static Shell				shell;
	private static InputStream			originalStdin;
	private static PrintStream			originalStdout;
	private static PrintStream			originalStderr;

	@BeforeClass
	public static void beforeClass() {
		// cache system variables.
		originalDirString = System.getProperty("user.dir");
		originalStdin = System.in;
		originalStdout = System.out;
		originalStderr = System.err;

		testDirString = originalDirString + File.separator + "data" + File.separator + "integrationTest2";

		// create files if not exist
		for (int i = 1; i < 7; i++) {
			File file = new File(testDirString + File.separator + "testFolder" + File.separator + "tempFile_0" + i + ".txt");
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
		testDirFileList = Arrays.asList(new File(testDirString).listFiles(fileNameFilter));
	}

	@AfterClass
	public static void afterClass() {
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
		System.setProperty("user.dir", originalDirString);

		// remove shell and set back system
		shell = null;
	}

	@Test
	public void testSimple() {
		String input = "ls\r\n ";
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
		String result = outContent.toString().replace(header,
				"").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChains1() {
		String input = "ls | grep test | grep 01 | cat - \r\n ";
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
		String expected = "testFile_01.txt\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChains2() {
		String input = "echo testFile_01.txt | cat | cat - testFile_01.txt \r\n ";
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
		String expected = "testFile_01.txt\nsausage\nblubber\npencil\ncloud\n\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChains3() {
		String input = "echo testFile_01.txt | cat testFile_02.txt - | cut -c 1-4 - \r\n ";
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
		String expected = "clou\nmoon\ncomp\nscho\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChains4() {
		String input = "sort testFile_03.txt testFile_02.txt | uniq | grep -A 2 -B 2 moon \r\n ";
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
		String expected = "computer\nhammer\nmoon\nnetwork\npencil\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}

	@Test
	public void testChains5() {
		String input = "cat testFile_05.txt testFile_02.txt | grep ^c | grep -c -v a \r\n ";
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
		String expected = "2\n";

		// remove header and windows dependency
		String result = outContent.toString().replace(header, "").replaceAll("\r", "");
		assertEquals(expected, result);
	}
	//
	// @Test
	// public void testChains6() {
	// String input = " \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChains7() {
	// String input =
	// "comm test_file_01.txt test_file_02.txt | grep -A 2 -B 3 a | paste f2.txt \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChains8() {
	// String input =
	// "paste -s test_file_01.txt | cat | cat - test_file_02.txt \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChains9() {
	// String input = "cut - filename | grep | cat - \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChains10() {
	// String input =
	// "echo test_file_01.txt | cat test_file_02.txt - | cut -c 1-4 | grep [a\\|b] | grep sa \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "saus";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChainsNegtive1() {
	// String input = " \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChainsNegtive2() {
	// String input = " \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChainsNegtive3() {
	// String input = " \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testChainsNegtive4() {
	// String input = " \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }

	// @Test
	// public void testStateChange1() {
	// String input = "pwd \r\n cd testFolder" + File.separator +
	// "testFolder_01 \r\n pwd \r\n ls \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	// String newHeader = System.getProperty("user.dir") + File.separator +
	// "testFolder" + File.separator + "testFolder_01" + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = header.replace(" $: ", "") + "\n" +
	// newHeader.replace(" $: ", "") + "\n" + "words_random.txt" + "\n";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replace(newHeader, "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testStateChange2() {
	// String input = "ls | grep tempFile_01 \r\n move testFolder" +
	// File.separator
	// +
	// "tempFile_01.txt . \r\n ls | grep tempFile_01 \r\n move tempFile_01.txt testFolder"
	// + File.separator + " \r\n ls | grep tempFile_01 \r\n";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // generate expected string
	// String expected = "\ntempFile_01.txt\n\n";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testStateChange3() {
	// String input = "ls | grep tempFile_01 \r\n copy testFolder" +
	// File.separator
	// +
	// "tempFile_01.txt . \r\n ls | grep tempFile_01 \r\n copy tempFile_01.txt testFolder"
	// + File.separator + " \r\n ls testFolder | grep tempFile_01 \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// TestUtils.delete(new File(System.getProperty("user.dir") + File.separator
	// + "tempFile_01.txt"));
	//
	// // generate expected string
	// String expected = "\ntempFile_01.txt\ntempFile_01.txt\n";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testStateChange4() {
	// String input = "copy testFile_01.txt testFolder" + File.separator +
	// " \r\n "
	// + File.separator + " \r\n ls testFolder | grep testFile_01 \r\n "
	// + "cat testFolder" + File.separator + "testFile_01.txt \r\n";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// TestUtils.delete(new File(System.getProperty("user.dir") + File.separator
	// + "testFolder" + File.separator + "testFile_01.txt"));
	//
	// // generate expected string
	// String expected = "testFile_01.txt\nsausage\nblubber\npencil\ncloud\n\n";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testStateChange5() {
	// String input =
	// "echo lala \r\n copy testFile_01.txt testFile_01_copy.txt \r\n echo mummy \r\n ls | grep testFile_01 \r\n";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	// // generally wait for a while
	// try {
	// Thread.sleep(500);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// TestUtils.delete(new File(System.getProperty("user.dir") + File.separator
	// + "testFile_01_copy.txt"));
	//
	// // generate expected string
	// String expected = "lala\nmummy\ntestFile_01.txt\ntestFile_01_copy.txt\n";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testStateChangeNegtive1() {
	// String input = " \r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
	//
	// @Test
	// public void testStateChangeNegtive2() {
	// String input = "\r\n ";
	// System.setIn(new ByteArrayInputStream(input.getBytes()));
	// String header = System.getProperty("user.dir") + " $: ";
	//
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// shell.start();
	// }
	// });
	// t.run();
	//
	// // generate expected string
	// String expected = "";
	//
	// // remove header and windows dependency
	// String result = outContent.toString().replace(header,
	// "").replaceAll("\r", "");
	// assertEquals(expected, result);
	// }
}