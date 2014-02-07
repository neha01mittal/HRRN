package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * copy a file to a given location
 * 
 */
public class CopyTool extends ATool implements ICopyTool {
	private final List<String> inputList;

	public CopyTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		inputList = new ArrayList<String>();
	}

	@Override
	public String execute(File workingDir, String stdin) {

		if (args == null || args.length < 1) {
			if (stdin == null || stdin.trim().length() < 1) {
				return "No input received.";
			}
		} else {

			for (String arg : args) {
				inputList.add(arg);
			}
		}
		for (int i = 0; i < (inputList.size() - 1); i++) {

			File f1 = new File(inputList.get(i));
			File f2 = new File(inputList.get(inputList.size() - 1));
			if (!(f1.isAbsolute())) {
				f1 = new File(workingDir, inputList.get(i));
			}
			if (!(f2.isAbsolute())) {
				f2 = new File(workingDir, inputList.get(inputList.size() - 1));
			}
			try {
				f1 = new File(f1.getCanonicalPath());
				f2 = new File(f2.getCanonicalPath());

			} catch (IOException e) {
				setStatusCode(1);
			}
			if (f1.isFile()) {
				if (f2.isDirectory()) {
					f2 = new File(inputList.get(inputList.size() - 1), f1.getName());
				}
			}
			copy(f1, f2);
		}
		return null;
	}

	@Override
	public boolean copy(File from, File to) {
		recursivecopy(from, to);
		return true;
	}

	public void recursivecopy(File from, File to) {
		try {
			if (from.isDirectory()) {
				// If destination is a file or a file path
				if (to.isFile()) {
					setStatusCode(1);
				} else {
					// If the destination is not exist then create it
					if (!to.exists()) {

						to.mkdirs();
					}

					// Create list of files and directories on the current
					// source
					String[] fList = from.list();

					for (int index = 0; index < fList.length; index++) {
						File dest = new File(to, fList[index]);
						File source = new File(from, fList[index]);

						// Recursion call take place here
						recursivecopy(source, dest);
					}
				}
			} else {
				// Found a file. Copy it into the destination
				if (from.exists()) {
					Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
					setStatusCode(0);
				} else {
					setStatusCode(1);
				}
			}

		} catch (Exception ex) {
			// Handle all the relevant exceptions here
			setStatusCode(1);
		}
	}
}
