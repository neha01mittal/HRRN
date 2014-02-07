package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
  echo writes its arguments separated by blanks and terminated by a newline on
  the standard output
 Prints the user input as it is. 
@usage	echo [ | string]
@options
echo word: Prints word
echo “word”: Prints word
echo “”“word”””: Prints word
echo ‘’’word’’’: Prints word
echo ‘“word”’: Prints “word” (To look for word in double quotes, it needs to escaped by single quotes before and after the double quotes)
echo word\n : Prints echo word\n
echo word            word2 :Prints word word2

@note
Removes the first enclosing (single and double) quotes pair- it finds the first matching pair and removes them. If there is an unmatched quote, it just prints it. Does not escapes any escape sequences like \n \t. If the command is followed by no input, the output with a new line. 
@success
@exceptions
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
