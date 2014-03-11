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

	private final List<String>	argList;
	private final List<String>	inputList;

	public SortTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// check for argument number
		if (args == null || args.length < 1) {
			return "No arguments and no standard input.";
		}

		// split arguments and inputs
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length() > 1) {
				argList.add(args[i]);
			} else if (args[i].equals("-")) {
				if (stdin == null)
					return "Invalid stdin";
				inputList.add("-");
			} else if (args[i].trim().length() > 0) {
				inputList.add(args[i]);
			}
		}

		// help always get print first
		if (argList.contains("-help")) {
			return getHelp();
		}

		// note for flags
		int inputFlag = 0;
		String input = "";
		for (int i = 0; i < inputList.size(); i++) {
			if (inputList.get(i).equals("-") && inputFlag == 0) {
				input += stdin;
				inputFlag++;
			} else {
				String tempInput = readFile(workingDir, inputList.get(i));
				if (tempInput == null) {
					return "sort: open failed: " + inputList.get(0) + ": No such file or directory.";
				}
				input += tempInput;
			}
		}

		if (argList.contains("-c")) {
			return checkIfSorted(input);
		} else {
			return sortFile(input);
		}
	}

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
		return "In order.";
	}

	@Override
	public String getHelp() {
		setStatusCode(0);
		return "-c : Check whether the given file is already sorted, if it is not all sorted, print a\n"
				+ " diagnostic containing the first line that is out of order\n" + " -help : Brief information about supported options";
	}

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
		} catch (IOException e) {
			return null;
		}
		fullText = (fullText.length() > 1) ? fullText.substring(0, fullText.length() - 1) : fullText;
		return fullText;
	}
}
