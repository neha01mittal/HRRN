package sg.edu.nus.comp.cs4218.impl;

import org.junit.Test;

public class ShellTest {

	@Test
	public void test() {
		new Shell().stop(new Runnable() {

			@Override
			public void run() {
				// do nothing
			}
		});
	}

}
