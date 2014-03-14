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
 * Specifications for Cut
 * 		cut [options] [character_positions] file
 *	cut file: prints all the file contents on the console
 *	cut -c 1,2-4,5 file : prints all the characters at positions from each line.
 *	cut -d delim -f 1,2-4,5 file: separates characters by delimiter and prints    
 *	command1 | cut : The output of command1 is treated as file contents to be used by cut
 *  command1 | cut [options] : stdin used as file contents
 *  command1 | cut - : "-" is replaced by file contents
 *  command1 | cut -c [character_positions] - 
 *  command1 | cut -d "," -f [character_positions] - 
 * @author ranjini
 *
 */
public class CutTool extends ATool implements ICutTool {

	private final List<String> argList;
	private final List<String> inputList;
	private String delim;

	public CutTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		setStatusCode(0);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
		delim = null;
	}

	@Override
	public String cutSpecfiedCharacters(String list, String input) {

		return cutSpecifiedCharactersUseDelimiter(list, "", input);

	}

	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim, String input) {
		// TODO Auto-generated method stub
		String escapeDelim = "";
		for(int i = 0; i < delim.length(); i ++){
			if (delim.substring(i, i+1).matches("[^a-zA-Z0-9 ]")){
				escapeDelim = escapeDelim + "\\" + delim.substring(i, i+1);
			}else{
				escapeDelim = escapeDelim + delim.substring(i, i+1);
			}
		}
		String cutString = "";

		List<Integer> characterList = new ArrayList<Integer>();

		List<String> elephantList = Arrays.asList(list.split(","));
		Collections.sort(elephantList);
		try {
			for (int x = 0; x < elephantList.size(); x++) {

				if (elephantList.get(x).contains("-")) {
					List<String> firstList = Arrays.asList(elephantList.get(x).split("-"));
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
							return "Invalid command";
						}

					} else {
						setStatusCode(1);
						return "Invalid command";
					}

				} else {
					int num = Integer.parseInt(elephantList.get(x));
					if (num > 0) {
						if (!(characterList.contains(num - 1)))
							characterList.add(num - 1);
					} else {
						setStatusCode(1);
						return "Invalid command";
					}
				}

			}
		} catch (Exception e) {
			setStatusCode(1);
			return "Invalid command";
		}
		Collections.sort(characterList);

		List<String> splitNewLine = Arrays.asList(input.split("\n"));
		if (escapeDelim == "") {
			for (String line : splitNewLine) {
				for (int num : characterList)
					if (line.length() > num)
						cutString = cutString + line.substring(num, num + 1);
				cutString = cutString + "\n";
			}

		} else {
			for (String f : splitNewLine) {
				List<String> inputLineList = Arrays.asList(f.split(escapeDelim));
				for (int num : characterList) {
					if (inputLineList.size() > num)
						if (num == characterList.get(characterList.size() - 1))
							cutString = cutString + inputLineList.get(num);
						else
							cutString = cutString + inputLineList.get(num) + delim;
				}
				if (cutString.endsWith(delim)) {
					int size = cutString.length();
					cutString = cutString.substring(0, size - 1);
				}
				cutString = cutString + "\n";

			}
		}
		if (cutString.endsWith("\n")) {
			int size = cutString.length();
			cutString = cutString.substring(0, size - 1);
		}
		return cutString;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		String helpString = "cut : prints a substring that is specified in a certain range" + "\nCommand Format - cut [OPTIONS] [FILE]"
				+ "\nFILE - Name of the file, when no file is present (denoted by '-') use standard input OPTIONS"
				+ "\n-c LIST: Use LIST as the list of characters to cut out. E.g 'cut -c 1-5,10,18-30'"
				+ "\nspecifies characters 1 through 5, 10 and 18 through 30."
				+ "\n-d DELIM -f FIELD: Use DELIM as the field-separator character instead of the TAB character."
				+ "\nFIELD is similar to LIST except FIELD is used as the list of string to cut out after delimited by DELIM."
				+ "\nThe way to define FIELD is the same as LIST." + "\n-help : Brief information about supported options";
		return helpString;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		String fileContents = "";
		String characterList = "";

		if (args == null || args.length == 0) {
				if (stdin != null && stdin != "") {
					List<String> ar = Arrays.asList(stdin.split(" "));
					args = new String[ar.size()];
					for (int a = 0; a < ar.size(); a++) {
						args[a] = ar.get(a);
					}
				} else {
					setStatusCode(1);
					return "No arguments and no standard input.";
				}
		}  
		

		// split arguments and inputs
		int x = 0;
		int found = -1;
		for (String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.equals("-c") || arg.equals("-d") || arg.equals("-f") || arg.equals("-help"))
					argList.add(arg);
				else if (arg.equals("-")){
					found = x;
				}
				else {
					setStatusCode(1);
					return "Invalid command";
				}
			} else {
				inputList.add(arg);
			}
			x++;
		}

		for (x = 0; x < argList.size(); x++) {
			String arg = argList.get(x);
			if (arg.equals("-d")) {
				delim = inputList.get(x);
			}

			else if (arg.equals("-help")) {
				setStatusCode(0);
				return getHelp();
			}
		}

		if (argList.contains("-c") && (argList.contains("-f") || argList.contains("-d"))) {
			setStatusCode(1);
			return "Invalid command";
		}

		if (argList.contains("-d") && (!(argList.contains("-f")))) {
			setStatusCode(1);
			return "Invalid command";
		}
		// Filename is the last one
		String validInput = inputList.get(inputList.size() - 1);

		File file = new File(validInput);
		if (!(file.isAbsolute())) {
			file = new File(workingDir, validInput);
		}
		// Set file contents to a string
		if (file.exists()) {
			fileContents = getStringForFile(file);
		}else if(stdin != null && stdin != ""){
			fileContents = stdin;
		}else{
			setStatusCode(1);
			return "Invalid command";
		}

			for ( x = 0; x < argList.size(); x++) {

				if (argList.get(x).equals("-c")) {
					String result = cutSpecfiedCharacters(inputList.get(x), fileContents);
					if (result != "Invalid command")
						setStatusCode(0);
					if(stdin != null && stdin != ""){
						if(x > found)
							characterList = "Invalid command";
						else
							characterList =  result;
					}
					else
						characterList = result;
					return characterList;
					
				} else if (argList.get(x).equals("-f")) {
					String result = "";
					if (delim != null)
						result = cutSpecifiedCharactersUseDelimiter(inputList.get(x), delim, fileContents);
					if (result != "Invalid command")
						setStatusCode(0);
					if(stdin != null && stdin != "" && (found>-1)){
						if(x > found)
							characterList = "Invalid command";
						else
							characterList =  result;
					}
					else
						characterList = result;
					return characterList;
				}
			}
		return characterList;
	}

	public String getStringForFile(File toRead) {
		BufferedReader br;
		String content = "";
		if (!toRead.isFile()) { // checks for Exists and !isDirectory
			content = "Error: No such file or directory\n";
		}

		else if (!toRead.canRead()) {
			content = "Error: Unable to read this file type";
		} else {
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
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

}
