package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * cat copies each file or standard input ( denoted by ï¿½-ï¿½) if no files are
 * given to the standard output
 */

/**
 * 
 * @author Neha Requirements/ Assumptions 1. Set status code to 0 even if one
 *         file is copied correctly 2. Does not handle cat file1 file2 > newFile
 *         3.
 * 
 */
public class CatTool extends ATool implements ICatTool {

	public CatTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		File file;
		if ((args != null && args.length != 0) || (stdin != null && !stdin.equals(""))) {
			String content = "";
			if (!stdin.equals("")) {
				// TODO
				content = stdin;
				setStatusCode(0);
			}
			else if (args.length >= 1) {
				for (String arg : args) {
					file = new File(arg);
					if (!file.isAbsolute()) {
						file = new File(workingDir, arg);
					}
					content += getStringForFile(file);
				}
			}
			return content;
		}
		return null;
	}

	@Override
	public String getStringForFile(File toRead) {
		// TODO Auto-generated method stub
		BufferedReader br;
		String content = "";
		if (!toRead.isFile()) { // checks for Exists and !isDirectory
			content = "cat: No such file exists\n";
		}

		else if (!toRead.canRead()) {
			content = "Unable to read this file type";
		}
		else {
			try {
				br = new BufferedReader(new FileReader(toRead));
				String line = null;
				try {
					// content += "Reading file: " + toRead.getName() + ": ";
					while ((line = br.readLine()) != null) {
						content += line + "\n";
					}
					br.close();
					setStatusCode(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}

}
