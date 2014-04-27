package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/*
 * 
 * comm : Compares two sorted files line by line. With no options, produce three-column output. 
 * 		 Column one contains lines unique to FILE1, column two contains lines unique to FILE2, 
 * 		 and column three contains lines common to both files.
 *	
 *	Command Format - comm [OPTIONS] FILE1 FILE2
 *	FILE1 - Name of the file 1
 *	FILE2 - Name of the file 2
 *		-c : check that the input is correctly sorted
 *      -d : do not check that the input is correctly sorted
 *      -help : Brief information about supported options
 */

/**
 * Specifications: comm file1 file2 comm - file2 comm file2 - comm -c - file2
 * comm -d file1 - comm -c file1 file2 comm -d file1 file2 comm -help comm - -c
 * -help (help gets priority) This will not work-> comm - -
 * 
 */
public class CommTool extends ATool implements ICommTool {

	private static final String INVALID_COMMAND = "Invalid command";
	private static final String NOT_SORTED = "Not Sorted!";
    public boolean sortFlag = false;
	public String currentLine1 = "";
	public String currentLine2 = "";
	public String file1 = "";
	public String file2 = "";
	public boolean flag1 = false;
	public boolean flag2 = false;

	/**
	 * constructor for CommTool
	 * 
	 * @param arguments
	 *            arguments or options passed by user as input
	 */
	public CommTool(String[] arguments) {
		super(arguments);
	}

	/**
	 * Compares if the input1 is less than, equal to or greater than input3
	 * 
	 * @param input1
	 *            first string to be compared
	 * @param input2
	 *            second string to be compared
	 * @return -1, 0 or 1
	 */
	@Override
	public String compareFiles(String input1, String input2) {
		// TODO Auto-generated method stub
		return (input1.compareTo(input2) < 0 ? "-1"
				: (input1.compareTo(input2) == 0) ? "0" : "1");
	}

	/**
	 * This function is called when the comm command is used with -c option It
	 * checks if
	 * 
	 * @param input1
	 *            the string from file 1 to be compared to currentLine1
	 * @param input
	 *            2 the string from file 2 to be compared to currentLine2
	 * @return -1 or not sorted status depending on whether input 1 and input 2
	 *         are sorted in comparison with currentline1 and currentline2
	 */
	@Override
	public String compareFilesCheckSortStatus(String input1, String input2) {
		// TODO Auto-generated method stub
		if (input1 == null && input2 == null)
			return "-1";
		if (input1 == null) {
			if (input2.compareTo(currentLine2) >= 0)
				return "-1";
			else
				return NOT_SORTED;
		} else if (input2 == null) {
			if (input1.compareTo(currentLine1) >= 0)
				return "-1";
			else {
				return NOT_SORTED;
			}
		} else if (input1.compareTo(currentLine1) >= 0
				&& input2.compareTo(currentLine2) >= 0)
			return "-1";
		else {
			return NOT_SORTED;
		}
	}

