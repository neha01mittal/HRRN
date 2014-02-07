package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * change directory
 * 
 * @author Zhang Haoqiang
 */
public class CdTool extends ATool implements ICdTool {

	private final List<String> argList;
	private final List<String> inputList;

	public CdTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
	}

	@Override
	public String execute(File workingDir, String stdin) {

		// check for argument number
		if (args == null || args.length < 1) {
			System.setProperty("user.dir", System.getProperty("user.home"));
			setStatusCode(0);
			return null;
		} else {

			// split arguments and inputs
			for (String arg : args) {
				if (arg.startsWith("-")) {
					argList.add(arg);
				} else {
					inputList.add(arg);
				}
			}

			if (stdin != null && stdin.trim().length() > 1) {
				inputList.add(stdin);
			}
		}

		// Only need the first one
		String validInput = inputList.get(0);

		File newdir;
		File file = new File(validInput);
		if (file.isAbsolute()) {
			newdir = changeDirectory(validInput);
		} else {
			newdir = changeDirectory(workingDir.getAbsolutePath() + File.separator + validInput);
		}

		if (newdir != null) {
			try {
				// get the nicely looking path
				System.setProperty("user.dir", newdir.getCanonicalPath());
				setStatusCode(0);
				return null;
			} catch (IOException e) {
				// error code 2: IOException to get canonical path
				e.printStackTrace();
			}
		}
		return "Error: No such directory or not directory: " + validInput;
	}

	// String newDirectory must be an absolute path
	@Override
	public File changeDirectory(String newDirectory) {

		File newdir = new File(newDirectory);

		// Error Handling
		if (newdir == null || !newdir.exists() || !newdir.isDirectory()) {
			// error code 1: Path not valid, not exist
			return null;
		}

		return newdir;
	}
}
