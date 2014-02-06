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

		int option = 0;
		if (args != null && args.length > 0) {
			if (args[0].equals("-a")) {
				option = 1;
			}
		}
		File[] files = directory.listFiles();

		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			switch (option) {
			case 0:
				if (!f.isHidden()) {
					fileList.add(f);
				}
				break;
			case 1:
				fileList.add(f);
				break;
			}
		}
		// Processing
		return fileList;
	}

	@Override
	public String getStringForFiles(List<File> files) {

		String result = "";
		int option = 0;
		if (args != null && args.length > 0) {
			if (args[0].equals("-l")) {
				option = 1;
			}
		}
		for (int i = 0; i < files.size(); i++) {
			switch (option) {
			case 0:
				result += files.get(i).getName();
				break;
			case 1:
				result += files.get(i).getName() + " " + files.get(i).getUsableSpace();
				break;
			}
			if (i != files.size() - 1) {
				result += "\n";
			}
		}
		return result;
	}
}
