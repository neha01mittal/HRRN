package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
/**
 * Specifications for Paste	
 * 	paste [options] [file1 ..]
 * 	paste file1 file2: prints contents of files in parallel on the console
 * 	paste -d delim file1 file2: file content printed in parallel separated by delimiter.
 * 	paste -s file1 file2 : paste appends the data in serial rather than in parallel.
 *  command1 | paste : The output of command1 is treated as file contents to be used by paste
 *  command1 | paste [options] : stdin used as file contents
 *  command1 | paste - [file1 ..] : "-" is replaced by file contents
 *  command1 | paste [file1 ..] - : "-" is replaced by file contents
 *  command1 | paste -s - [file1 ..] 
 *  command1 | paste -d "," [file1 ..] - 
 * @author ranjini
 *
 */
public class PasteTool extends ATool implements IPasteTool {

	private final List<String> argList;
	private final List<String> inputList;
	private File directory;
	private static File stdinFile;

	public PasteTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		setStatusCode(0);
		argList = new ArrayList<String>();
		inputList = new ArrayList<String>();
		directory = null;
		stdinFile = new File("");
		
	}

	@Override
	public String pasteSerial(String[] input) {
		// TODO Auto-generated method stub
		String content = "";
		String[] listOfFileContents = new String[input.length];
		int numFiles = 0;
		try{
			for (numFiles = 0; numFiles < input.length; numFiles++) {
				File file = new File(input[numFiles]);
				if (!file.isAbsolute()) {
					if (directory == null){
						setStatusCode(1);
						return "Error: No such file or directory\n";
					}						
					file = new File(directory, input[numFiles]);
				}
				if (file.exists() & file.canRead()) {
					listOfFileContents[numFiles] = getStringForFile(file);
				}
				else{
					setStatusCode(1);
					return "Error: No such file or directory\n";
				}
			}
			for (String f : listOfFileContents) {
				List<String> inputLineList = Arrays.asList(f.split("\n"));
				for (int a = 0; a < inputLineList.size(); a++) {
					content = content + inputLineList.get(a);
					if (a < (inputLineList.size() - 1))
						content = content + "\t";
				}
				if (content.endsWith("\t")) {
					int size = content.length();
					content = content.substring(0, size - 1);
				}
				content = content + "\n";
			}
			if (content.endsWith("\n")) {
				int size = content.length();
				content = content.substring(0, size - 1);
			}
			return content;
		}catch(Exception e){
			setStatusCode(1);
			return "Wrong command";
		}
	}

	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		// TODO Auto-generated method stub
		String content = "";
		int highest = 0;
		int numFiles = 0;
		String[] listOfFileContents = new String[input.length];
		try{
			
			for (numFiles = 0; numFiles < input.length; numFiles++) {
				File file = new File(input[numFiles]);
				if (!file.isAbsolute()) {
					file = new File(directory, input[numFiles]);
				}
				if (file.exists() & file.canRead()) {
					listOfFileContents[numFiles] = getStringForFile(file);
				}
				else{
					setStatusCode(1);
					return "Error: No such file or directory\n";
				}
			}
	
			String[][] matrix = new String[numFiles][];
			int x = 0;
			int y = 0;
			for (String f : listOfFileContents) {
				List<String> inputLineList = Arrays.asList(f.split("\n"));
				matrix[x] = new String[inputLineList.size()];
				for (String fileLine : inputLineList) {
					matrix[x][y] = fileLine;
					y++;
				}
				x++;
				if (y > highest)
					highest = y;
				y = 0;
			}
			for (y = 0; y < highest; y++) {
				for (x = 0; x < numFiles; x++) {
					if (y < matrix[x].length) {
						content = content + matrix[x][y];
						if ((x + 1) < numFiles)
							content = content + delim;
					}
	
				}
				if (content.endsWith(delim)) {
					int size = content.length();
					int sizeDelim = delim.length();
					content = content.substring(0, size - sizeDelim);
				}
				content = content + "\n";
			}
			if (content.endsWith("\n")) {
				int size = content.length();
				content = content.substring(0, size - 1);
			}
			return content;
		}catch(Exception e){
			setStatusCode(1);
			return "Wrong command";
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		String helpString = "-s : paste one file at a time instead of in parallel\t" + " -d DELIM: Use characters from the DELIM instead of TAB character\t"
				+ " -help : Brief information about supported options";
		return helpString;
	}

	@Override
	public String execute(File workingDir, String stdin) {

		// TODO Auto-generated method stub
		directory = workingDir;
		String characterList = "";

		if (args == null || args.length == 0) {
			if (stdin == null ||  stdin == "") {
				setStatusCode(1);
				if(stdinFile.exists()){
					stdinFile.delete();
				}
				return "No arguments and no standard input.";
			}
		}
		// split arguments and inputs
		int x = 0;
		int found = 0;
		if(args!= null ){
		for (String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.equals("-s") || arg.equals("-d") || arg.equals("-help"))
					argList.add(arg);
				else if (arg.equals("-")){
					found = 1;
					if(stdin != null && stdin != ""){
						found = x;
						Writer writer = null;
						try {
							stdinFile = new File(directory, "StdinContentClass.txt");
							writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(stdinFile.getAbsolutePath().toString()), "utf-8"));
							writer.write(stdin);
						} catch (IOException ex) {
							// report
						} finally {
							try {
								writer.close();
							} catch (Exception ex) {
							}
						}
					}
					inputList.add(stdinFile.getAbsolutePath())	;
				}
				else {
					setStatusCode(1);
					if(stdinFile.exists()){
						stdinFile.delete();
					}
					return "Wrong command";
				}

			} else {
				inputList.add(arg);
			}
			x++;
		}
		}
		if(inputList.size() == 0 && found == 0){
			if(stdin != null && stdin != ""){
				found = x;
				Writer writer = null;
				try {
					stdinFile = new File(directory, "StdinContentClass.txt");
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(stdinFile.getAbsolutePath().toString()), "utf-8"));
					writer.write(stdin);
				} catch (IOException ex) {
					// report
				} finally {
					try {
						writer.close();
					} catch (Exception ex) {
					}
				}
			}
			inputList.add(stdinFile.getAbsolutePath());
		}
		if (argList.contains("-help")) {
			setStatusCode(0);
			if(stdinFile.exists()){
				stdinFile.delete();
			}
			return getHelp();
		} else if (argList.size() == 0) {
			String[] listOfFiles = new String[inputList.size()];
			for (int a = 0; a < inputList.size(); a++) {
				listOfFiles[a] = inputList.get(a);
			}
			String result = pasteUseDelimiter("\t", listOfFiles);
			characterList = result;
		} else if (argList.get(0).equals("-s")) {
			String[] listOfFiles = new String[inputList.size()];
			for (int a = 0; a < inputList.size(); a++) {
				listOfFiles[a] = inputList.get(a);
			}
			String result = pasteSerial(listOfFiles);
			characterList = result;
		} else if (argList.get(0).equals("-d")) {
			String[] listOfFiles = new String[inputList.size() - 1];
			for (int a = 1; a < inputList.size(); a++) {
				listOfFiles[a - 1] = inputList.get(a);
			}
			String result = pasteUseDelimiter(inputList.get(0), listOfFiles);
			characterList = result;
		} else {
			characterList = "Wrong command";
			setStatusCode(1);
			if(stdinFile.exists()){
				stdinFile.delete();
			}
			return characterList;
		}
		setStatusCode(0);
		if(stdinFile.exists()){
			stdinFile.delete();
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
