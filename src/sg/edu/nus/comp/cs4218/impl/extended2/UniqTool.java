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

	public String				currentLine		= "";
	public String				previousLine	= "";
	private int					inputFlag		= 0;	// 1 for file, 2 for
														// string
	private int					skipNum			= 0;	// 1 for file, 2 for
														// string
	private final List<String>	argList;
	private final List<String>	inputList;
	private final List<String>	uniqueList;

	public UniqTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
		uniqueList = new ArrayList<String>();
	}

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
						return "Invalid command";
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
				return "Invalid command";
			}
		} else if (inputFlag == 2 || inputList.size() == 0) {
			if (stdin == null)
				return "Invalid command";
			readAndProcessString(stdin);
		} else {
			return "Invalid command";
		}

		String result = "";
		for (int i = 0; i < uniqueList.size(); i++) {
			result += uniqueList.get(i);
			if (i != uniqueList.size() - 1)
				result += "\n";
		}
		setStatusCode(0);
		return result;
	}

	private String readAndProcessFile(File workingDir, String path) {
		File newFile = new File(path);
		if (!newFile.isAbsolute()) {
			newFile = new File(workingDir.getAbsolutePath() + File.separator + path);
		}

		String fullText = "";
		try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {

				if (argList.contains("-f")) {
					getUniqueSkipNum(skipNum, !argList.contains("-i"), sCurrentLine);
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

	private boolean compareStringSkip(String str1, String str2, int num) {
		String[] array1 = str1.split("\\s+");
		String[] array2 = str2.split("\\s+");
		if (array1.length != array2.length) {
			return false;
		}
		if (array1.length <= num) {
			return false;
		}
		for (int i = num; i < array2.length; i++) {
			if (!array2[i].equals(array1[i]))
				return false;
		}
		return true;
	}

	private boolean compareStringIgnoreCaseSkip(String str1, String str2, int num) {
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
