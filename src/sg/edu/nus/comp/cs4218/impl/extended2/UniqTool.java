package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/*
 * 
 * uniq : Writes the unique lines in the given input. The input need not be sorted, 
 *      but repeated input lines are detected only if they are adjacent.
 *
 * Command Format - uniq [OPTIONS] [FILE]
 * FILE - Name of the file, when no file is present (denoted by "-") use standard input
 * OPTIONS
 * 		-f NUM : Skips NUM fields on each line before checking for uniqueness. 
 * 			     Use a null string for comparison if a line has fewer than n fields. 
 *               Fields are sequences of non-space non-tab characters that are separated 
 *                  from each other by at least one space or tab.
 *      -i : Ignore differences in case when comparing lines.
 *      -help : Brief information about supported options
 */

public class UniqTool extends ATool implements IUniqTool {

	private static final String INVALID_COMMAND = "Invalid command";
	public String currentLine = "";
	public String previousLine = "";
	private int inputFlag = 0; // 1 for file, 2 for
								// string
	private int skipNum = 0; // 1 for file, 2 for
								// string
	private final List<String> argList;
	private final List<String> inputList;
	private final List<String> uniqueList;

	/**
	 * constructor for Uniq tool
	 * 
	 * @param arguments
	 */
	public UniqTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
		uniqueList = new ArrayList<String>();
	}

	/**
	 * @param workingDir
	 *            current working directory
	 * @param stdin
	 *            input received from pipe
	 * @return expected string with unique elements
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		boolean inputFlag2 = false;
		// split arguments and inputs
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-") && args[i].length() > 1) {
				if (inputFlag2)
					return "Invalid: option found after input argument";
				argList.add(args[i]);
				if (args[i].equals("-f")) {
					try {
						skipNum = Integer.parseInt(args[++i]);
					} catch (Exception e) {
						return INVALID_COMMAND;
					}
				}
			} else if (args[i].equals("-")) {
				if (inputFlag != 0) {
					return null;
				}
				inputFlag = 2;
				inputFlag2 = true;
			} else if (args[i].trim().length() > 0) {
				if (inputFlag != 0) {
					return null;
				}
				inputList.add(args[i]);
				inputFlag = 1;
				inputFlag2 = true;
			}
		}
		// help always get print first
		if (argList.contains("-help")) {
			return getHelp();
		}
		// note for flags
		if (inputFlag == 1 && inputList.size() > 0) {
			if (readAndProcessFile(workingDir, inputList.get(0)) == null) {
				return INVALID_COMMAND;
			}
		} else if (inputFlag == 2 || inputList.size() == 0) {
			if (stdin == null)
				return INVALID_COMMAND;
			readAndProcessString(stdin);
		} else {
			return INVALID_COMMAND;
		}
		return buildResultFromUniqList();
	}

	/**
	 * This function builds result from the uniq list items. This is the final
	 * output to the user
	 * 
	 * @return
	 */
	private String buildResultFromUniqList() {
		String result = "";
		for (int i = 0; i < uniqueList.size(); i++) {
			result += uniqueList.get(i);
			if (i != uniqueList.size() - 1)
				result += "\n";
		}
		setStatusCode(0);
		return result;
	}

	/**
	 * 
	 * @param workingDir
	 *            current working directory
	 * @param path
	 *            file's path
	 * @return process the input file depending on options like -f and -i
	 */
	private String readAndProcessFile(File workingDir, String path) {
		File newFile = new File(path);
		if (!newFile.isAbsolute()) {
			newFile = new File(workingDir.getAbsolutePath() + File.separator
					+ path);
		}

		String fullText = "";
		try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {

				if (argList.contains("-f")) {
					getUniqueSkipNum(skipNum, !argList.contains("-i"),
							sCurrentLine);
				} else {
					getUnique(!argList.contains("-i"), sCurrentLine);
				}
			}

			br.close();
		} catch (IOException e) {
			return null;
		}
		return fullText;
	}

	/**
	 * This function reads input from input list, process it according to
	 * options and returns the correct full text
	 * 
	 * @param input
	 *            content of the file which is checked for
	 * @return full text formed after processing the input
	 */
	private String readAndProcessString(String input) {
		String fullText = "";
		String[] inputList = input.split("\n");
		for (int i = 0; i < inputList.length; i++) {
			if (argList.contains("-f")) {
				getUniqueSkipNum(skipNum, !argList.contains("-i"), inputList[i]);
			} else {
				getUnique(!argList.contains("-i"), inputList[i]);
			}
		}
		return fullText;
	}

	/**
	 * 
	 * @param string1 first string to be compared
	 * @param string2 second string to be compared
	 * @param num this is an integer that checks for the array size limit 
	 * @return compares string1 and string 2 and returns the boolean result
	 */
	private boolean compareStringSkip(String string1, String string2, int count) {
		String[] array1 = string1.split("\\s+");
		String[] array2 = string2.split("\\s+");
		if (array1.length != array2.length) {
			return false;
		}
		if (array1.length <= count) {
			return false;
		}
		for (int i = count; i < array2.length; i++) {
			if (!array2[i].equals(array1[i]))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param str1 string1 first string to be compared
	 * @param str2 string2 second string to be compared
	 * @param num this is an integer that checks for the array size limit 
	 * @return compares string1 and string 2 ignoring their case and returns the boolean result
	 */
	private boolean compareStringIgnoreCaseSkip(String str1, String str2,
			int num) {
		String[] array1 = str1.split("\\s+");
		String[] array2 = str2.split("\\s+");
		if (array1.length != array2.length) {
			return false;
		}
		if (array1.length <= num) {
			return false;
		}
		for (int i = num; i < array2.length; i++) {
			if (!array2[i].equalsIgnoreCase(array1[i]))
				return false;
		}
		return true;
	}

	/**
	 * @param checkCase the value returned decides if the case needs to be checked or not
	 * @param input the current line which is processed 
	 * This functions eliminates repeated lines and outputs the correct unique result
	 */
	@Override
	public String getUnique(boolean checkCase, String input) {
		if (checkCase) {
			if (!previousLine.equals(input)) {
				previousLine = input;
				uniqueList.add(input);
			}
			if (!currentLine.equals(input)) {
				currentLine += "\n" + input;
			}
		} else {
			if (!previousLine.equalsIgnoreCase(input)) {
				previousLine = input;
				uniqueList.add(input);
			}
			if (!currentLine.equalsIgnoreCase(input)) {
				currentLine += "\n" + input;
			}
		}
		return currentLine;
	}

	/**
	 * @param num this is an integer that checks for the array size limit 
	 * @param checkCase the value returned decides if the case needs to be checked or not
	 * @param input the current line which is processed 
	 * @return this function returns appends the input string to the current line if it is not the same as the current line to avoid duplicates
	 */
	@Override
	public String getUniqueSkipNum(int num, boolean checkCase, String input) {
		num = (num < 0) ? 0 : num;
		if (checkCase) {
			if (!compareStringSkip(previousLine, input, num)) {
				previousLine = input;
				uniqueList.add(input);
			}
			if (!compareStringSkip(currentLine, input, num)) {
				currentLine += "\n" + input;
			}
		} else {
			if (!compareStringIgnoreCaseSkip(previousLine, input, num)) {
				previousLine = input;
				uniqueList.add(input);
			}
			if (!compareStringIgnoreCaseSkip(currentLine, input, num)) {
				currentLine += "\n" + input;
			}
		}
		return currentLine;
	}

	/**
	 * @return displays the entire menu for uniq tool
	 */
	@Override
	public String getHelp() {
		setStatusCode(0);
		return "uniq : Writes the unique lines in the given input. The input need not be sorted, but repeated input lines are detected only if they are adjacent."
				+ "\nCommand Format - uniq [OPTIONS] [FILE]"
				+ "\nFILE - Name of the file, when no file is present (denoted by '-') use standard input"
				+ "\nOPTIONS"
				+ "\n-f NUM : Skips NUM fields on each line before checking for uniqueness. Use a null"
				+ "\nstring for comparison if a line has fewer than n fields. Fields are sequences of"
				+ "\nnon-space non-tab characters that are separated from each other by at least one space or tab."
				+ "\nNUM is a single digit positive integer"
				+ "\n-i : Ignore differences in case when comparing lines."
				+ "\n-help : Brief information about supported options";
	}
}
