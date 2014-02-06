package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * echo writes its arguments separated by blanks and terminated by a newline on
 * the standard output
 */
public class EchoTool extends ATool implements IEchoTool {

	public EchoTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		String userInput = "";
		boolean error = false;
		if (args != null) {
			// print the user input to stdout
			for (String arg : args) {
				// TODO handle printing special character (esp quotes)
				if (arg.contains("$")) {
					error = true;
					break;
				}
				userInput += arg + " ";
			}

			if (error == true) {
				return echo("Error in input. Cannot enter $ sign");

			} else {
				setStatusCode(0);
			}

			// REMOVE THE EXTRA LAST SPACE
			if (userInput.length() >= 1 && userInput.charAt(userInput.length() - 1) == ' ')
				userInput = userInput.substring(0, userInput.length() - 1);

			userInput += "\n";
			return echo(userInput);
		} else if (!stdin.equals("")) {
			// TODO
			// does nothing
		}
		return "\n";
	}

	@Override
	public String echo(String toEcho) {
		// TODO Auto-generated method stub
		return toEcho;
	}
}
