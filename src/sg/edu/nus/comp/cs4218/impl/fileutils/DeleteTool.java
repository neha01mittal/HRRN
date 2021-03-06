package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Delete a file or folder
 * 
 * @usage delete [path]
 * @options delete file1 - Deletes file1 delete relativepath1 - Converts
 *          relativepath to absolutepath and deletes file1 delete /../file1 -
 *          Deletes file1 delete "file1" - Deletes file1 delete newfile - Does
 *          nothing delete folder1 - Deletes folder and all its contents
 * @note
 * @success
 * @exceptions
 * 
 */

public class DeleteTool extends ATool implements IDeleteTool {

	private final List<String> inputList;

	/**
	 * Constructor
	 * 
	 * @param arguments
	 *            The filename or folder path sent as args to delete
	 */
	public DeleteTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		setStatusCode(1);
		inputList = new ArrayList<String>();
	}

	/**
	 * Executes the tool with args provided in the constructor
	 * 
	 * @param workingDir
	 *            The working directory on which the tool will operate on
	 * @param stdin
	 *            Stdin is not used
	 * @return Output on stdout
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		// check for argument number
		if (args == null || args.length < 1) {
			if (stdin == null || stdin.trim().length() < 1) {
				setStatusCode(1);
				return "No input received.";
			}
		} else {

			for (String arg : args) {
				inputList.add(arg);
			}

		}
		File f1 = new File(inputList.get(0));
		if (!(f1.isAbsolute())) {
			f1 = new File(workingDir, inputList.get(0));
		}
		if (delete(f1)) {
			setStatusCode(0);
		}
		return null;

	}

	/**
	 * Delete a file or folder if it exists
	 * 
	 * @param toDelete
	 *            Source path of the file/folder
	 * 
	 * @return Outcome of the delete operation
	 * 
	 */
	@Override
	public boolean delete(File toDelete) {
		// TODO Auto-generated method stub
		if (toDelete.exists()) {
			recursivedelete(toDelete);
		} else {
			setStatusCode(1);
		}
		return true;
	}

	/**
	 * Deletes files recursively in case path is a folder
	 * 
	 * @param file
	 *            path of the file/folder to be deleted
	 * 
	 */
	public void recursivedelete(File file) {
		try {
			if (file.isDirectory()) {

				String[] fList = file.list();
				for (int index = 0; index < fList.length; index++) {
					File source = new File(file, fList[index]);
					// Recursion call take place here
					recursivedelete(source);
				}
				// Delete the source folders
				for (int i = 0; i < fList.length; i++) {
					File source = new File(file, fList[i]);
					if (!Files.deleteIfExists(source.toPath())) {
						setStatusCode(1);
					}
				}
				if (Files.deleteIfExists(file.toPath())) {
					setStatusCode(0);
				}
			} else {
				// Found a file. Delete it
				if (Files.deleteIfExists(file.toPath()))
					setStatusCode(0);
			}

		} catch (Exception ex) {
			// Handle all the relevant exceptions here
			setStatusCode(1);
		}
	}
}
