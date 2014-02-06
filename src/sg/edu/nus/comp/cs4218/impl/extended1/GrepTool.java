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

public class GrepTool extends ATool implements IGrepTool {
	private int statusCode;

	private int[] mark;
	private int lastOpt = -1;

	// private final String[] allOptions = {"A", "B", "C", "c", "o", "v",
	// "help"};

	// input: content of the processing string/file
	// check not directory and file readable
	// check empty file path
	// check empty string
	// check double quotes, single quotes
	//

	public GrepTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		String result = "";
		// String[] tokens = stdin.split(" ");
		// assert tokens[0].toLowerCase().compareTo("grep") == 0;

		// get the pattern
		Map<String, ArrayList<String>> parsed = parse();
		String pattern = getPatternFromInput();

		// get the input
		String fileContent;
		if (stdin.compareTo("") != 0)
			fileContent = stdin;
		else
			fileContent = getFileContentFromInput(getFileListFromInput());

		mark = new int[fileContent.split("\n").length];

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

		result = getResultForMultipleOptions(mark, fileContent);

		if (parsed.containsKey("c")) {
			result = Integer.toString(getCountOfMatchingLines(pattern, fileContent));
		}

		if (parsed.containsKey("o")) {
			result = getOnlyMatchingLines(pattern, fileContent);
		}

		if (parsed.containsKey("v")) {
			result = getNonMatchingLines(pattern, fileContent);
		}

		if (parsed.containsKey("h")) {
			result = getHelp();
		}

