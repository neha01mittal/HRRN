package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MoveTool extends ATool implements IMoveTool {

	public MoveTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		for (int i = 0; i < (args.length - 1); i++) {
			File f1 = new File(args[i]);
			File f2 = new File(args[args.length - 1]);
			if (!(f1.isAbsolute())) {
				f1 = new File(workingDir, args[i]);
			}
			if (!(f2.isAbsolute())) {
				f2 = new File(workingDir, args[args.length - 1]);
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
					f2 = new File(args[args.length - 1], f1.getName());
				}
			}
			move(f1, f2);
		}
		return null;
	}

	@Override
	public boolean move(File from, File to) {
		// TODO Auto-generated method stub
		recursivemove(from, to);
		return true;
	}

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
					File toto = new File(to.toString() + File.separator + from.getName());
					toto.mkdir();
					// Create list of files and directories on the current
					// source
					String[] fList = from.list();

					for (int index = 0; index < fList.length; index++) {
						File dest = new File(toto, fList[index]);
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
					Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
