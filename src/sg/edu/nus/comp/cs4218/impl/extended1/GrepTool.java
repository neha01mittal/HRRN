package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Searches for pattern in a file
 * 
 * @usage grep [-A | -B | -C | -c | -o | -v | -help] pattern [path]
 * @options grep -A NUM filename: Print NUM lines of trailing context after
 *          matching lines grep -B NUM filename: Print NUM lines of leading
 *          context before matching lines grep -C NUM filename: Print NUM lines
 *          of output context grep -c “pattern” filename : Print a count of
 *          matching lines with pattern grep -c ‘“pattern”’ filename : Print a
 *          count of matching lines with “pattern”(pattern surrounded by double
 *          quotes) grep -c “pattern” file1 file2 : Print a count of matching
 *          lines containing pattern for both files grep -o “pattern” filename:
 *          Show only the part of a matching line that matches PATTERN grep -v
 *          “pattern” filename: Select non-matching (instead of matching) lines
 *          grep -help : Brief information about supported options grep -o -v
 *          “pattern filename: Provides the conjunction of results from both
 *          options grep -<any option> ‘pattern’ filename: Provides the same
 *          output as compared to pattern surrounded by double quotes grep -<any
 *          option> <pattern with one word> filename: Provides the same output
 *          even without surrounding quotes if the pattern consists of one word
 *          grep -<any option> “pattern” file1 file2: Provides the output after
 *          executing the command on both files grep filename: Prints the
 *          command since no option was provided
 * @note
 * @success
 * @exceptions
 */
public class GrepTool extends ATool implements IGrepTool {
	private int statusCode;

	private int[] mark;
	public int lastOpt = -1;
	private int[] fileLength;
	private Vector<String> fileList;

	public GrepTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		String result = "";

		Map<String, ArrayList<String>> parsed = parse();
		String pattern = getPatternFromInput();
		if (pattern.compareTo("") == 0)
			return "Invalid command";
		fileList = getFileListFromInput();
		fileLength = new int[fileList.size()];

		String fileContent;
		if (stdin != null && stdin.compareTo("") != 0
				&& getFileListFromInput().size() == 0)
			fileContent = stdin;
		else
			fileContent = getFileContentFromInput(getFileListFromInput());

		mark = new int[fileContent.split("\n").length];

		if (parsed.containsKey("h") || parsed.containsKey("help")) {
			result = getHelp();
		}
		if (parsed.containsKey("C")) {
			int optionC = Integer.parseInt(parsed.get("C").get(0));
			getMatchingLinesWithOutputContext(optionC, pattern, fileContent);
		}
		if (parsed.containsKey("A")) {
			int optionA = Integer.parseInt(parsed.get("A").get(0));
			getMatchingLinesWithTrailingContext(optionA, pattern, fileContent);
		}
		if (parsed.containsKey("B")) {
			int optionB = Integer.parseInt(parsed.get("B").get(0));
			getMatchingLinesWithLeadingContext(optionB, pattern, fileContent);
		}

		getOnlyMatchingLines(pattern, fileContent);

		if (parsed.containsKey("o")) {
			result = getMatchingLinesOnlyMatchingPart(pattern, fileContent);
		}
		if (parsed.containsKey("v")) {
			getNonMatchingLines(pattern, fileContent);
		}
		if (parsed.containsKey("c")) {
			result = getFinalResultInt();
		} else
			result = getFinalResultString(fileContent.split("\n"));

