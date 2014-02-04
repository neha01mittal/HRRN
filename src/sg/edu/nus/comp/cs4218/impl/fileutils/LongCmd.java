package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class LongCmd extends ATool implements ITool {

	public LongCmd(String[] arguments) {
		super(null);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
				// System.out.println("Still running... (" + i + ")");
			} catch (InterruptedException e) {
				setStatusCode(1);
				return "   process interrupted!";
			}
		}
		return "Long finished!";
	}

}
