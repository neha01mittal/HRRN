package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;

public class ThreadTest {

	private Shell	shell;

	@Before
	public void before() throws IOException {
		shell = new Shell();
	}

	@After
	public void after() throws IOException {
		shell = null;
	}

	@Test
	public void testRunningPWD() {
		ITool itool = shell.parse("pwd");
		Future<?> future = shell.executeTest(itool);

		while (!(future.isDone() || future.isCancelled())) {
			// just wait.
		}
		shell.stopTest(future, itool);
		assertEquals(0, itool.getStatusCode());
	}

	/**
	 * Ctrl-Z testing
	 * 
	 * Step 1. Create a mock up iTool than can run for a long time
	 * 
	 * Step 2. Then use the decoupled method runItool to add the new tool to the
	 * thread pool and executing.
	 * 
	 * Step 3. Generally wait for a little while to let itool start run
	 * 
	 * Step 4. As soon as we detect it running, we can call stopItool to stop
	 * it.
	 * 
	 * Step 5. We assert the error code to make sure that the itool thread did
	 * not reach the final state where the status code is changes. We also
	 * assert the thread state to make sure it is really stopped.
	 */
	@Test
	public void testTerminatingSleepThread() {
		//
		//
		// This test focused on the waiting thread
		// which is generally easy to interrupt
		//
		//
		ITool itool = new ITool() {
			private int	statusCode	= 1;

			@Override
			public String execute(File workingDir, String stdin) {
				for (int i = 0; i < 1000; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						return null;
					}
				}
				statusCode = 0;
				return "finished!";
			}

			@Override
			public int getStatusCode() {
				return statusCode;
			}
		};

		Future<?> future = shell.executeTest(itool);
		boolean result = false;
		// generally wait for a while
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (!(future.isDone() || future.isCancelled())) {
			result = shell.stopTest(future, itool);
		}

		assertTrue(result);
		assertNotEquals(0, itool.getStatusCode());
	}

	@Test
	public void testTerminatingNonStopThread() {
		//
		//
		// This test focused on the forever thread
		// which is not able to interrupt
		//
		//
		ITool itool = new ITool() {
			private int	statusCode	= 1;

			@Override
			public String execute(File workingDir, String stdin) {
				@SuppressWarnings("unused")
				double a = 0;
				for (double i = 1; i < 1000000000; i += 0.000000001) {
					a += i;
				}
				statusCode = 0;
				return "finished!";
			}

			@Override
			public int getStatusCode() {
				return statusCode;
			}
		};

		Future<?> future = shell.executeTest(itool);
		boolean result = false;
		// generally wait for a while
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (!(future.isDone() || future.isCancelled())) {
			result = shell.stopTest(future, itool);
		}

		assertTrue(result);
		assertNotEquals(0, itool.getStatusCode());
	}
}
