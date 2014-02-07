package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Show the current directory
 * 
 * @usage pwd
 * @note pwd tool does not take in any arguments or stdin, thus it could not be
 *       piped to. It will set the initial path to the directory where the
 *       program start.
 * @success return a string showing the absolute path of current working
 *          directory.
 * @exceptions Cannot find working directory
 */
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
			return "Error: Cannot find working directory";
		}
		// Processing
		setStatusCode(0);
		return directory.getAbsolutePath();
	}

}
