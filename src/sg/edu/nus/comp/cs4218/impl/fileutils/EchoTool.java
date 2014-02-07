package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * echo writes its arguments separated by blanks and terminated by a newline on
 * the standard output
 * 
 */
public class EchoTool extends ATool implements IEchoTool {

	private final List<String> inputList;

	public EchoTool(String[] arguments) {
		super(arguments);
		setStatusCode(1);
		inputList = new ArrayList<String>();
	}

	@Override
	public String execute(File workingDir, String stdin) {

		// check for argument number
		if (args == null || args.length < 1) {
			setStatusCode(0);
			return "";
		} else {
			// there's no difference between arguments and inputs
			for (String arg : args) {
				inputList.add(arg);
			}
		}

		String userInput = "";
		for (String arg : inputList) {
			userInput += echo(arg);
		}

		setStatusCode(0);
		return userInput.trim();
	}

	@Override
	public String echo(String toEcho) {
		return toEcho + " ";
	}
}
