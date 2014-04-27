package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Move a file/folder to a given location
 * 
 * @usage move [path] [path] ��� [path to folder]
 * @options move file1 file2 - Moves file1 contents into file2 move /../file1
 *          file2 - Moves file1 contents into file2 move "file1" "file2" - Moves
 *          file1 contents into file2 move file1 newfile - Creates newfile and
 *          moves file1 contents into newfile move file1 folder - Moves file1
 *          into folder move file1 newfolder - Moves file1 into newfolder move
 *          folder1 folder2 - Moves contents of folder1 into folder2 move
 *          folder1 newfolder - Moves folder1 into newfolder move file1 file2
 *          file3 folder1 - Moves file1, file2, file3 in folder1 move folder1
 *          folder2 file3 newfolder - Moves folder1, folder2, file3 into
 *          newfolder
 * @note
 * @success
 * @exceptions
 */

public class MoveTool extends ATool implements IMoveTool {

	private final List<String> inputList;

	/**
	 * Constructor
	 * 
	 * @param arguments
	 *            The filename or folder path sent as args to move
	 */
	public MoveTool(String[] arguments) {
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
		if (args == null || args.length < 1) {
			return "No input received.";
		} else {

			for (String arg : args) {
				inputList.add(arg);
			}

		}
		for (int i = 0; i < inputList.size() - 1; i++) {
			File f1 = new File(args[i]);
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
				// TODO Auto-generated catch block
				setStatusCode(1);
			}
			if (f1.isFile()) {
				if (f2.isDirectory()) {
					f2 = new File(inputList.get(inputList.size() - 1),
							f1.getName());
				}
			}
			move(f1, f2);
		}
		return null;
	}

	/**
	 * Move a file to a given location, move a file to another existing file In
	 * case of multiple paths, all the files are moved into the final path (if
	 * it exists)
	 * 
	 * @param from
	 *            Source path of the file/folder
	 * @param to
	 *            Destination to be copied to
	 * 
	 * @return Outcome of the move operation
	 * 
	 */
	@Override
	public boolean move(File from, File to) {
		// TODO Auto-generated method stub
		recursivemove(from, to);
		return true;
	}

	/**
	 * Moves files recursively to a location in case source path is a folder
	 * 
	 * @param from
	 *            Source path of the file/folder
	 * @param to
	 *            Destination to be copied to
	 * 
	 */
	public void recursivemove(File from, File to) {
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
					File toDirectory = new File(to.toString() + File.separator
							+ from.getName());
					toDirectory.mkdir();
					// Create list of files and directories on the current
					// source
					String[] fList = from.list();

					for (int index = 0; index < fList.length; index++) {
						File dest = new File(toDirectory, fList[index]);
						File source = new File(from, fList[index]);

						// Recursion call take place here
						recursivemove(source, dest);
					}
					// Delete the source folders
					for (int i = 0; i < fList.length; i++) {
						File source = new File(from, fList[i]);
						Files.deleteIfExists(source.toPath());
					}
					Files.deleteIfExists(from.toPath());
				}
			} else {
				// Found a file. Copy it into the destination
				if (from.exists()) {
					Files.move(from.toPath(), to.toPath(),
							StandardCopyOption.REPLACE_EXISTING);
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
