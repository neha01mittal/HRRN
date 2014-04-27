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
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

/**
 * The Shell is used to interpret and execute user's commands. Following
 * sequence explains how a basic shell can be implemented in Java
 * 
 * DIFFERENT OPTIONS commands $ invalid command - prints error message $ NOTE:
 * 
 * @usage
 * @options $: ls - lists current directory $: copy path1 path2 - copies
 *          contents of file/folder from path1 to path2 $: move path1 path2 -
 *          moves file/folder from path1 to path2 $: delete path1 - deletes
 *          file/folder $: pwd - displays present working directory $: echo
 *          input - displays input $: cat file - prints contents of
 *          file/standard input $: grep options "pattern" file - searches for a
 *          pattern in file(s) $: p1 | p2 - send stdout of p1 to p2 to execute
 * @note The input arguments or path can be relative or absolute, in quotes or
 *       without quotes, with backslash or front slash, with escaped space. But
 *       other escaped like \" or \' characters are not yet handled. But you can
 *       still escape " by surrounding it with ", vise versa. The termination
 *       function is either ctrl-z to terminate a running tool, the char has to
 *       be input with enter key hit. The shell will try it"s best to stop the
 *       running thread, if it"s really not able to stop the thread, the program
 *       will stop for safety. User are not allowed to use "~" to denote home
 *       directory.
 * @success It will print the returning string, or nothing if returned null, or
 *          a blank line if returned empty string.
 * @exceptions IOException for reading user input Tool returned status code not
 *             0, if so shell will print out the error code.
 * 
 */
public class Shell implements IShell {

	private ITool			currentTool;
	private Scanner			scanner;
	private Future<?>		future;
	private BufferedReader	in;
	private ExecutorService	executorService;
	public boolean			executionFlag	= true;

	public void start() {
		// Input scanner
		in = new BufferedReader(new InputStreamReader(System.in));
		scanner = new Scanner(in);
		// 1. Wait for a user input
		System.out.print(System.getProperty("user.dir") + " $: ");
		while (executionFlag && scanner.hasNext()) {
			// 2. Parse the user input
			String input = scanner.nextLine();
			executeInput(input, true);
			System.out.print(System.getProperty("user.dir") + " $: ");
		}
	}

	// returm error message or null
	public void executeInput(String input, boolean isInteruptable) {
		ITool itool = parse(input);
		if (itool != null) {
			// 3. Create a new thread to execute the command
			executorService = Executors.newSingleThreadExecutor();
			// 4. Execute the command on the newly created thread.
			currentTool = itool;
			Runnable toolExecution = execute(itool);
			// 5. In the shell, wait for the thread to complete
			while (true) {
				if (isInteruptable) {
					try {
						if (in.ready() && in.readLine().equals("ctrl-z")) {
							if (!(future.isDone() || future.isCancelled())) {
								boolean cancellable = future.cancel(true);
								if (!cancellable) {
									System.err.println("[" + currentTool.getStatusCode() + "]+  Cannot stop thread, thus quit!");
									System.exit(1);
								} else {
									System.err.println("[" + currentTool.getStatusCode() + "]+  Stopped                 "
											+ currentTool.getClass().getSimpleName());
								}
							}
							break;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				if (future.isCancelled() || future.isDone()) {
					break;
				}
			}
			executorService.shutdownNow();
		} else {
			System.err.println("   cmd: " + input + " not recognized.");
		}
	}

	@Override
	public Runnable execute(final ITool tool) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// get current path
				File f = new File(System.getProperty("user.dir"));
				// execute command
				String returnedValue = tool.execute(f, null);
				// print if has output
				if (returnedValue != null && returnedValue.trim().length() > 0) {
					if (tool.getStatusCode() == 0) {
						System.out.println(returnedValue);
					}
					// else {
					// System.err.println(returnedValue);
					// }
				} else if (returnedValue != null) {
					System.out.println();
				}
			}
		};

		future = executorService.submit(runnable);
		return runnable;
	}

	@Override
	public void stop(Runnable toolExecution) {

	}

