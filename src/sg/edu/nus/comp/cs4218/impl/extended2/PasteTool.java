package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/*
 * 
 * paste : writes to standard output lines consisting of sequentially corresponding 
 * lines of each given file, separated by a TAB character
 * 
 * Command Format - paste [OPTIONS] [FILE]
 * 		FILE - Name of the file, when no file is present (denoted by "-") use standard input OPTIONS
 * 		-s : paste one file at a time instead of in parallel
 * 		-d DELIM: Use characters from the DELIM instead of TAB character
 * 		-help : Brief information about supported options
 */

public class PasteTool extends ATool implements IPasteTool {

	public PasteTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String pasteSerial(String[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		return null;
	}
}
