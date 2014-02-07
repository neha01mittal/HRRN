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
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

/**
 * The pipe tools allows the output of one program to be sent to the input of
 * another program. With the help of pipe tool multiple small (and simple)
 * programs can be connected to accomplish large number of tasks.
 * 
 * Command Format - PROGRAM-1-STANDARD_OUTPUT | PROGRAM-2-STANDARD_INPUT Where
 * "|" is the pipe operator and PROGRAM-1-STANDARD_OUTPUT is the standard output
 * of program 1 and PROGRAM-2-STANDARD_INPUT is the standard input of program 2.
 * 
 */
public class PipingTool extends ATool implements IPipingTool {

	File workingDir;

	public PipingTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		this.workingDir = workingDir;
		if (args.length == 2) {
			ITool from = parse(args[0]);
			ITool to = parse(args[1]);

			return pipe(from, to);
		}
		return null;
	}

	@Override
	public String pipe(ITool from, ITool to) {
		// execute command 1
		String returnedValue = from.execute(workingDir, null);
		if (from.getStatusCode() != 0) {
			setStatusCode(1);
			return "Error executing first tool.";
		}
		return pipe(returnedValue, to);
	}

	@Override
	public String pipe(String stdout, ITool to) {
		// execute command 2
		String returnedValue = to.execute(workingDir, stdout);
		if (to.getStatusCode() != 0) {
			setStatusCode(1);
			return "Error executing second tool.";
		}
		return returnedValue;
	}

	public ITool parse(String commandline) {
		if (commandline.contains("|")) {
			return new PipingTool(commandline.split("|", 2));
		} else {
			String trimmed = commandline.trim();
			String[] cmdSplit = trimmed.split("\\s+");
			if (trimmed.length() > 0 && cmdSplit.length > 0) {
				String cmd = cmdSplit[0].toLowerCase();
				String[] args = Shell.getArgsArray(trimmed);

				if (cmd.equalsIgnoreCase("cat")) {
					return new CatTool(args);
				} else if (cmd.equalsIgnoreCase("cd")) {
					return new CdTool(args);
				} else if (cmd.equalsIgnoreCase("copy")) {
					return new CopyTool(args);
				} else if (cmd.equalsIgnoreCase("delete")) {
					return new DeleteTool(args);
				} else if (cmd.equalsIgnoreCase("echo")) {
					return new EchoTool(args);
				} else if (cmd.equalsIgnoreCase("ls")) {
					return new LsTool(args);
				} else if (cmd.equalsIgnoreCase("move")) {
					return new MoveTool(args);
				} else if (cmd.equalsIgnoreCase("pwd")) {
					return new PWDTool();
				} else if (cmd.equalsIgnoreCase("grep")) {
					return new GrepTool(args);
				}
			}
		}
		return null;
	}

}
