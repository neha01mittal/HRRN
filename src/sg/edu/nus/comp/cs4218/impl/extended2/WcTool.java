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
	private int		readStatus	= -1;
	private int		fileIndex	= -1;
	private File	workingDir;
	Vector<String>	argList		= new Vector<String>();

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
		this.workingDir = workingDir;
		StringBuilder result = new StringBuilder();
		if ((args == null) && (stdin == null || stdin.compareTo("") == 0)) {
			result.append("No arguments and no standard input.");
			return result.toString();
		}

		String content = null;
		if (stdin != null && stdin.compareTo("") != 0) {
			content = stdin;
		}

		for (int i = 0; i < args.length; i++) {
	        String arg = args[i];
	        if (arg.compareTo("-") == 0)
	        	content = stdin;
	        else if (arg.startsWith("-") && arg.compareTo("-m") != 0
	        		&& arg.compareTo("-w") != 0 && arg.compareTo("-l") != 0
	        		&& arg.compareTo("-help") != 0) {
	        	result.append("Invalid arguments.");
	        	return result.toString();
	        }
	    }
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

		if (argList.contains("-help")) {
			result.append(getHelp());
			return result.toString();
		}

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
					result.append("word count: open failed: " + args[i]
							+ ": No such file or directory.");
				}
			}

		return result.toString();
	}

	private void processString(StringBuilder sb, String content) throws IOException {
		if (this.argList.contains("-m")) {
			sb.append("\t");
			characterCount(sb, content);
		}
		if (this.argList.contains("-w")) {
			sb.append("\t");
			wordCount(sb, content);
		}
		if (this.argList.contains("-l")) {
			sb.append("\t");
			lineCount(sb, content);
		}
		if (this.argList.isEmpty())
			generalCount(sb, content);
	}

	private void processFile(StringBuilder sb, String fileName) throws IOException {
		// TODO Auto-generated method stub
		String fileContent = readFile(fileName, Charset.forName("UTF-8"));
		if (this.argList.contains("-m")) {
			sb.append("\t");
			characterCount(sb, fileContent);
		}
		if (this.argList.contains("-w")) {
			sb.append("\t");
			wordCount(sb, fileContent);
		}
		if (this.argList.contains("-l")) {
			sb.append("\t");
			lineCount(sb, fileContent);
		}
		if (this.argList.isEmpty()) {
			generalCount(sb, fileContent);
		}
		sb.append("\t" + fileName);
		sb.append("\n");
	}

	private void generalCount(StringBuilder result, String content)
			throws IOException {
		if (content != null) {
			result.append("\t" + getNewLineCount(content));
			result.append("\t" + getWordCount(content));
			result.append("\t" + getCharacterCount(content));
		}
	}

	private void lineCount(StringBuilder result, String content)
			throws IOException {
		if (content != null)
			result.append(getNewLineCount(content));
	}

	private void wordCount(StringBuilder result, String content)
			throws IOException {
		if (content != null)
			result.append(getWordCount(content));
	}

	private void characterCount(StringBuilder result, String content)
			throws IOException {
		if (content != null)
			result.append(getCharacterCount(content));
	}

	/*
	 * Read content from file
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
			throw new IOException(fileName);
		}
	}
}