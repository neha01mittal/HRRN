package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * list the contents of a directory
 * 
 * @author Zhang Haoqiang
 */
public class LsTool extends ATool implements ILsTool {

	private final List<String> argList;
	private final List<String> inputList;

	public LsTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// check for argument number
		if (!(args == null || args.length < 1)) {

			// split arguments and inputs
			for (String arg : args) {
				if (arg.startsWith("-")) {
					argList.add(arg);
				} else {
					inputList.add(arg);
				}
			}
		}

		List<File> fileList = null;
		if (inputList.size() > 0) {
			String validInput = inputList.get(0);
			File newDir = new File(validInput);

			if (!newDir.isAbsolute()) {
				newDir = new File(workingDir.getAbsolutePath() + File.separator + validInput);
				System.out.println(workingDir.getAbsolutePath() + File.separator + validInput);
			}

			if (newDir != null && newDir.isDirectory() && newDir.exists()) {
				fileList = getFiles(newDir);
			} else if (newDir != null && newDir.isFile() && newDir.exists()) {
				if (argList.contains("-a")) {
					setStatusCode(0);
					return validInput;
				} else if (!newDir.isHidden()) {
					setStatusCode(0);
					return validInput;
				}
			} else {
				return "Error: invalid input: " + validInput;
			}
		} else {
			fileList = getFiles(workingDir);
		}

		if (fileList != null) {
			setStatusCode(0);
			if (fileList.size() < 1) {
				return null;
			}
			return getStringForFiles(fileList);
		}
		return "Error: retrieve file list error";
	}

	@Override
	public List<File> getFiles(File directory) {
		// Error Handling
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			return null;
		}

		List<File> fileList = new ArrayList<File>();

		File[] files = directory.listFiles();
		for (File f : files) {
			if (argList.contains("-a")) {
				fileList.add(f);
			} else if (!f.isHidden()) {
				fileList.add(f);
			} else {
				continue;
			}
			if (argList.contains("-R") && f.isDirectory()) {
				fileList.addAll(getFiles(f));
			}
		}
		// Processing
		return fileList;
	}

	@Override
	public String getStringForFiles(List<File> files) {
		String result = "";
		for (int i = 0; i < files.size(); i++) {
			if (argList.contains("-R")) {
				result += files.get(i).getAbsolutePath();
			} else {
				result += files.get(i).getName();
			}
			if (i != files.size() - 1) {
				result += "\n";
			}
		}
		return result;
	}
}
