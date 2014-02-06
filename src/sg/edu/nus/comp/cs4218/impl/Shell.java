package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
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
 * The Shell is used to interpret and execute user's commands. Following
 * sequence explains how a basic shell can be implemented in Java
 */
public class Shell implements IShell {

	private static String dilimiter1 = "::escape-space::";
	private static String dilimiter2 = "::space::";

	public static String[] getArgsArray(String commandline) {
		// Step 1. remove all the surround quotes
		Pattern regex = Pattern.compile("[^'\"]*(\"[^\"]*\")[^'\"]*|[^'\"]*('[^']*')[^'\"]*");
		Matcher regexMatcher = regex.matcher(commandline);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				String temp = regexMatcher.group(1);
				// System.out.println("group1: " + temp);
				commandline = commandline.replace(temp, temp.substring(1, temp.length() - 1));
				// System.out.println("group1: " + commandline);
			}
			if (regexMatcher.group(2) != null) {
				String temp = regexMatcher.group(2);
				// System.out.println("group2: " + temp);
				commandline = commandline.replace(temp, temp.substring(1, temp.length() - 1));
				// System.out.println("group2: " + commandline);
			}
		}
		// System.out.println(commandline);
		// Step 2. find the escape space in quotes
		regex = Pattern.compile("['][^']*\\s+[^']*[']|[\"][^\"]*\\s+[^\"]*[\"]");
		regexMatcher = regex.matcher(commandline);
		while (regexMatcher.find()) {
			if (regexMatcher.group() != null) {
				String temp = regexMatcher.group();
				String replaced = regexMatcher.group().replaceAll("\\s", dilimiter1);
				commandline = commandline.replace(temp, replaced);
			}
		}
		// System.out.println(commandline);
		commandline = commandline.replaceAll("\\\\\\s", dilimiter2);
		// System.out.println(commandline);

		// Step 3. remove the first one and switch back delimiter
		List<String> argList = new ArrayList<String>(Arrays.asList(commandline.split("\\s+")));
		for (int i = 0; i < argList.size(); i++) {
			argList.set(i, argList.get(i).replaceAll(dilimiter1, " ").replaceAll(dilimiter2, " "));
		}
		argList.remove(0);

		if (argList.size() > 0) {
			argList.remove(0);
			return argList.toArray(new String[0]);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sg.edu.nus.comp.cs4218.IShell#parse(java.lang.String)
	 * 
	 * Check for syntax and construct tool with the arguments
	 */
	@Override
	public ITool parse(String commandline) {
		if (commandline.contains("|")) {
			return new PipingTool(commandline.split("|"));
		} else {
			commandline = commandline.trim();
			String[] cmdSplit = commandline.split("\\s+");
			if (commandline.length() > 0 && cmdSplit.length > 0) {

				String cmd = cmdSplit[0].toLowerCase(); // This guarantee valid
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
		}
		return null;
	}

	@Override
	public Runnable execute(final ITool tool) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// get current path
				File f = new File(System.getProperty("user.dir"));
				// execute command
				String returnedValue = tool.execute(f, "");
				// print if has output
				if (returnedValue != null && returnedValue.trim().length() > 0) {
					System.out.println(returnedValue);
				}
			}
		};

		return runnable;
	}

	@Override
	public void stop(Runnable toolExecution) {

	}

	/**
	 * Do Forever 1. Wait for a user input 2. Parse the user input. Separate the
	 * command and its arguments 3. Create a new thread to execute the command
	 * 4. Execute the command and its arguments on the newly created thread.
	 * Exit with the status code of the executed command 5. In the shell, wait
	 * for the thread to complete execution 6. Report the exit status of the
	 * command to the user
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		// Input scanner
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		Scanner scanner = new Scanner(in);

		// Create Shell object
		Shell shell = new Shell();

		// while (true) {
		// 1. Wait for a user input
		System.out.print("$: ");
		// try {
		// 2. Parse the user input
		String input = scanner.nextLine();
		ITool itool = shell.parse(input);
		if (itool != null) {
			// 3. Create a new thread to execute the command
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			Runnable toolExecution = shell.execute(itool);
			// 4. Execute the command on the newly created thread.
			Future<?> future = executorService.submit(toolExecution);
			// 5. In the shell, wait for the thread to complete
			while (true) {
				try {
					if (in.ready() && in.readLine().equals("c")) {
						break;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (future.isCancelled() || future.isDone()) {
					break;
				}
			}
			executorService.shutdownNow();
			// 6. Report the exit status
			System.out.println("   status code: " + itool.getStatusCode());
		} else {
			System.out.println("   cmd: " + input + " not recognized.");
		}
		// } catch (Exception e) {
		// System.out.println("Exception:\n" + e.getMessage());
		// }
	}
	// }

}
