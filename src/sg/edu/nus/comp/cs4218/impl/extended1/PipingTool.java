package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

	private static String dilimiter1 = "::escape-space::";
	private static String dilimiter2 = "::space::";

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
		returnedValue = to.execute(f, returnedValue);
		// print if has output
		// args 1-> length
		for (int i = 2; i < args.length; i++) {
			if (returnedValue != null && returnedValue.trim().length() > 0) {
				to = parse(args[i]);
				returnedValue = to.execute(f, returnedValue);
			}
		}
		setStatusCode(to.getStatusCode());
		return returnedValue;
	}

	@Override
	public String pipe(String stdout, ITool to) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String[] getArgsArray(String command) {
		// Step 1. remove all the surround quotes
		Pattern regex = Pattern.compile("[^'\"]*(\"[^\"]*\")[^'\"]*|[^'\"]*('[^']*')[^'\"]*");
		Matcher regexMatcher = regex.matcher(command);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				String temp = regexMatcher.group(1);
				// System.out.println("group1: " + temp);
				String commandline = command.replace(temp, temp.substring(1, temp.length() - 1));
				command = commandline;
				// System.out.println("group1: " + commandline);
			}
			if (regexMatcher.group(2) != null) {
				String temp = regexMatcher.group(2);
				// System.out.println("group2: " + temp);
				String commandline = command.replace(temp, temp.substring(1, temp.length() - 1));
				command = commandline;
				// System.out.println("group2: " + commandline);
			}
		}
		// System.out.println(commandline);
		// Step 2. find the escape space in quotes
		regex = Pattern.compile("['][^']*\\s+[^']*[']|[\"][^\"]*\\s+[^\"]*[\"]");
		regexMatcher = regex.matcher(command);
		while (regexMatcher.find()) {
			if (regexMatcher.group() != null) {
				String temp = regexMatcher.group();
				String replaced = regexMatcher.group().replaceAll("\\s", dilimiter1);
				String newString = command.replace(temp, replaced);
				command = newString;
			}
		}
		// System.out.println(commandline);
		String replacedCommand = command.replaceAll("\\\\\\s", dilimiter2);
		command = replacedCommand;
		// System.out.println(commandline);

		// Step 3. remove the first one and switch back delimiter
		List<String> argList = new ArrayList<String>(Arrays.asList(command.split("\\s+")));
		for (int i = 0; i < argList.size(); i++) {
			argList.set(i, argList.get(i).replaceAll(dilimiter1, " ").replaceAll(dilimiter2, " "));
		}
		if (argList.size() > 0) {
			argList.remove(0);
			return argList.toArray(new String[0]);
		} else {
			return null;
		}
	}

	public ITool parse(String commandline) {
		if (commandline.contains("|")) {
			return new PipingTool(commandline.split("\\|"));
		} else {
			String trimmed = commandline.trim();
			String[] cmdSplit = trimmed.split("\\s+");
			if (trimmed.length() > 0 && cmdSplit.length > 0) {

				String cmd = cmdSplit[0].toLowerCase(); // This guarantee valid
				// Now we need to construct arguments
				String[] args = getArgsArray(trimmed);

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
				} else if (cmd.equalsIgnoreCase("long")) {
					return new LongCmd(args);
				}
			}
		}
		return null;
	}

}