	/**
	 * compares input1 with input 2
	 * 
	 * @param input1
	 *            first string to be compared
	 * @param input2
	 *            second string to be compared
	 * @return -1, 1 or 0
	 */
	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		// TODO Auto-generated method stub
		return compareFiles(input1, input2);
	}

	/**
	 * This function is called to view all the possible ways in which comm tool
	 * can be used
	 * 
	 * @param
	 * @return help menu
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		String result = "comm : Compares two sorted files line by line. With no options, produce three-column output."
				+ "\nColumn one contains lines unique to FILE1, column two contains lines unique to FILE2,"
				+ "\nand column three contains lines common to both files."
				+ "\n"
				+ "\nCommand Format - comm [OPTIONS] FILE1 FILE2"
				+ "\nFILE1 - Name of the file 1"
				+ "\nFILE2 - Name of the file 2"
				+ "\n-c : check that the FILE1 is correctly sorted (increasing order) and also FILE2"
				+ "\n if it is not correctly sorted, 'Not Sorted!' is shown"
				+ "\n-d : do not check that the FILE1 and FILE2 are correctly sorted"
				+ "\n-help : Brief information about supported options";
		setStatusCode(0);
		return result;
	}

	/**
	 * @param workingDir
	 *            current working directory
	 * @param stdin
	 *            input received from pipe
	 * @return the formatted output as expected by the original command input
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		String operation = parse();
		if (operation.equals(INVALID_COMMAND))
			return INVALID_COMMAND;

			if (operation.equalsIgnoreCase("help")) {
				return getHelp();
			} else if (stdin != null && !stdin.equals("")) {
				decodeParsedOperation(stdin, operation);
			} else if (operation.equalsIgnoreCase("d") && args.length == 3) {
				// do not care about sorting
				file1 = args[1];
				file2 = args[2];
			} else if (operation.equalsIgnoreCase("c") && args.length == 3) {
				sortFlag = true;
				file1 = args[1];
				file2 = args[2];
			} else if (args.length == 2 && operation.equals("")) {
				sortFlag = false;
				file1 = args[0];
				file2 = args[1];
			}
			List<String> file1Data = new ArrayList<String>();
			List<String> file2Data = new ArrayList<String>();
			file1Data = populateFileData(workingDir, stdin, file1, flag1,
					new File(file1));
			file2Data = populateFileData(workingDir, stdin, file2, flag2,
					new File(file2));
			if (isInvalidData(file1Data, file2Data)) {
				return INVALID_COMMAND;
			}
			return processExpectedOutput(file1Data, file2Data);
	}

	/**
	 * decode the parsed string and accordingly populate files and flag values
	 * 
	 * @param stdin
	 *            input received from pipe
	 * @param operation
	 *            varies depending on the command type- if it contains -c then
	 *            it is c:f1 or c:f2, if it contains -d, it is d:f1 or d:f2,
	 *            otherwise f1 or f2
	 */
	public void decodeParsedOperation(String stdin, String operation) {
		if (args.length == 2) {
			if (operation.equals("f1")) {
				flag1 = true;
				file1 = stdin;
				file2 = args[1];
			}
			if (operation.equals("f2")) {
				flag2 = true;
				file2 = stdin;
				file1 = args[0];
			}
		}
		if (args.length == 3) {
			if (operation.equals("c:f2")) {
				flag2 = true;
				file2 = stdin;
				file1 = args[1];
				sortFlag = true;
			}
			if (operation.equals("c:f1")) {
				flag1 = true;
				file1 = stdin;
				file2 = args[2];
				sortFlag = true;
			}
			if (operation.equals("d:f2")) {
				flag2 = true;
				file2 = stdin;
				file1 = args[1];
				sortFlag = false;
			}
			if (operation.equals("d:f1")) {
				flag1 = true;
				file1 = stdin;
				file2 = args[2];
				sortFlag = false;
			}
		}
	}

	/**
	 * check for null or empty values
	 * 
	 * @param file1Data content of file 1
	 * @param file2Data content of file 2 
	 * @return false if file1data or file2data is null or empty 
	 */
	private boolean isInvalidData(List<String> file1Data, List<String> file2Data) {
		return file1Data == null || file1Data.size() == 0 || file2Data == null
				|| file2Data.size() == 0;
	}

	/**
	 * populate files with the right data
	 * 
	 * @param workingDir current working directory
	 * @param stdin 
	 * @param file1
	 * @param flag1
	 * @param f1
	 * @return 
	 */
	private List<String> populateFileData(File workingDir, String stdin,
			String file1, boolean flag1, File f1) {
		List<String> file1Data;
		if (flag1) {
			file1Data = Arrays.asList(stdin.split("\\r?\\n"));
		} else {
			file1Data = readFile(f1.isAbsolute() ? f1 : new File(workingDir,
					file1));
		}
		return file1Data;
	}

	/**
	 * process data according to comparison results
	 * 
	 * @param file1Data contents of file1
	 * @param file2Data contents of file2 d
	 * @return expected output depending on the options passed in input
	 */
	private String processExpectedOutput(List<String> file1Data,
			List<String> file2Data) {
		String line1, line2;
		String expectedOutput = "";
		int i = 0, j = 0;
		while (i < file1Data.size() || j < file2Data.size()) {
			if (i >= file1Data.size()) {
				line2 = file2Data.get(j);
				if (sortFlag
						&& !compareFilesCheckSortStatus(null, line2).equals(
								"-1")) {
					expectedOutput += NOT_SORTED;
					break;
				}
				currentLine2 = line2;
				expectedOutput += "\t\t" + line2;
				j++;
			} else if (j >= file2Data.size()) {
				line1 = file1Data.get(i);
				if (sortFlag
						&& !compareFilesCheckSortStatus(line1, null).equals(
								"-1")) {
					expectedOutput += NOT_SORTED;
					break;
				}
				currentLine1 = line1;
				expectedOutput += line1;
				i++;
			} else {
				line1 = file1Data.get(i);
				line2 = file2Data.get(j);
				if (sortFlag) {
					if (!compareFilesCheckSortStatus(line1, line2).equals("-1")) {
						expectedOutput += NOT_SORTED;
						setStatusCode(1);
						break;
					}
					currentLine1 = line1;
					currentLine2 = line2;
				}
				expectedOutput = formatOutput(line1, line2, expectedOutput);
				i++;
				j++;
			}
			expectedOutput += "\n";
		}
		expectedOutput = removeExtraNewLine(expectedOutput);
		setStatusCode(0);
		return expectedOutput;
	}

	/**
	 * removes the last extrea new line from expected output
	 * 
	 * @param expectedOutput the current output with or without extra new line
	 * @return output after removing last line
	 */
	private String removeExtraNewLine(String expectedOutput) {
		if (expectedOutput.endsWith("\n"))
			expectedOutput = expectedOutput.substring(0,
					expectedOutput.length() - 1);
		return expectedOutput;
	}

	/**
	 * formats the console output depending on comparison results
	 * 
	 * @param line1 current line from file 1
	 * @param line2 current line from file 2
	 * @param expectedOutput formatted output depending on the comparison of the 2 lines
	 * @return
	 */
	private String formatOutput(String line1, String line2,
			String expectedOutput) {
		int comparison = Integer.parseInt(compareFiles(line1, line2));
		if (comparison == 0)
			expectedOutput += "\t\t\t\t" + line1;
		else if (comparison < 0)
			expectedOutput += line1 + "\n\t\t" + line2;
		else
			expectedOutput += "\t\t" + line2 + "\n" + line1;
		return expectedOutput;
	}

	/**
	 * parses command by checking if command contains help which gets priority,
	 * else checks number of arguments
	 * 
	 * @return the option which appears first in the user input command
	 */
	public String parse() {
		String parsed = "";
		int count = 0;
		if(args==null) {
			return 	INVALID_COMMAND;
		}
		int i = args.length - 1;
		while (i >= 0) {
			if (args[i].length() > 1 && args[i].startsWith("-")) {
				// help gets priority // if not help, the first one gets
				// priority
				String option = args[i].substring(1);
				if (option.equalsIgnoreCase("help")) {
					return option;
				}
				count++;
				parsed = option;
			}

			i--;
		}
		parsed = buildParseCode(parsed, count);
		return parsed;
	}

	/**
	 * Builds the parse code according to user input. Appends f1 if the arg is
	 * treated as f1, f2 if treated as file2
	 * 
	 * @param parseCode string formed based on how args are interpreted 
	 * @param count number of - in the command
	 * @return well formed parse code depending on number and type of options passed by user
	 */
	public String buildParseCode(String parseCode, int count) {
		if (args.length > 3 || args.length < 2)
			return INVALID_COMMAND;

		if (count > 1) { // cannot have comm - - or comm - -c -----
			return INVALID_COMMAND;
		}

		if (count == 0 && args.length > 0 && args[0].equals("-")) {
			parseCode = "f1";
		}

		if (count == 0 && args.length > 1 && args[1].equals("-")) {
			parseCode = "f2";
		}

		if (count == 1 && args.length > 1 && args[1].equals("-")) {
			parseCode += ":f1";
		}

		if (count == 1 && args.length > 2 && args[2].equals("-")) {
			parseCode += ":f2";
		}
		return parseCode;
	}

	/**
	 * reads the file and returns the content
	 * 
	 * @param f file to be read
	 * @return contents of the file
	 */
	public List<String> readFile(File f) {
		List<String> expectedOutput = new ArrayList<String>();
		if (f.isFile()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					expectedOutput.add(line);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return expectedOutput;
		}
		return null;
	}
}
