package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Specifications for Cut cut [options] [character_positions] file cut file:
 * prints all the file contents on the console cut -c 1,2-4,5 file : prints all
 * the characters at positions from each line. cut -d delim -f 1,2-4,5 file:
 * separates characters by delimiter and prints command1 | cut : The output of
 * command1 is treated as file contents to be used by cut command1 | cut
 * [options] : stdin used as file contents command1 | cut - : "-" is replaced by
 * file contents command1 | cut -c [character_positions] - command1 | cut -d ","
 * -f [character_positions] -
 * 
 */
public class CutTool extends ATool implements ICutTool {

	private static final String ERROR_NO_SUCH_FILE_OR_DIRECTORY = "Error: No such file or directory\n";
	private static final String NO_ARGUMENTS_AND_NO_STANDARD_INPUT = "No arguments and no standard input.";
	private static final String INVALID_COMMAND = "Invalid command";
	private final List<String> argList;
	private final List<String> inputList;
	private String delim;
	private List<Integer> characterList = new ArrayList<Integer>();

	/**
	 * Constructor for cut tool
	 * 
	 * @param arguments
	 */
	public CutTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		setStatusCode(0);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
		delim = null;
	}

	/**
	 * @param list Character list
	 * @param input File content 
	 * @return cuts specified characters with "" as the delimiter
	 */
	@Override
	public String cutSpecfiedCharacters(String list, String input) {

		return cutSpecifiedCharactersUseDelimiter(list, "", input);

	}

	/**
	 * @param list Character list
	 * @param delim File content
	 * @param inputs
	 *            builds the cut string depending on the delimiter locations and
	 *            returns the string if the input is valid else returns invalid
	 *            command
	 */
	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {
		// TODO Auto-generated method stub
		String escapeDelim = "";
		for (int i = 0; i < delim.length(); i++) {
			if (delim.substring(i, i + 1).matches("[^a-zA-Z0-9 ]")) {
				escapeDelim = escapeDelim + "\\" + delim.substring(i, i + 1);
			} else {
				escapeDelim = escapeDelim + delim.substring(i, i + 1);
			}
		}
		String cutString = "";
		List<String> tokenList = Arrays.asList(list.split(","));
		Collections.sort(tokenList);
		try {
			for (int x = 0; x < tokenList.size(); x++) {

				if (tokenList.get(x).contains("-")) {
					populateCharacterList(tokenList, x);
					if (getStatusCode() == 1)
						return INVALID_COMMAND;
				} else {
					modifyCharacterList(tokenList, x);
					if (getStatusCode() == 1)
						return INVALID_COMMAND;
				}
			}
		} catch (Exception e) {
			setStatusCode(1);
			return INVALID_COMMAND;
		}

		Collections.sort(characterList);
		return buildCutString(delim, input, escapeDelim, cutString,
				characterList);
	}

	/**
	 * adds tokens to the character list if the list is not empty
	 * 
	 * @param tokenList List of characters to be displayed
	 * @param x Number corresponding to the character in the list
	 */
	private void modifyCharacterList(List<String> tokenList, int x) {
		int num = Integer.parseInt(tokenList.get(x));
		if (num > 0) {
			if (!(characterList.contains(num - 1)))
				characterList.add(num - 1);
		} else {
			setStatusCode(1);
		}
	}

	/**
	 * populates character list with token list values
	 * 
	 * @param tokenList Adds numbers in the case of a range and removes duplicates
	 * @param x Number corresponding to the character in the list
	 */
	private void populateCharacterList(List<String> tokenList, int x) {
		List<String> firstList = Arrays.asList(tokenList.get(x).split("-"));
		if (firstList.size() == 2) {
			int start = Integer.parseInt(firstList.get(0));
			int end = Integer.parseInt(firstList.get(1));
			if (start >= 1 & end >= 1) {
				for (int num = start; num <= end; num++) {
					if (!(characterList.contains(num - 1)))
						characterList.add(num - 1);
				}
			} else {
				setStatusCode(1);
			}

		} else {
			setStatusCode(1);
		}
	}

	/**
	 * builds the final string to be returned to the console based on the
	 * delimiter values and character list
	 * 
	 * @param delim Delimiter
	 * @param input File content
	 * @param escapeDelim Delimiter after it has been escaped
	 * @param cutString Content to be displayed
	 * @param characterList Character list
	 * @return Content to be displayed
	 */
	private String buildCutString(String delim, String input,
			String escapeDelim, String cutString, List<Integer> characterList) {
		List<String> splitByNewLineList = Arrays.asList(input.split("\n"));
		if (escapeDelim == "") {
			for (String line : splitByNewLineList) {
				for (int character : characterList)
					if (line.length() > character)
						cutString = cutString
								+ line.substring(character, character + 1);
				cutString = cutString + "\n";
			}

		} else {
			for (String line : splitByNewLineList) {
				List<String> inputLineList = Arrays.asList(line
						.split(escapeDelim));
				cutString = buildCutString(delim, cutString, characterList,
						inputLineList);
			}
		}
		cutString = removeExtraNewLine(cutString);
		return cutString;
	}

	/**
	 * Overloaded function which builds cut string based on splitting done by
	 * new line character
	 * 
	 * @param delim Delimiter
	 * @param cutString Content to be displayed
	 * @param characterList List of character positions
	 * @param inputLineList Lines in the files
	 * @return Content to be displayed
	 */
	private String buildCutString(String delim, String cutString,
			List<Integer> characterList, List<String> inputLineList) {
		for (int character : characterList) {
			if (inputLineList.size() > character)
				if (character == characterList.get(characterList.size() - 1))
					cutString = cutString + inputLineList.get(character);
				else
					cutString = cutString + inputLineList.get(character)
							+ delim;
		}
		cutString = removeLastDelimiter(delim, cutString);
		cutString = cutString + "\n";
		return cutString;
	}

	/**
	 * removes last delimiter if any present
	 * 
	 * @param delim Delimiter
	 * @param cutString Content to be displayed
	 * @return Content to be displayed
	 */
	private String removeLastDelimiter(String delim, String cutString) {
		if (cutString.endsWith(delim)) {
			int size = cutString.length();
			cutString = cutString.substring(0, size - 1);
		}
		return cutString;
	}

	/**
	 * removes the extra new line at the end
	 * 
	 * @param cutString Content to be displayed
	 * @return Updated content to be displayed
	 */
	private String removeExtraNewLine(String cutString) {
		if (cutString.endsWith("\n")) {
			int size = cutString.length();
			cutString = cutString.substring(0, size - 1);
		}
		return cutString;
	}

	/**
	 * outputs a menu with all the options available
	 * 
	 * @return Content to be displayed
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		String helpString = "cut : prints a substring that is specified in a certain range"
				+ "\nCommand Format - cut [OPTIONS] [FILE]"
				+ "\nFILE - Name of the file, when no file is present (denoted by '-') use standard input OPTIONS"
				+ "\n-c LIST: Use LIST as the list of characters to cut out. E.g 'cut -c 1-5,10,18-30'"
				+ "\nspecifies characters 1 through 5, 10 and 18 through 30."
				+ "\n-d DELIM -f FIELD: Use DELIM as the field-separator character instead of the TAB character."
				+ "\nFIELD is similar to LIST except FIELD is used as the list of string to cut out after delimited by DELIM."
				+ "\nThe way to define FIELD is the same as LIST."
				+ "\n-help : Brief information about supported options";
		return helpString;
	}

	/**
	 * @param workingDir The working directory on which the tool will operate
	 * @param stdin
	 *            returns the final formatted string depending on the user
	 *            options
	 * @return stdout Content to be displayed
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		String fileContents = "";
		String finalString = "";
		checkForArgsAndStdin(stdin);
		if (getStatusCode() == 1)
			return NO_ARGUMENTS_AND_NO_STANDARD_INPUT;
		// split arguments and inputs
		int x = 0;
		int found = -1;
		for (String arg : args) {
			found = buildListBasedOnOptions(x, found, arg);
			if (getStatusCode() == 1)
				return INVALID_COMMAND;
			x++;
		}
		for (x = 0; x < argList.size(); x++) {
			String arg = argList.get(x);
			if (arg.equals("-d")) {
				delim = inputList.get(x);
			} else if (arg.equals("-help")) {
				setStatusCode(0);
				return getHelp();
			}
		}
		if (isInvalidOption()) {
			setStatusCode(1);
			return INVALID_COMMAND;
		}
		if (isIncorrectOptions()) {
			setStatusCode(1);
			return INVALID_COMMAND;
		}
		// Filename is the last one
		fileContents = addFileContent(workingDir, stdin, fileContents);
		if (getStatusCode() == 1) {
			return INVALID_COMMAND;
		}
		return finalFormattedString(stdin, fileContents, finalString, found);
	}

	/**
	 * creates the final string that is sent to stdout
	 * 
	 * @param stdin can be null. Otherwise, it has File contents
	 * @param fileContents File contents
	 * @param characterList List of character positions
	 * @param found Boolean if '-' is found or not
	 * @return Content to be displayed
	 */
	private String finalFormattedString(String stdin, String fileContents,
			String finalString, int found) {
		int x;
		for (x = 0; x < argList.size(); x++) {

			if (argList.get(x).equals("-c")) {
				String result = cutSpecfiedCharacters(inputList.get(x),
						fileContents);
				return contructStringIfValid(finalString, stdin, x, found,
						result);

			} else if (argList.get(x).equals("-f")) {
				return processCharacterList(finalString, stdin, fileContents,
						x, found);
			}
		}
		return finalString;
	}

	/**
	 * checks if the argument list contains incorrect options
	 * 
	 * @return Truth value if invalid options were used ( -d without -f)
	 */
	private boolean isIncorrectOptions() {
		return argList.contains("-d") && (!(argList.contains("-f")));
	}

	/**
	 * checks if the argument list contains invalid options
	 * 
	 * @return Truth value if invalid options were used
	 */
	private boolean isInvalidOption() {
		return argList.contains("-c")
				&& (argList.contains("-f") || argList.contains("-d"));
	}

	/**
	 * checks if args or stdin is null
	 * 
	 * @param stdin can be null. It is used with args or used instead of args if args is missing
	 */
	private void checkForArgsAndStdin(String stdin) {
		if (args == null || args.length == 0) {
			if (stdin != null && stdin != "") {
				populateArguments(stdin);
			} else {
				setStatusCode(1);
			}
		}
	}

	/**
	 * constructs string if it is valid
	 * 
	 * @param output 
	 * @param stdin Standard input
	 * @param x Position of "-"
	 * @param found "-" if found or not 
	 * @param result 
	 * @return Output string
	 */
	private String contructStringIfValid(String output, String stdin, int x,
			int found, String result) {
		if (result != INVALID_COMMAND)
			setStatusCode(0);
		if (stdin != null && stdin != "") {
			if (x > found)
				output = INVALID_COMMAND;
			else
				output = result;
		} else
			output = result;
		return output;
	}

	/**
	 * processes the character list based on delimiter values
	 * 
	 * @param characterList List of character positions
	 * @param stdin Standard input
	 * @param fileContents Contents of file
	 * @param x position of "-"
	 * @param found If "-" is found or not
	 * @return  Content to be displayed
	 */
	private String processCharacterList(String characterList, String stdin,
			String fileContents, int x, int found) {
		String result = "";
		if (delim != null)
			result = cutSpecifiedCharactersUseDelimiter(inputList.get(x),
					delim, fileContents);
		if (result != INVALID_COMMAND)
			setStatusCode(0);
		if (stdin != null && stdin != "" && (found > -1)) {
			if (x > found)
				characterList = INVALID_COMMAND;
			else
				characterList = result;
		} else
			characterList = result;
		return characterList;
	}

	/**
	 * add content to file based on input
	 * @param workingDir working directory
	 * @param stdin standard input which might contain file contents
	 * @param fileContents Contents of one file
	 * @return Contents of all files
	 */
	private String addFileContent(File workingDir, String stdin,
			String fileContents) {
		String validInput = inputList.get(inputList.size() - 1);

		File file = new File(validInput);
		file = checkForAbsoluteFile(workingDir, validInput, file);
		// Set file contents to a string
		if (file.exists()) {
			fileContents = getStringForFile(file);
		} else if (stdin != null && stdin != "") {
			fileContents = stdin;
		} else {
			setStatusCode(1);
		}
		return fileContents;
	}

	/**
	 * construct file if if it is not absolute
	 * @param workingDir Working directory
	 * @param validInput Relative path
	 * @param file File whose contents have to be displayed
	 * @return
	 */
	private File checkForAbsoluteFile(File workingDir, String validInput,
			File file) {
		if (!(file.isAbsolute())) {
			file = new File(workingDir, validInput);
		}
		return file;
	}

	/**
	 * build list based on user input (options)
	 * @param x position of the "-"
	 * @param found If "-" is found or not
	 * @param arg arguments sent by Shell
	 * @return  If the "-" is found or not
	 */
	private int buildListBasedOnOptions(int x, int found, String arg) {
		if (arg.startsWith("-")) {
			if (arg.equals("-c") || arg.equals("-d") || arg.equals("-f")
					|| arg.equals("-help"))
				argList.add(arg);
			else if (arg.equals("-")) {
				found = x;
			} else {
				setStatusCode(1);
			}
		} else {
			inputList.add(arg);
		}
		return found;
	}

	/**
	 * add tokens from token list to argument array
	 * @param stdin Standard input
	 */
	private void populateArguments(String stdin) {
		List<String> tokenList = Arrays.asList(stdin.split(" "));
		args = new String[tokenList.size()];
		for (int i = 0; i < tokenList.size(); i++) {
			args[i] = tokenList.get(i);
		}
	}

	/**
	 * get file content
	 * @param toRead File to be read
	 * @return File contents
	 */
	public String getStringForFile(File toRead) {
		BufferedReader br;
		String content = "";
		if (toRead.isFile() && toRead.canRead()) { // checks for Exists and !isDirectory
			try {
				br = new BufferedReader(new FileReader(toRead));
				String line = null;
				try {
					// content += "Reading file: " + toRead.getName() + ": ";
					while ((line = br.readLine()) != null) {
						content += line + "\n";
					}
					br.close();
					setStatusCode(0);
				} catch (IOException e) {
					content = ERROR_NO_SUCH_FILE_OR_DIRECTORY;
				}
			} catch (FileNotFoundException e) {
				content = ERROR_NO_SUCH_FILE_OR_DIRECTORY;
			}
		}else{
			content = ERROR_NO_SUCH_FILE_OR_DIRECTORY;
		}
		return content;
	}

}
