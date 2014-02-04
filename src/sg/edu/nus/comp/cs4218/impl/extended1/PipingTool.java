package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
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
 * Command Format - PROGRAM-1-STANDARD_OUTPUT | PROGRAM-2-STANDARD_INPUT Where
 * "|" is the pipe operator and PROGRAM-1-STANDARD_OUTPUT is the standard output
 * of program 1 and PROGRAM-2-STANDARD_INPUT is the standard input of program 2.
 * 
 */
public class PipingTool extends ATool implements IPipingTool {

	private static String	dilimiter	= "::space::";

	public PipingTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		if (args.length > 1) {
			String result = "";
			ITool from = parse(args[0]);
			ITool to = parse(args[1]);
			result = pipe(from, to);
			return result;
		}
		return null;
	}

	@Override
	public String pipe(ITool from, ITool to) {
		// TODO Auto-generated method stub
		File f = new File(System.getProperty("user.dir"));
		// execute command
		String returnedValue = from.execute(f, "");
		// print if has output
		// args 1-> length
		for (int i = 1; i < args.length; i++) {
			if (returnedValue != null && returnedValue.trim().length() > 0) {
				returnedValue = to.execute(f, returnedValue);
			}
		}

		return returnedValue;
	}

	@Override
	public String pipe(String stdout, ITool to) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITool parse(String commandline) {

		commandline = commandline.trim();
		String[] cmdSplit = commandline.split("\\s+");
		if (commandline.length() > 0 && cmdSplit.length > 0) {

			String cmd = cmdSplit[0]; // This guarantee valid
			// Now we need to construct arguments
			String[] args = getArgsArray(commandline);

			if (cmd.equals("cat")) {
				return new CatTool(args);
			} else if (cmd.equals("cd")) {
				return new CdTool(args);
			} else if (cmd.equals("copy")) {
				return new CopyTool(args);
			} else if (cmd.equals("delete")) {
				return new DeleteTool(args);
			} else if (cmd.equals("echo")) {
				return new EchoTool(args);
			} else if (cmd.equals("ls")) {
				return new LsTool(args);
			} else if (cmd.equals("move")) {
				return new MoveTool(args);
			} else if (cmd.equals("pwd")) {
				return new PWDTool();
			} else if (cmd.equals("grep")) {
				return new GrepTool(args);
			} else if (cmd.equals("long")) {
				return new LongCmd(args);
			}
		}
		return null;
	}

	private String[] getArgsArray(String commandline) {
		List<String> argList = new ArrayList<String>();
		commandline = commandline.replaceAll("\\\\\\s", dilimiter);
		Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
		Matcher regexMatcher = regex.matcher(commandline);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				argList.add(regexMatcher.group(1).replaceAll(dilimiter, " "));
			} else if (regexMatcher.group(2) != null) {
				argList.add(regexMatcher.group(2).replaceAll(dilimiter, " "));
			} else {
				argList.add(regexMatcher.group().replaceAll(dilimiter, " "));
			}
		}
		if (argList.size() > 0) {
			argList.remove(0);
			return argList.toArray(new String[0]);
		} else {
			return null;
		}
	}

}
