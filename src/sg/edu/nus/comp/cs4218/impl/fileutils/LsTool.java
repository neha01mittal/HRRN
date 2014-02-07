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

			if (stdin != null && stdin.trim().length() > 1) {
				inputList.add(stdin);
			}
		}

		List<File> fileList = getFiles(workingDir);
		if (fileList != null) {
			setStatusCode(0);
			return getStringForFiles(fileList);
		}
		return null;
	}

	@Override
	public List<File> getFiles(File directory) {
		// Error Handling
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			return null;
		}

		File[] files = directory.listFiles();

		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			if (argList.contains("-a") && !f.isHidden()) {
				fileList.add(f);
			} else {
				fileList.add(f);
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
			if (argList.contains("-l")) {
				result += files.get(i).getName() + " " + files.get(i).getUsableSpace();
			} else if (argList.contains("-R")) {
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
