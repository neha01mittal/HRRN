package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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

	public WcTool(String[] args) {
		super(args);
	}

	@Override
	public String getCharacterCount(String input) {
		if (input == null || readStatus == 1)
			return "0";
		String content = "";
		try {
			content = readFile(input, Charset.forName("UTF-8"));
		} catch (IOException e) {
			return "word count: open failed: " + input + ": No such file or directory.";
		}
		int result = content.length();
		setStatusCode(0);
		return Integer.toString(result);
	}

	@Override
	public String getWordCount(String input) {
		if (input == null || readStatus == 1)
			return "0";
		String content = "";
		try {
			content = readFile(input, Charset.forName("UTF-8"));
		} catch (IOException e) {
			return "word count: open failed: " + input + ": No such file or directory.";		
		}

		String[] words = content.split("[ \\n]");
		int result = words.length;
		setStatusCode(0);
		return Integer.toString(result);
	}

	@Override
	public String getNewLineCount(String input) {
		if (input == null || readStatus == 1)
			return "0";
		String content = "";
		try {
			content = readFile(input, Charset.forName("UTF-8"));
		} catch (IOException e) {
			return "word count: open failed: " + input + ": No such file or directory.";
		}

		int result = content.length() - content.replaceAll("\n", "").length();
		setStatusCode(0);
		return Integer.toString(result);
	}

	@Override
	public String getHelp() {
		String help = "-m : Print only the character counts\t" + " -w : Print only the word counts\t" + " -l : Print only the newline counts\t"
				+ " -help : Brief information about supported options";
		setStatusCode(0);
		// System.out.println(help);
		return help;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		StringBuilder result = new StringBuilder();
		if ((args == null) && (stdin == null || stdin.compareTo("") == 0)) {
			result.append("No arguments and no standard input.");
			return result.toString();
		}

		String content = null;
		if (stdin != null && stdin.compareTo("") != 0)
			//content = stdin;
			args = stdin.split(" ");

		if (args[0] != null && args[0].compareTo("-m") == 0) {
			try {
				characterCount(result, content);
			} catch (IOException e) {
				readStatus = 1;
			}
		} else if (args[0] != null && args[0].compareTo("-w") == 0) {
			try {
				wordCount(result, content);
			} catch (IOException e) {
				readStatus = 1;
			}
		} else if (args[0] != null && args[0].compareTo("-l") == 0) {
			try {
				lineCount(result, content);
			} catch (IOException e) {
				readStatus = 1;
			}
		} else if (args[0] != null && args[0].compareTo("-help") == 0) {
			result.append(getHelp());
		} else {
			result.append("Invalid arguments.");
			setStatusCode(1);
		}
		return result.toString();
	}

	private void lineCount(StringBuilder result, String content) throws IOException {
		Charset std = Charset.forName("UTF-8");

		if (content != null)
			result.append(getNewLineCount(content));
		else
			for (int i = 1; i < args.length; i++) {
				content = readFile(args[i], std);
				if (readStatus != 1)
					result.append(getNewLineCount(args[i]));
				else
					result.append(content);
			}
	}

	private void wordCount(StringBuilder result, String content) throws IOException {
		Charset std = Charset.forName("UTF-8");
		if (content != null)
			result.append(getWordCount(content));
		else
			for (int i = 1; i < args.length; i++) {
				//content = readFile(args[i], std);
				if (readStatus != 1)
					result.append(getWordCount(args[i]));
				else
					result.append(content);
			}
	}

	private void characterCount(StringBuilder result, String content) throws IOException {
		Charset std = Charset.forName("UTF-8");

		if (content != null)
			result.append(getCharacterCount(content));
		else
			for (int i = 1; i < args.length; i++) {
				//content = readFile(args[i], std);
				if (readStatus != 1)
					result.append(getCharacterCount(args[i]));
				else
					result.append(content);
			}
	}

	/*
	 * Read content from file
	 */
	public String readFile(String fileName) {
		String filePath;
		readStatus = -1;
		File f = new File(fileName);
		if (f.isAbsolute())
			filePath = fileName;
		else
			filePath = System.getProperty("user.dir") + File.separator + fileName;

		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			readStatus = 0;
			return sb.toString();
		} catch (Exception e) {
			sb.append("word count: open failed: " + fileName + ": No such file or directory.");
			readStatus = 1;
			setStatusCode(1);
		}
		return sb.toString();
	}

	public String readFile(String fileName, Charset encoding) throws IOException {
		String filePath;
		readStatus = -1;
		File f = new File(fileName);
		if (f.isAbsolute())
			filePath = fileName;
		else
			filePath = System.getProperty("user.dir") + File.separator + fileName;

		byte[] encoded = Files.readAllBytes(Paths.get(filePath));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
}