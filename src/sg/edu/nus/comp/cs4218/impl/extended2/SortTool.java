package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/*
 * 
 * sort : sort lines of text file
 *
 * Command Format - sort [OPTIONS] [FILE]
 *	FILE - Name of the file
 *	OPTIONS
 *		-c : Check whether the given file is already sorted, if it is not all sorted, print a
 *           diagnostic containing the first line that is out of order
 *	    -help : Brief information about supported options
 */

public class SortTool extends ATool implements ISortTool {

	private static final String IN_ORDER = "In order.";
	private final List<String>	argList;
	private final List<String>	inputList;

	/**
	 * constructor for sort tool
	 * @param arguments user defined 
	 */
	public SortTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
	}
	
	/**
	 * @param workingDir current working directory for relative file browsing
	 * @param stdin standard input received from pipe
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// check for argument number
		if (args == null || args.length < 1) {
			if (stdin == null)
				return "Invalid: No arguments and no standard input.";
			inputList.add("-");
		}

		boolean inputFlag = false;
		// split arguments and inputs
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length() > 1) {
				if (inputFlag)
					return "Invalid: option found after input argument";
				argList.add(args[i]);
			} else if (args[i].equals("-")) {
				if (stdin == null)
					return "Invalid: no stdin";
				inputList.add("-");
				inputFlag = true;
			} else if (args[i].trim().length() > 0) {
				inputList.add(args[i]);
				inputFlag = true;
			}
		}
		// help always get print first
		if (argList.contains("-help")) {
			return getHelp();
		}
		// help always get print first
		if (inputList.size() == 0) {
			inputList.add("-");
		}

		// note for flags
		int inputFlag2 = 0;
		String input = "";
		for (int i = 0; i < inputList.size(); i++) {
			if (inputList.get(i).equals("-") && inputFlag2 == 0) {
				input += stdin + "\n";
				inputFlag2++;
			} else {
				String tempInput = readFile(workingDir, inputList.get(i));
				if (tempInput == null) {
					return "sort: open failed: " + inputList.get(0) + ": No such file or directory.";
				}
				input += tempInput + "\n";
			}
		}
		input = (input.length() > 1) ? input.substring(0, input.length() - 1) : input;
		return processOptionC(input);
	}

	/**
	 * 
	 * @param input processed user input based on options
	 * @return if args list contains -c then returns output after checking if it is sorted else sorts file normally an returns
	 */
	private String processOptionC(String input) {
		if (argList.contains("-c")) {
			return checkIfSorted(input);
		} else {
			return sortFile(input);
		}
	}

	/**
	 * @param input processed user input based on options
	 * @return sorted file contents
	 */
	@Override
	public String sortFile(String input) {
		String[] inputList = input.split("\n");
		List<String> sortedList = new ArrayList<String>(Arrays.asList(inputList));
		Collections.sort(sortedList);
		String result = "";
		for (int i = 0; i < sortedList.size(); i++) {
			result += sortedList.get(i);
			if (i != sortedList.size() - 1)
				result += "\n";
		}
		setStatusCode(0);
		return result;
	}

	/**
	 * @param input processed user input based on options
	 * @return status if it in order or not
	 */
	@Override
	public String checkIfSorted(String input) {
		String[] inputList = input.split("\n");
		String head = (inputList.length > 0) ? inputList[0] : "";
		for (int i = 0; i < inputList.length; i++) {
			if (head.compareTo(inputList[i]) > 0) {
				setStatusCode(0);
				return inputList[i];
			}
			head = inputList[i];
		}
		setStatusCode(0);
		return IN_ORDER;
	}

	/**
	 * @return help menu with all available options
	 */
	@Override
	public String getHelp() {
		setStatusCode(0);
		return "-c : Check whether the given file is already sorted, if it is not all sorted, print a\n"
				+ " diagnostic containing the first line that is out of order\n" + " -help : Brief information about supported options";
	}

	/**
	 * 
	 * @param workingDir current working directory 
	 * @param path path of the file to be read
	 * @return reads the contents of the file and returns the full text 
	 */
	private String readFile(File workingDir, String path) {
		File newFile = new File(path);
		if (!newFile.isAbsolute()) {
			newFile = new File(workingDir.getAbsolutePath() + File.separator + path);
		}

		String fullText = "";
		try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				fullText += sCurrentLine + "\n";
			}

			fullText = (fullText.length() > 1) ? fullText.substring(0, fullText.length() - 1) : fullText;
		} catch (IOException e) {
			return null;
		}
		return fullText;
	}
}
