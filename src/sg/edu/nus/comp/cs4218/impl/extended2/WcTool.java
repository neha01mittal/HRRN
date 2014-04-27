package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

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
	private int readStatus = -1;
	private int fileIndex = -1;
	private File workingDir;
	Vector<String> argList = new Vector<String>();

	/**
	 * constructor for WcTool
	 * 
	 * @param args
	 */
	public WcTool(String[] args) {
		super(args);
	}

	/**
	 * @input the string whose characters are counted
	 * @return returns the character count of the input string
	 */
	@Override
	public String getCharacterCount(String input) {
		if (input == null || input.compareTo("") == 0 || readStatus == 1)
			return "0";
		int result = input.length();
		setStatusCode(0);
		return Integer.toString(result);
	}

	/**
	 * @param input
	 *            the string whose words are counted
	 * @return returns the word count of the input string
	 */
	@Override
	public String getWordCount(String input) {
		if (input == null || input.compareTo("") == 0 || readStatus == 1)
			return "0";
		
		String[] words = input.split("[ \\n]");
		int result = words.length;
		setStatusCode(0);
		return Integer.toString(result);
	}

	/**
	 * @param input
	 *            string whose lines are counted
	 * @return gets the new line count of input string
	 */
	@Override
	public String getNewLineCount(String input) {
		if (input == null || input.compareTo("") == 0 || readStatus == 1)
			return "0";

		int result = input.length() - input.replaceAll("\n", "").length();
		setStatusCode(0);
		return Integer.toString(result);
	}

	/**
	 * displays the menu with options supported by WCTool
	 */
	@Override
	public String getHelp() {
		String help = "-m : Print only the character counts\t"
				+ " -w : Print only the word counts\t"
				+ " -l : Print only the newline counts\t"
				+ " -help : Brief information about supported options";
		setStatusCode(0);
		return help;
	}

	/**
	 * @param workingDir
	 *            current working directory
	 * @stdin input received from pipe
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		this.workingDir = workingDir;
		StringBuilder result = new StringBuilder();
		if ((args == null) && (stdin == null || stdin.compareTo("") == 0)) {
			result.append("No arguments and no standard input.");
			return result.toString();
		}
		String content = populateContent(stdin);
		
		for (int i = 0; i < args.length; i++) {
	        String arg = args[i];
	        if (arg.compareTo("-") == 0 && i ==  args.length - 1)
	        	content = stdin;
	        else if (arg.startsWith("-") && arg.compareTo("-m") != 0
	        		&& arg.compareTo("-w") != 0 && arg.compareTo("-l") != 0
	        		&& arg.compareTo("-help") != 0) {
	        	this.setStatusCode(1);
	        	result.append("Invalid arguments.");
	        	return result.toString();
	        }
	    }
		addOptionsToArgList();

		if (argList.contains("-help")) {
			result.append(getHelp());
			return result.toString();
		}

		return processOutput(result, content);
	}

	/**
	 * 
	 * @param result
	 *            the resulting string formed after the execute function is
	 *            executed
	 * @param content
	 *            file content to be processed
	 * @return This function appends the content to result (or error message in
	 *         case of exception) and returns it to the console
	 */
	private String processOutput(StringBuilder result, String content) {
		if (fileIndex == -1)
			try {
				processString(result, content);
			} catch (IOException e) {
			}
		else
			for (int i = fileIndex; i < args.length; i++) {
				try {
					processFile(result, args[i]);
				} catch (IOException e1) {
					this.setStatusCode(1);
					result.append("word count: open failed: " + args[i]
							+ ": No such file or directory.");
				}
			}
		this.setStatusCode(0);
		return result.toString();
	}

	/**
	 * This function populates the argList with options sent by the user as
	 * input
	 */
	private void addOptionsToArgList() {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null || !args[i].startsWith("-")) {
				fileIndex = i;
				break;
			}
			if (args[i].compareTo("-w") == 0)
				argList.add("-w");
			if (args[i].compareTo("-m") == 0)
				argList.add("-m");
			if (args[i].compareTo("-l") == 0)
				argList.add("-l");
			if (args[i].compareTo("-help") == 0)
				argList.add("-help");
		}
	}

	/**
	 * 
	 * @param stdin
	 *            standard input
	 * @return this method populates the content with stdin if it is not null or
	 *         empty
	 */
	private String populateContent(String stdin) {
		String content = null;
		if (stdin != null && stdin.compareTo("") != 0 || args[args.length - 1].compareTo("-") == 0) {
			content = stdin;
		}
		return content;
	}

	/**
	 * 
	 * @param stringbuilder
	 *            the string builder to which the new strings are attached
	 * @param content
	 *            content of the file
	 * @throws IOException
	 *             This function formats the string and appends the right
	 *             character/ word/ line count depending on the option specified
	 *             by the user
	 */
	private void processString(StringBuilder stringbuilder, String content)
			throws IOException {
		if (this.argList.contains("-m")) {
			stringbuilder.append("\t");
			characterCount(stringbuilder, content);
		}
		if (this.argList.contains("-w")) {
			stringbuilder.append("\t");
			wordCount(stringbuilder, content);
		}
		if (this.argList.contains("-l")) {
			stringbuilder.append("\t");
			lineCount(stringbuilder, content);
		}
		if (this.argList.isEmpty())
			generalCount(stringbuilder, content);
	}

	/**
	 * 
	 * @param stringBuilder the string to which every new string is appended
	 * @param fileName the file which is being read
	 * @throws IOException
	 * This function reads the file contents and append them to the string builder according to options input by the user
	 */
	private void processFile(StringBuilder stringBuilder, String fileName)
			throws IOException {
		// TODO Auto-generated method stub
		String fileContent = readFile(fileName, Charset.forName("UTF-8"));
		if (this.argList.contains("-m")) {
			stringBuilder.append("\t");
			characterCount(stringBuilder, fileContent);
		}
		if (this.argList.contains("-w")) {
			stringBuilder.append("\t");
			wordCount(stringBuilder, fileContent);
		}
		if (this.argList.contains("-l")) {
			stringBuilder.append("\t");
			lineCount(stringBuilder, fileContent);
		}
		if (this.argList.isEmpty()) {
			generalCount(stringBuilder, fileContent);
		}
		stringBuilder.append("\t" + fileName);
		stringBuilder.append("\n");
	}

	/**
	 * 
	 * @param result the final string which is processed
	 * @param content file content whose character, line and word count is appended to result
	 * @throws IOException
	 * This method appends the content's line, word and character count to the final result
	 */
	private void generalCount(StringBuilder result, String content)
			throws IOException {
		if (content != null) {
			result.append("\t" + getNewLineCount(content));
			result.append("\t" + getWordCount(content));
			result.append("\t" + getCharacterCount(content));
		}
	}

	/**
	 * 
	 * @param result the final string which is processed
	 * @param content file content whose line count is appended to result
	 * @throws IOException
	 * This method appends the content's line count to the final result
	 */
	private void lineCount(StringBuilder result, String content)
			throws IOException {
		if (content != null)
			result.append(getNewLineCount(content));
	}

	/**
	 * 
	 * @param result the final string which is processed
	 * @param content file content whose word count is appended to result
	 * @throws IOException
	 * This method appends the content's word count to the final result
	 */
	private void wordCount(StringBuilder result, String content)
			throws IOException {
		if (content != null)
			result.append(getWordCount(content));
	}

	/**
	 * 
	 * @param result the final string which is processed
	 * @param content file content whose character count is appended to result
	 * @throws IOException
	 * This method appends the content's character count to the final result
	 */
	private void characterCount(StringBuilder result, String content)
			throws IOException {
		if (content != null)
			result.append(getCharacterCount(content));
	}

	/**
	 * @param filename name of the file
	 * @param encoding byte encoding for decoding byte buffer
	 * @return Read content from file
	 */
	public String readFile(String fileName, Charset encoding)
			throws IOException {
		try {
			String filePath;
			readStatus = -1;
			// File f = new File(workingDir, fileName);
			File f = new File(fileName);
			if (f.isAbsolute())
				filePath = fileName;
			else if (workingDir == null)
				filePath = System.getProperty("user.dir") + File.separator
						+ fileName;
			else
				filePath = workingDir.getAbsolutePath() + File.separator
						+ fileName;

			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			return encoding.decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			this.setStatusCode(1);
			throw new IOException(fileName);
		}
	}
}