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
	/**
	 * Constructor 
	 * 
	 */
	public PWDTool() {
		super(null);
		setStatusCode(1);
	}

	/**
	 * Executes the tool with args provided in the constructor 
	 * 
	 * @param workingDir The working directory on which the tool will operate on
	 * @param stdin Input on stdin. Can be null. If args are missing, stdin is used 
	 * @return Output on stdout
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		return getStringForDirectory(workingDir);
	}

	/**
	 * Checks if the path of working directory exists and is a directory
	 * 
	 * @param directory The working directory
	 * 
	 * @return Absolute path of the working directory as a string
	 */
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