	//
	// Test interfaces and testable functions
	//
	public Future<?> executeTest(final ITool tool) {

		executorService = Executors.newSingleThreadExecutor();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// get current path
				File f = new File(System.getProperty("user.dir"));
				// execute command
				tool.execute(f, null);
			}
		};
		return future = executorService.submit(runnable);
	}

	public boolean stopTest(Future<?> future, ITool currentTool) {

		if (!(future.isDone() || future.isCancelled())) {
			boolean cancellable = future.cancel(true);
			// if (!cancellable) {
			// System.exit(1);
			// }
			executorService.shutdownNow();
		}
		return true;
	}

	public static String[] getArgsArray(String commandline) {
		String dilimiter1 = "::surrounded-space::";
		String dilimiter2 = "::escaped-space::";

		// Step 1. find the escape space between quotes
		Pattern regex = Pattern.compile("[^'\"]*[^\\\\]\"([^\"]*)\"|[^'\"]*[^\\\\]'([^\']*)\'");
		Matcher regexMatcher = regex.matcher(commandline);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				String temp = regexMatcher.group(1);
				String replaced = temp.replaceAll("\\s", dilimiter1);
				commandline = commandline.replace("\"" + temp + "\"", "\"" + replaced + "\"");
			}
			if (regexMatcher.group(2) != null) {
				String temp = regexMatcher.group(2);
				String replaced = temp.replaceAll("\\s", dilimiter1);
				commandline = commandline.replace("'" + temp + "'", "'" + replaced + "'");
			}
		}

		// Step 2. remove all escape space
		commandline = commandline.replaceAll("\\\\\\s", dilimiter2);
		// System.out.println(commandline);

		// Step 3. remove the first one and switch back delimiter
		List<String> argList = new ArrayList<String>(Arrays.asList(commandline.split("\\s+")));
		for (int i = 0; i < argList.size(); i++) {
			String newArg = argList.get(i);
			newArg = " " + newArg;
			// System.out.println(i + ". " + argList.get(i));
			// Step 4. remove all the surround quotes
			regex = Pattern.compile("[^'\"]*[^\\\\](\"[^\"]*\")|[^'\"]*[^\\\\]('[^']*')");
			regexMatcher = regex.matcher(newArg);
			List<String> quoteList = new ArrayList<String>();
			while (regexMatcher.find()) {
				if (regexMatcher.group(1) != null) {
					String quoteText = regexMatcher.group(1);
					String newquoteText = quoteText.substring(1, quoteText.length() - 1);
					// System.out.println("group1: " + quoteText);
					// Step 5. remove all escape word in "" $ ` " \ <newline>
					Pattern regex2 = Pattern.compile("(\\\\[$\\\\\\`\"])+");
					Matcher regexMatcher2 = regex2.matcher(quoteText);
					while (regexMatcher2.find()) {
						if (regexMatcher2.group(1) != null) {
							String escapeChar = regexMatcher2.group(1);
							// System.out.println("group1: " + escapeChar);
							newquoteText = newquoteText.replace(escapeChar, escapeChar.substring(1));
							// System.out.println("group1: " + quoteText);
						}
					}
					newArg = newArg.replace(quoteText, "::quote-" + quoteList.size());
					quoteList.add(newquoteText);
				}
				if (regexMatcher.group(2) != null) {
					String quoteText = regexMatcher.group(2);
					// System.out.println("group2: " + quoteText);
					newArg = newArg.replace(quoteText, "::quote-" + quoteList.size());
					quoteList.add(quoteText.substring(1, quoteText.length() - 1));
				}
			}

			// System.out.println("No quote: " + newArg);
			// Step 6. remove all escape word
			// | & ; < > ( ) $ ` \ " ' <space> <tab> <newline> * ? [ # �� = %
			regex = Pattern.compile("(\\\\[;$`\"'*?\\[#��=%\\|\\&\\<\\>\\(\\)\\\\\\s])+");
			regexMatcher = regex.matcher(newArg);
			while (regexMatcher.find()) {
				if (regexMatcher.group(1) != null) {
					String temp = regexMatcher.group(1);
					// System.out.println("group1: " + temp);
					newArg = newArg.replace(temp, temp.substring(1));
					// System.out.println("group1: " + newArg);
				}
			}

			// Step 7. set the value back
			for (int j = 0; j < quoteList.size(); j++) {
				newArg = newArg.replace("::quote-" + j, quoteList.get(j));
			}
			newArg = newArg.trim().replace(dilimiter1, " ").replace(dilimiter2, " ");
			argList.set(i, newArg);
		}

		// System.out.println(argList.toString());
		argList.remove(0);
		return argList.toArray(new String[argList.size()]);
	}

	@Override
	public ITool parse(String commandline) {
		String dilimiter1 = "::surrounded-pipe::";
		String dilimiter2 = "::escaped-pipe::";

		// Step 1. find the escape pipe between quotes
		Pattern regex = Pattern.compile("[^'\"]*[^\\\\]\"([^\"]*)\"|[^'\"]*[^\\\\]'([^\']*)\'");
		Matcher regexMatcher = regex.matcher(commandline);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				String temp = regexMatcher.group(1);
				// System.out.println("group1: " + temp);
				String replaced = temp.replaceAll("\\|", dilimiter1);
				// System.out.println("group1: " + replaced);
				commandline = commandline.replace("\"" + temp + "\"", "\"" + replaced + "\"");
			}
			if (regexMatcher.group(2) != null) {
				String temp = regexMatcher.group(2);
				// System.out.println("group2: " + temp);
				String replaced = temp.replaceAll("\\|", dilimiter1);
				// System.out.println("group2: " + replaced);
				commandline = commandline.replace("'" + temp + "'", "'" + replaced + "'");
			}
		}

		// Step 2. remove all escape pipe
		commandline = commandline.replaceAll("\\\\\\|", dilimiter2);
		// System.out.println(commandline);

		if (commandline.contains("|")) {
			String[] args = commandline.split("\\|");
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].trim().replace(dilimiter1, "|").replace(dilimiter2, "|");
			}
			return new PipingTool(args);
		} else {
			commandline = commandline.trim();
			String[] cmdSplit = commandline.split("\\s+");
			if (commandline.length() > 0) {
				// This guarantee valid
				String cmd = cmdSplit[0].toLowerCase();
				// Now we need to construct arguments
				String[] args = getArgsArray(commandline);
				for (int i = 0; i < args.length; i++) {
					args[i] = args[i].replace(dilimiter1, "|").replace(dilimiter2, "|");
				}

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
						return new CommTool(args);
					case "cut":
						return new CutTool(args);
					case "sort":
						return new SortTool(args);
					case "paste":
						return new PasteTool(args);
					case "uniq":
						return new UniqTool(args);
					case "wc":
						return new WcTool(args);
					default:
						return null;
				}
			}
		}
		return null;
	}

	/**
	 * Do Forever 1. Wait for a user input 2. Parse the user input. Separate the
	 * command and its arguments 3. Create a new thread to execute the command
	 * 4. Execute the command and its arguments on the newly created thread.
	 * Exit with the status code of the executed command 5. In the shell, wait
	 * for the thread to complete execution 6. Report the exit status of the
	 * command to the user
	 */
	public static void main(String[] args) {

		// Create Shell object
		new Shell().start();
	}

}