		// cut out the \n char at the end of result
		if (result.endsWith("\n"))
			result = result.substring(0, result.length() - 1);
		return result;
	}

	private String getFinalResultInt() {
		String result = "";
		int tempResult = 0;
		if (fileList.size() < 2) {
			for (int i = 0; i < mark.length; i++)
				if (mark[i] == 1)
					tempResult++;
			result = Integer.toString(tempResult);
			return result;
		}

		int begin = 0;
		for (int i = 0; i < fileList.size(); i++) {
			int matchCount = 0;
			for (int j = begin; j < fileLength[i]; j++) {
				if (mark[j] == 1)
					matchCount++;
			}
			begin = fileLength[i];
			result += fileList.get(i) + ": " + Integer.toString(matchCount)
					+ "\n";
		}

		return result;
	}

	/*
	 * 3 following methods are for getting pattern and file content from input
	 * command
	 */
	public String getFileContentFromInput(Vector<String> stdin) {
		String result = "";
		int lineCount = 0;

		for (int i = 0; i < stdin.size(); i++) {
			try {
				String filePath;
				File f = new File(stdin.get(i));
				if (f.isAbsolute())
					filePath = stdin.get(i);
				else
					filePath = System.getProperty("user.dir") + File.separator
							+ stdin.get(i);
				File targetFile = new File(filePath);
				if (!targetFile.isFile())
					continue;

				FileInputStream fis = new FileInputStream(filePath);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine = "";
				while ((strLine = br.readLine()) != null) {
					result += strLine + "\n";
					lineCount++;
				}
				br.close();
				in.close();
				fis.close();
				fileLength[i] = lineCount;
				setStatusCode(0);
			} catch (IOException e) {
				fileLength[i] = lineCount;
				setStatusCode(1);
			}
		}

		return result;
	}

	/*
	 * Get list of files to read from input args
	 */
	public Vector<String> getFileListFromInput() {
		Vector<String> result = new Vector<String>();

		// String[] tokens = stdin.trim().replace(" +", " ").split(" ");
		int patternIndex = lastOpt + 1;
		assert patternIndex < args.length;
		for (int i = patternIndex + 1; i < args.length; i++) {
			result.add(args[i]);
		}
		setStatusCode(0);
		return result;
	}

	/*
	 * Get pattern from args
	 */
	public String getPatternFromInput() {
		String result = "";
		if (lastOpt + 1 < args.length)
			result = args[lastOpt + 1];
		return result;
	}

	@Override
	public int getStatusCode() {
		return this.statusCode;
	}

	@Override
	public int getCountOfMatchingLines(String pattern, String input) {
		int result = 0;
		result = getListOfMatchingLines(pattern, input);
		setStatusCode(0);
		return result;
	}

	private int getListOfMatchingLines(String pattern, String input) {
		int result = 0;

		try {
			String[] lines = input.split("\n");
			ArrayList<String> matchingLines = new ArrayList<String>();
			for (String line : lines) {
				// if (line.matches(pattern))
				if (match(line, pattern))
					matchingLines.add(line);
			}
			result = matchingLines.size();
		} catch (Exception e) {
			setStatusCode(1);
		}
		setStatusCode(0);
		return result;
	}

	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		String result = "";

		String[] lines = input.split("\n");
		for (int i = 0; i < mark.length; i++) {
			// if (line.matches(pattern))
			String line = lines[i];
			if (match(line, pattern))
				mark[i] = 1;
		}
		setStatusCode(0);
		return result;
	}

	/*
	 * grep "pattern" file1 file2 file3 grep -C 2 "pattern" file1 file2 file3
	 * grep -A 3 "pattern" folderPath Parse the input command to filter out
	 * options
	 */
	public Map<String, ArrayList<String>> parse() {
		Map<String, ArrayList<String>> parsed = new HashMap<String, ArrayList<String>>();

		int i = 0;
		while (i < args.length) {
			if (args[i].startsWith("-")) {
				lastOpt = i;
				String option = args[i].substring(1);
				if (parsed.get(option) == null) {
					ArrayList<String> newArrayList = new ArrayList<String>();
					parsed.put(option, newArrayList);
					i++;
					if ((option.compareTo("A") != 0)
							&& (option.compareTo("B") != 0)
							&& (option.compareTo("C") != 0))
						continue;
					if (i < args.length) {
						assert (args[i].matches("-?\\d+"));
						parsed.get(option).add(args[i]);
						lastOpt = i;
					}
				}
			}
			i++;
		}
		setStatusCode(0);
		return parsed;
	}

	@Override
	public String getMatchingLinesWithTrailingContext(int optionA,
			String pattern, String input) {
		// print NUM lines after the matching lines
		String result = "";
		String[] lines = input.split("\n");
		if (mark == null)
			mark = new int[lines.length];
		for (int j = 0; j < mark.length; j++) {
			String line = lines[j]; // if j-th line hits, print lines j + 1 -> j
									// + option_A - 1
			if (match(line, pattern)) {
				int i = 1;
				mark[j] = 1;
				while (i + j < mark.length && i <= optionA) {
					if (mark[i + j] != 1)
						mark[i + j] = 2;
					i++;
				}
			}
		}
		for (int i = 0; i < mark.length; i++) {
			if (mark[i] != 0)
				result += lines[i] + "\n";
		}

		if (result.endsWith("\n"))
			result = result.substring(0, result.length() - 1);
		setStatusCode(0);
		return result;
	}

	@Override
	public String getMatchingLinesWithLeadingContext(int optionB,
			String pattern, String input) {
		String result = "";
		String[] lines = input.split("\n");
		if (mark == null)
			mark = new int[lines.length];
		for (int j = 0; j < mark.length; j++) {
			String line = lines[j];
			if (match(line, pattern)) {
				int i = 1;
				mark[j] = 1;
				while (i <= j && i <= optionB) {
					// result += lines[j - i];
					if (mark[j - i] != 1)
						mark[j - i] = 2;
					i++;
				}
			}
		}

		for (int i = 0; i < mark.length; i++) {
			if (mark[i] != 0)
				result += lines[i] + "\n";
		}

		if (result.endsWith("\n"))
			result = result.substring(0, result.length() - 1);
		setStatusCode(0);
		return result;
	}

	@Override
	public String getMatchingLinesWithOutputContext(int optionC,
			String pattern, String input) {
		String result = "";
		String[] lines = input.split("\n");
		if (mark == null)
			mark = new int[lines.length];
		for (int j = 0; j < mark.length; j++) {
			String line = lines[j]; // if j-th line hits, print lines j + 1 -> j
									// + option_A - 1
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				int i = 1;
				mark[j] = 1;
				while (i + j < mark.length && i <= optionC) {
					if (mark[i + j] != 1)
						mark[i + j] = 2;
					i++;
				}
			}
		}
		for (int j = 0; j < mark.length; j++) {
			String line = lines[j];
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				int i = 1;
				mark[j] = 1;
				while (i <= j && i <= optionC) {
					// result += lines[j - i];
					if (mark[j - i] != 1)
						mark[j - i] = 2;
					i++;
				}
			}
		}
		for (int i = 0; i < mark.length; i++) {
			if (mark[i] != 0)
				result += lines[i] + "\n";
		}

		if (result.endsWith("\n"))
			result = result.substring(0, result.length() - 1);
		setStatusCode(0);
		return result;
	}

	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		String result = "";

		String[] lines = input.split("\n");
		if (mark == null)
			mark = new int[lines.length];
		for (int j = 0; j < mark.length; j++) {
			String line = lines[j];
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				result += getMatchedGroups(pattern, line) + "\n";
			}
		}
		if (result.endsWith("\n"))
			result = result.substring(0, result.length() - 1);
		setStatusCode(0);
		return result;
	}

	@Override
	public String getNonMatchingLines(String pattern, String input) {
		String result = "";
		String[] lines = input.split("\n");
		if (mark == null)
			mark = new int[lines.length];
		for (int i = 0; i < mark.length; i++) {
			String line = lines[i];
			if (!match(line, pattern)) {
				mark[i] = 1;
				result += line + "\n";
			} else
				mark[i] = 0;
		}
		// if (result.endsWith("\n"))
		// result = result.substring(0, result.length() - 1);
		setStatusCode(0);
		return result;
	}

	@Override
	public String getHelp() {
		String helpString = "The grep command searches one or more input files \n"
				+ "for lines containing a match to a specified pattern. \n"
				+ "The grep tool must work on all characters in UTF-8 encoding.\n"
				+ "Command Format - grep [OPTIONS] PATTERN [FILE]\n"
				+ "PATTERN - This specifies a regular expression pattern that describes a set of strings\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n"
				+ "OPTIONS\n"
				+ "-A NUM : Print NUM lines of trailing context after matching lines\n"
				+ "-B NUM : Print NUM lines of leading context before matching lines\n"
				+ "-C NUM : Print NUM lines of output context\n"
				+ "-c : Suppress normal output. Instead print a count of matching lines for each input file\n"
				+ "-o : Show only the part of a matching line that matches PATTERN\n"
				+ "-v : Select non-matching (instead of matching) lines\n"
				+ "-help : Brief information about supported options";
		setStatusCode(0);
		return helpString;
	}

	/*
	 * If PATTERN has special character: take it as a regex else: check if
	 * source contains pattern as a substring
	 */
	public static boolean match(String source, String pattern) {
		boolean isRegex;
		try {
			Pattern.compile(pattern);
			isRegex = true;
		} catch (PatternSyntaxException e) {
			isRegex = false;
		}
		if (isRegex == false)
			return source.contains(pattern);

		// if pattern is a correct regex
		Pattern p1 = Pattern.compile(pattern);
		Matcher m1 = p1.matcher(source);
		return m1.find();
	}

	public static String getMatchedGroups(String pattern, String line) {
		String result = "";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		m.matches();
		if (m.find()) {
			int i = 0;
			do {
				result += m.group(i);
				i++;
			} while (i < m.groupCount());
		}
		return result;
	}

	public String getFinalResultString(String[] lines) {
		StringBuilder result = new StringBuilder();
		if (fileList.size() < 2) {
			for (int i = 0; i < mark.length; i++)
				if (mark[i] != 0)
					result.append(lines[i] + "\n");
			return result.toString();
		}

		int begin = 0;
		for (int i = 0; i < fileList.size(); i++) {
			for (int j = begin; j < fileLength[i]; j++) {
				if (mark[j] != 0)
					result.append(fileList.get(i) + ": " + lines[j] + "\n");
			}
			begin = fileLength[i];
		}
		setStatusCode(0);
		return result.toString();
	}
}