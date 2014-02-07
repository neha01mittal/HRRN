package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class PWDTool extends ATool implements IPwdTool {

	public PWDTool() {
		super(null);
		setStatusCode(1);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		return getStringForDirectory(workingDir);
	}

	@Override
	public String getStringForDirectory(File directory) {

		// Error Handling
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			setStatusCode(1);
			return "Error: Cannot find working directory";
		}
		// Processing
		setStatusCode(0);
		return directory.getAbsolutePath();
	}

}
