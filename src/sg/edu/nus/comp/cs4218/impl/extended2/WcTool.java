package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/*
 * 
 * wc : Prints the number of bytes, words, and lines in given file
 *
 * Command Format - wc [OPTIONS] [FILE]
 * FILE - Name of the file, when no file is present (denoted by "-") use standard input
 * OPTIONS
 *		-m : Print only the character counts
 *      -w : Print only the word counts
 *      -l : Print only the newline counts
 *		-help : Brief information about supported options
 */

public class WcTool extends ATool implements IWcTool {

	int readStatus = -1;
	//File workingDir;

	public WcTool(String[] args) {
		super(args);
	}

	@Override
	public String getCharacterCount(String input) {
		if (input == null || readStatus == 1)
			return "0";
		int result = input.length();
		setStatusCode(0);
		return Integer.toString(result);
	}

	@Override
	public String getWordCount(String input) {
		if (input == null || readStatus == 1)
			return "0";

		String[] words = input.split("[ \\n]");
		int result = words.length;
		setStatusCode(0);
		return Integer.toString(result);
	}

	@Override
	public String getNewLineCount(String input) {
		if (input == null || readStatus == 1)
			return "0";

		int result = input.length() - input.replaceAll("\n", "").length();
		setStatusCode(0);
		return Integer.toString(result);
	}

	@Override
	public String getHelp() {
		String help = "-m : Print only the character counts\t"
				+ " -w : Print only the word counts\t"
				+ " -l : Print only the newline counts\t"
				+ " -help : Brief information about supported options";
		setStatusCode(0);
		return help;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		//this.workingDir = workingDir;
		StringBuilder result = new StringBuilder();
		if ((args == null) && (stdin == null || stdin.compareTo("") == 0)) {
			result.append("No arguments and no standard input.");
			return result.toString();
		}

		String content = null;
		if (stdin != null && stdin.compareTo("") != 0) {
			content = stdin;
		}

		if (args.length > 0 && args[0] != null && args[0].compareTo("-m") == 0) {
			try {
				characterCount(result, content);
			} catch (IOException e) {
				result.append("word count: open failed: " + e.getMessage()
						+ ": No such file or directory.");
				readStatus = 1;
			}
		} else if (args.length > 0 && args[0] != null && args[0].compareTo("-w") == 0) {
			try {
				wordCount(result, content);
			} catch (IOException e) {
				result.append("word count: open failed: " + e.getMessage()
						+ ": No such file or directory.");
				readStatus = 1;
			}
		} else if (args.length > 0 && args[0] != null && args[0].compareTo("-l") == 0) {
			try {
				lineCount(result, content);
			} catch (IOException e) {
				result.append("word count: open failed: " + e.getMessage()
						+ ": No such file or directory.");
				readStatus = 1;
			}
		} else if (args.length > 0 && args[0] != null && args[0].compareTo("-help") == 0) {
			result.append(getHelp());
		} else if (args.length > 0 && args[0] != null && args[0].startsWith("-")
				&& args[0].compareTo("-m") != 0 && args[0].compareTo("-l") != 0
				&& args[0].compareTo("-w") != 0) {
			result.append("Invalid arguments.");
		} else {
			try {
				generalCount(result, content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	private void generalCount(StringBuilder result, String content)
			throws IOException {
		if (content != null) {
			result.append("\t" + getNewLineCount(content));
			result.append("\t" + getWordCount(content));
			result.append("\t" + getCharacterCount(content));
		} else
			for (int i = 0; i < args.length; i++) {
				content = readFile(args[i], Charset.forName("UTF-8"));
				result.append("\t" + getNewLineCount(content));
				result.append("\t" + getWordCount(content));
				result.append("\t" + getCharacterCount(content));
				result.append("\t" + args[i]);
			}
	}

	private void lineCount(StringBuilder result, String content)
			throws IOException {
		Charset std = Charset.forName("UTF-8");
		if (content != null)
			result.append(getNewLineCount(content));
		else
			for (int i = 1; i < args.length; i++) {
				content = readFile(args[i], std);
				result.append(getNewLineCount(content) + "\t" + args[i]);
			}
	}

	private void wordCount(StringBuilder result, String content)
			throws IOException {
		Charset std = Charset.forName("UTF-8");
		if (content != null)
			result.append(getWordCount(content));
		else
			for (int i = 1; i < args.length; i++) {
				content = readFile(args[i], std);
				result.append(getWordCount(content) + "\t" + args[i]);
			}
	}

	private void characterCount(StringBuilder result, String content)
			throws IOException {
		Charset std = Charset.forName("UTF-8");
		if (content != null)
			result.append(getCharacterCount(content));
		else
			for (int i = 1; i < args.length; i++) {
				content = readFile(args[i], std);
				result.append(getCharacterCount(content) + "\t" + args[i]);
			}
	}

	/*
	 * Read content from file
	 */
	public String readFile(String fileName, Charset encoding)
			throws IOException {
		try {
			String filePath;
			readStatus = -1;
			//File f = new File(workingDir, fileName);
			File f = new File(fileName);
			if (f.isAbsolute())
				filePath = fileName;
			else
				filePath = System.getProperty("user.dir") + File.separator
						+ fileName;

			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			throw new IOException(fileName);
		}
	}
}