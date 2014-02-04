package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * delete a file
 */
public class DeleteTool extends ATool implements IDeleteTool {

	public DeleteTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		File f1 = new File(args[0]);
		if (!(f1.isAbsolute())) {
			f1 = new File(workingDir, args[0]);
		}
		try {
			f1 = new File(f1.getCanonicalPath());

		} catch (IOException e) {
			setStatusCode(1);
		}
		if (delete(f1)) {
		}
		return null; 
	}

	@Override
	public boolean delete(File toDelete) {
		// TODO Auto-generated method stub
		if (toDelete.exists())
		{
			recursivedelete(toDelete);
		}
		else {
			setStatusCode(1);
		}
		return true;
	}

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
					Files.deleteIfExists(source.toPath());
				}
				Files.deleteIfExists(file.toPath());
			}
			else {
				// Found a file. Delete it
				if (!(Files.deleteIfExists(file.toPath()))) {
					setStatusCode(1);
				}
				else {
					setStatusCode(0);
				}

			}

		} catch (Exception ex) {
			// Handle all the relevant exceptions here
			setStatusCode(1);
		}
	}
}
