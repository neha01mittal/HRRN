package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Lists all the files and subdirectories in the current directory or provided
 * class
 * 
 * @usage ls [-a | -R] [path]
 * @options ls : Lists all files in current directory. ls [path] : Lists all
 *          files in the given path. ls -a : Lists all files including hidden
 *          files in current directory. ls -a [path] : Lists all files including
 *          hidden files in the given path ls -R : Lists all the files (absolute
 *          path) in the current directory and subdirectories ls -R [path] :
 *          Lists all the files (absolute path) in the given path. ls -a -R :
 *          Combination of functions above ls file : Prints file path if it
 *          exists in the file system.
 * @note [path] could be either an absolute file or a relative path. If multiple
 *       path is given as arguments, only the first argument will be
 *       entertained. Ls tool does not allow std, thus can not be pipe to. The
 *       recursive variable is printed in absolute path to show the hierarchical
 *       structure.
 * @success return the list of file names or path in the
 * @exceptions invalid input path not exist retrieve file list error
 */

public class LsTool extends ATool implements ILsTool {

	private final List<String>	argList;
	private final List<String>	inputList;
	/**
	 * Constructor 
	 * @param arguments The path sent as args  to change the directory 
	 */
	public LsTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
	}
	
	/**
	 * Executes the tool with args provided in the constructor 
	 * @param workingDir The working directory on which the tool will operate on 
	 * @param stdin Stdin is not used 
	 * @return Output on stdout
	 */
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
			}

			if (newDir != null && newDir.isDirectory() && newDir.exists()) {
				fileList = getFiles(newDir);
			} else if (newDir != null && newDir.isFile() && newDir.exists()) {
				setStatusCode(0);
				return validInput;
			} else {
				return "ls: " + validInput + ": No such file or directory";
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
		return "ls: Error: retrieve file list error";
	}

	/**
	 * 
	 * Retrieves the contents (list of folders and files) in the path
	 * 
	 * @param directory The directory whose contents need to be retieved 
	 * 
	 * @return List of file/folder paths  
	 * 
	 */
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
		return fileList;
	}

	/**
	 * 
	 * Returns the sting with all the file names 
	 * For option -R: List of files (absolute path) in the directory and subdirectories
	 * For -a : List of all files including hidden file
	 * 
	 * @param files List of files
	 * 
	 * @return String with all file names appended
	 * 
	 */
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
