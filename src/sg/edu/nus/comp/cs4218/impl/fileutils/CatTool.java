package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * @usage cat [ - ] [string | path]
 * @options Cat [filename] contents of file Cat [filename1] [filename2] contents
 *          of both files Cat - ���������������������������text text��������������������������� output- text text, Anything
 *          preceded by ���������������������������-��������������������������� is printed as it is
 * @note If files extensions are such that they not readable properly (eg: pdf),
 *       it might display garbage values. ls | cat and ls | cat - will print as
 *       expected (contents of that directory or in other words standard input
 *       it receives from the ���������������������������from��������������������������� tool). Prioritizes args over stdin i.e.
 *       if there are args in front, it will execute them and ignore stdin eg:
 *       ls | cat file1.txt will print the contents of file1 and not the
 *       contents of current directory@note
 *       can also use both stdin and arg. Eg: echo test1 | cat - filename
 *       prints stdin followed by file's content
 * @success
 * @exceptions
 **/
public class CatTool extends ATool implements ICatTool {
	/**
	 * Constructor 
	 * @param arguments The filename or folder path sent as args  to print
	 */
	public CatTool(String[] arguments) {
		super(arguments);
	}

	/**
	 * Executes the tool with args provided in the constructor 
	 * @param workingDir The working directory on which the tool will operate on 
	 * @param stdin Input on stdin. Can be null. If args are missing, stdin is used (and printed directly. It is not considered as a file path)
	 * @return Output on stdout
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		File file;

		if ((args != null && args.length > 0) || (stdin != null)) {
			String content = "";
			if (args == null || args.length == 0) {
				if (stdin != "") {
					content = stdin;
					setStatusCode(0);
				}
			} else {
				for (String arg : args) {
					if (!arg.equals("-")) {
						file = new File(arg);
						if (!file.isAbsolute()) {
							file = new File(workingDir, arg);
						}
						content += getStringForFile(file);
					} else {
						content += stdin;
						setStatusCode(0);
					}
				}
			}
			return content;
		}
		return "Error: No input receieved";
	}
	/**
	 * 
	 * Reads the contents of the file path and returns it as a string
	 * 
	 * @param toRead The file path whose content will be displayed on console
	 * 
	 * @return The content of file
	 */
	@Override
	public String getStringForFile(File toRead) {
		BufferedReader br;
		String content = "";
		if (!toRead.isFile()) { // checks for Exists and !isDirectory
			content = "Error: No such file or directory\n";
		}
		 else {
			try {
				br = new BufferedReader(new FileReader(toRead));
				String line = null;
				try {
					while ((line = br.readLine()) != null) {
						content += line + "\n";
					}
					br.close();
					setStatusCode(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

}
