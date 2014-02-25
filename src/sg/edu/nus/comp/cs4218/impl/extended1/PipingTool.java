package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LongCmd;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

/**
 * The pipe tools allows the output of one program to be sent to the input of
 * another program. With the help of pipe tool multiple small (and simple)
 * programs can be connected to accomplish large number of tasks.
 * 
 * Command Format
 * 
 * - PROGRAM-1-STANDARD_OUTPUT | PROGRAM-2-STANDARD_INPUT Where "|" is the pipe
 * operator and PROGRAM-1-STANDARD_OUTPUT is the standard output of program 1
 * and PROGRAM-2-STANDARD_INPUT is the standard input of program 2.
 * 
 * Pipe tool will always return with status code 0, because the it does not
 * execute anything
 */
public class PipingTool extends ATool implements IPipingTool {
	File	workingDir;

	public PipingTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		this.workingDir = workingDir;

		if (args != null && args.length > 1) {
			String result;
			ITool from;
			ITool to;
			from = parse(args[0]);
			to = parse(args[1]);
			result = pipe(from, to);

			for (int i = 1; i < args.length; i++) {
				to = parse(args[i]);
				result = pipe(result, to);
			}

			return result;
		}
		return null;
	}

	@Override
	public String pipe(ITool from, ITool to) {
		// execute command 1
		String returnedValue = from.execute(workingDir, null);
		if (from.getStatusCode() != 0 && returnedValue != null) {
			System.err.println(returnedValue);
			return "";
		}
		return returnedValue;
	}

	@Override
	public String pipe(String stdout, ITool to) {
		// execute command 2
		String returnedValue = to.execute(workingDir, stdout);
		if (to.getStatusCode() != 0 && returnedValue != null) {
			System.err.println(returnedValue);
			return "";
		}
		return returnedValue;
	}

	public ITool parse(String c) {
		String commandline = c;
		commandline = commandline.trim();
		String[] cmdSplit = commandline.split("\\s+");
		if (commandline.length() > 0 && cmdSplit.length > 0) {
			// This guarantee valid
			String cmd = cmdSplit[0].toLowerCase();
			// Now we need to construct arguments
			String[] args = Shell.getArgsArray(commandline);
			switch (cmd) {
				case "cat":
					return new CatTool(args);
				case "cd":
					return new CdTool(args);
				case "copy":
					return new CopyTool(args);
				case "delete":
					return new DeleteTool(args);
				case "echo":
					return new EchoTool(args);
				case "ls":
					return new LsTool(args);
				case "move":
					return new MoveTool(args);
				case "pwd":
					return new PWDTool();
				case "grep":
					return new GrepTool(args);
				case "comm":
					return new LongCmd(args);
				case "cut":
					return new LongCmd(args);
				case "sort":
					return new LongCmd(args);
				case "paste":
					return new LongCmd(args);
				case "uniq":
					return new LongCmd(args);
				case "wc":
					return new LongCmd(args);
			}
		}
		return null;
	}

}