		if (parsed.isEmpty()) {
			result = getOnlyMatchingLines(pattern, fileContent);
		}
		return result;
	}

	/*
	 * 3 following methods are for getting pattern and file content from input
	 * command
	 */
	public String getFileContentFromInput(Vector<String> stdin) {
		String result = "";

		for (int i = 0; i < stdin.size(); i++) {
			try {
				FileInputStream fis = new FileInputStream(stdin.get(i));
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					result += strLine + "\n";
				}
				br.close();
				in.close();
				fis.close();
			} catch (IOException e) {
				setStatusCode(1);
			}
		}

		return result;
	}

	public Vector<String> getFileListFromInput() {
		Vector<String> result = new Vector<String>();

		// String[] tokens = stdin.trim().replace(" +", " ").split(" ");
		int patternIndex = lastOpt + 1;
		assert patternIndex < args.length;
		for (int i = patternIndex + 1; i < args.length; i++) {
			result.add(args[i]);
		}

		return result;
	}

	public String getPatternFromInput() {
		String result = "";
		// String[] tokens = stdin.trim().replace(" +", " ").split(" ");

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
		result = getLisOftMatchingLines(pattern, input);
		return result;
	}

	private int getLisOftMatchingLines(String pattern, String input) {
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
		return result;
	}

	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		String result = "";

		String[] lines = input.split("\n");
		for (String line : lines) {
			// if (line.matches(pattern))
			if (match(line, pattern))
				result += line + "\n";
		}

		return result;
	}

	/*
	 * grep "pattern" file1 file2 file3 grep -C 2 "pattern" file1 file2 file3
	 * grep -A 3 "pattern" folderPath Parse the input command to filter out
	 * options
	 */
	public Map<String, ArrayList<String>> parse() {
		Map<String, ArrayList<String>> parsed = new HashMap<String, ArrayList<String>>();
		// String[] tokens = input.trim().replace(" +", " ").split(" ");

		int i = 0;
		while (i < args.length) {
			if (args[i].startsWith("-")) {
				lastOpt = i;
				String option = args[i].substring(1);
				if (parsed.get(option) == null) {
					ArrayList<String> newArrayList = new ArrayList<String>();
					parsed.put(option, newArrayList);
					i++;
					if ((option.compareTo("A") != 0) && (option.compareTo("B") != 0) && (option.compareTo("C") != 0))
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
		return parsed;
	}

	@Override
	public String getMatchingLinesWithTrailingContext(int optionA, String pattern, String input) {
		// print NUM lines after the matching lines
		String result = "";
		String[] lines = input.split("\n");
		for (int j = 0; j < lines.length; j++) {
			String line = lines[j]; // if j-th line hits, print lines j + 1 -> j
									// + option_A - 1
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				int i = 0;
				while (i + j < lines.length && i <= optionA) {
					mark[i + j] = 1;
					i++;
				}
			}
		}
		for (int i = 0; i < lines.length; i++) {
			if (mark[i] == 1)
				result += mark[i];
		}

		return result;
	}

	@Override
	public String getMatchingLinesWithLeadingContext(int optionB, String pattern, String input) {
		// print NUM lines before the matching lines
		String result = "";
		String[] lines = input.split("\n");
		for (int j = 0; j < lines.length; j++) {
			String line = lines[j];
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				int i = 0;
				while (i < j && i <= optionB) {
					// result += lines[j - i];
					mark[j - i] = 1;
					i++;
				}
			}
		}

		for (int i = 0; i < lines.length; i++) {
			if (mark[i] == 1)
				result += lines[i] + "\n";
		}

		return result;
	}

	@Override
	public String getMatchingLinesWithOutputContext(int optionC, String pattern, String input) {
		String result = "";
		String[] lines = input.split("\n");
		for (int j = 0; j < lines.length; j++) {
			String line = lines[j]; // if j-th line hits, print lines j + 1 -> j
									// + option_A - 1
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				int i = 0;
				while (i + j < lines.length && i < optionC) {
					// result += lines[i];
					mark[i + j] = 1;
					i++;
				}
			}
		}
		for (int j = 0; j < lines.length; j++) {
			String line = lines[j];
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				int i = 0;
				while (i < j && i < optionC) {
					// result += lines[j - i];
					mark[j - i] = 1;
					i++;
				}
			}
		}
		for (int i = 0; i < lines.length; i++) {
			if (mark[i] == 1)
				result += lines[i];
		}
		return result;
	}

	/*
	 * If a grep command has A, B or C options, after each of the corresponding
	 * method is called, array mark is changed to mark which line will be
	 * included in the final result. This method will give the final result.
	 */
	public String getResultForMultipleOptions(int[] mark, String input) {
		String result = "";
		String[] lines = input.split("\n");
		for (int i = 0; i < lines.length; i++) {
			if (mark[i] == 1)
				result += lines[i] + "\n";
		}
		return result;
	}

	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		String result = "";

		String[] lines = input.split(" ");
		for (int j = 0; j < lines.length; j++) {
			String line = lines[j];
			// if (line.matches(pattern))
			if (match(line, pattern)) {
				result = getMatchedGroups(pattern, line);
			}
		}
		return result;
	}

	@Override
	public String getNonMatchingLines(String pattern, String input) {
		String result = "";
		String[] lines = input.split("\n");
		for (String line : lines) {
			// if (!line.matches(pattern))
			if (!match(line, pattern))
				if (result.compareTo("") == 0)
					result += line;
				else
					result += "\n" + line;
		}

		return result;
	}

	@Override
	public String getHelp() {
		String helpString = "The grep command searches one or more input files \n" + "for lines containing a match to a specified pattern. \n"
				+ "The grep tool must work on all characters in UTF-8 encoding.\n" + "Command Format - grep [OPTIONS] PATTERN [FILE]\n"
				+ "PATTERN - This specifies a regular expression pattern that describes a set of strings\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") use standard input\n" + "OPTIONS\n"
				+ "-A NUM : Print NUM lines of trailing context after matching lines\n" + "-B NUM : Print NUM lines of leading context before matching lines\n"
				+ "-C NUM : Print NUM lines of output context\n" + "-c : Suppress normal output. Instead print a count of matching lines for each input file\n"
				+ "-o : Show only the part of a matching line that matches PATTERN\n" + "-v : Select non-matching (instead of matching) lines\n"
				+ "-help : Brief information about supported options"
				+ "ASSUMPTION: Here we assume that PATTERN is always surrounded by a pair of double quotes";
		return helpString;
	}

	/*
	 * If PATTERN has special character: take it as a regex else: check if
	 * source contains pattern as a substring
	 */
	public static boolean match(String source, String pattern) {
		Pattern p = Pattern.compile("[\\[\\]\\+\\|\\*\\\\^\\$]", java.util.regex.Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(pattern);

		if (m.find()) // if pattern contains characters [,],\,$,*,+
		{
			boolean isRegex;
			try {
				Pattern.compile(pattern);
				isRegex = true;
			} catch (PatternSyntaxException e) {
				isRegex = false;
			}
			if (isRegex == false) // pattern is not a syntactically correct
									// regex
				return source.contains(pattern);

			// if pattern is a correct regex
			Pattern p1 = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			Matcher m1 = p1.matcher(source);
			return m1.find();
			// return source.matches(pattern);
		} else {
			return source.contains(pattern);
		}
	}

	public static String getMatchedGroups(String pattern, String line) {
		String result = "";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		if (m.find()) {
			int hits = m.groupCount();
			for (int i = 0; i < hits; i++) {
				result += m.group(i) + "\n";
			}
		}
		return result;
	}
}