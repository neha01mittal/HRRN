package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class LongCmd extends ATool implements ITool {

	public LongCmd(String[] arguments) {
		super(null);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		return LongWrite();
	}

	public String inputBlocked() {
		for (double i = 1; i < 1000000; i += 0.1) {
			System.out.println("" + i + "");
		}
		return "finished!";
	}

	public String SimpleSleep() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
				// System.out.println("Still running... (" + i + ")");
			} catch (InterruptedException e) {
				setStatusCode(1);
				return "process interrupted!";
			}
		}
		return "finished!";
	}

	public String LongWrite() {
		try {
			File file = new File("test.txt");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (double i = 1; i < 1000000; i += 0.1) {
				bw.write("" + i + "\n");
			}
			bw.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "finished!";
	}

}
