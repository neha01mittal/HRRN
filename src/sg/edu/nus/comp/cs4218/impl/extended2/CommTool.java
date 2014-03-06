package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/*
 * 
 * comm : Compares two sorted files line by line. With no options, produce three-column output. 
 * 		 Column one contains lines unique to FILE1, column two contains lines unique to FILE2, 
 * 		 and column three contains lines common to both files.
 *	
 *	Command Format - comm [OPTIONS] FILE1 FILE2
 *	FILE1 - Name of the file 1
 *	FILE2 - Name of the file 2
 *		-c : check that the input is correctly sorted
 *      -d : do not check that the input is correctly sorted
 *      -help : Brief information about supported options
 */

public class CommTool extends ATool implements ICommTool {

	private static final String	INVALID_COMMAND	= "Invalid command";
	private static final String	NOT_SORTED		= "Not Sorted!";
	private boolean				sortFlag		= false;
	String						currentLine1	= "";
	String						currentLine2	= "";

	// check this ^^^
	public CommTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String compareFiles(String input1, String input2) {
		// TODO Auto-generated method stub
		return (input1.compareTo(input2) < 0 ? "-1"
				: (input1.compareTo(input2) == 0) ? "0" : "1");

	}

	@Override
	public String compareFilesCheckSortStatus(String input1, String input2) {
		// TODO Auto-generated method stub
		if (input1 == null && input2 == null)
			return "-1";
		if (input1 == null) {
			if (input2.compareTo(currentLine2) > 0)
				return "-1";
			else
				return NOT_SORTED;
		} else if (input2 == null) {
			if (input1.compareTo(currentLine1) > 0)
				return "-1";
			else
				return NOT_SORTED;
		} else if (input1.compareTo(currentLine1) > 0
				&& input2.compareTo(currentLine2) > 0)
			return "-1";
		else
			return NOT_SORTED;
	}

	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		// TODO Auto-generated method stub
		return compareFiles(input1, input2);
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		String result = "comm : Compares two sorted files line by line. With no options, produce three-column output."
				+ "\nColumn one contains lines unique to FILE1, column two contains lines unique to FILE2,"
				+ "\nand column three contains lines common to both files."
				+ "\n"
				+ "\nCommand Format - comm [OPTIONS] FILE1 FILE2"
				+ "\nFILE1 - Name of the file 1"
				+ "\nFILE2 - Name of the file 2"
				+ "\n-c : check that the FILE1 is correctly sorted (increasing order) and also FILE2"
				+ "\n if it is not correctly sorted, 'Not Sorted!' is shown"
				+ "\n-d : do not check that the FILE1 and FILE2 are correctly sorted"
				+ "\n-help : Brief information about supported options";
		setStatusCode(0);
		return result;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		String file1 = "";
		String file2 = "";
		String operation = parse();
		if (operation.equals(INVALID_COMMAND))
			return INVALID_COMMAND;

		if (args != null && args.length > 0) {
			if (operation.equalsIgnoreCase("help")) {
				return getHelp();
			} else if (operation.equalsIgnoreCase("d") && args.length > 2) {
				// do not care about sorting
				file1 = workingDir + File.separator + args[args.length - 2];
				file2 = workingDir + File.separator + args[args.length - 1];
			} else if (operation.equalsIgnoreCase("c") && args.length > 2) {
				sortFlag = true;
				file1 = workingDir + File.separator + args[args.length - 2];
				file2 = workingDir + File.separator + args[args.length - 1];
			} else if (args.length == 2 && operation.equals("")) {
				sortFlag = false;
				file1 = workingDir + File.separator + args[0];
				file2 = workingDir + File.separator + args[1];
			}

			File f1 = new File(file1);
			File f2 = new File(file2);
			List<String> file1Data = readFile(f1);
			List<String> file2Data = readFile(f2);

			if (file1Data == null || file2Data == null)
				return INVALID_COMMAND;

			String line1 = "";
			String line2 = "";
			String expectedOutput = "";

			int i = 0;
			int j = 0;
			while (i < file1Data.size() || j < file2Data.size()) {
				if (i >= file1Data.size()) {
					line2 = file2Data.get(j);
					if (sortFlag
							&& !compareFilesCheckSortStatus(null, line2)
									.equals("-1")) {
						expectedOutput += NOT_SORTED;
						break;
					}
					currentLine2 = line2;
					expectedOutput += "\t\t" + line2;
					j++;
				} else if (j >= file2Data.size()) {
					line1 = file1Data.get(i);
					if (sortFlag
							&& !compareFilesCheckSortStatus(line1, null)
									.equals("-1")) {
						expectedOutput += NOT_SORTED;
						break;
					}
					currentLine1 = line1;
					expectedOutput += line1;
					i++;
				} else {
					line1 = file1Data.get(i);
					line2 = file2Data.get(j);
					if (sortFlag) {
						if (!compareFilesCheckSortStatus(line1, line2).equals(
								"-1")) {
							expectedOutput += NOT_SORTED;
							break;
						}
						currentLine1 = line1;
						currentLine2 = line2;
					}
					int comparison = Integer
							.parseInt(compareFiles(line1, line2));
					if (comparison == 0)
						expectedOutput += "\t\t\t\t" + line1;
					else if (comparison < 0)
						expectedOutput += line1 + "\n\t\t" + line2;
					else
						expectedOutput += "\t\t" + line2 + "\n" + line1;
					i++;
					j++;

				}

				expectedOutput += "\n";
			}
			if (expectedOutput.endsWith("\n"))
				expectedOutput = expectedOutput.substring(0,
						expectedOutput.length() - 1);

			setStatusCode(0);
			// System.out.println(expectedOutput);
			return expectedOutput;

		}
		return INVALID_COMMAND;
	}

	public String parse() {
		String parsed = "";
		int count = 0;
		int i = args.length - 1;
		while (i >= 0) {
			if (args[i].startsWith("-")) {
				// help gets priority // if not help, the first one gets
				// priority
				String option = args[i].substring(1);
				if (option.equalsIgnoreCase("help")) {
					parsed = option;
					break;
				}
				count++;
				parsed = option;
			}

			if (count > 1) {
				return INVALID_COMMAND;
			}
			i--;
		}
		return parsed;
	}

	private List<String> readFile(File f) {
		List<String> expectedOutput = new ArrayList<String>();
		if (f.isFile() && f.canRead()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					expectedOutput.add(line);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return expectedOutput;
		}
		return null;
	}
}