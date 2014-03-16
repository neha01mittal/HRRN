Specifications for each class

Insert in the relevant section:

1. Shell

@usage
[CURRENT DIR] $: command options input. There's no strict sequence requirements for options and input.
@note
The input arguments or path can be relative or absolute, in quotes or without quotes, with backslash or front slash. Take special consideration for using characters like /, ', ", |, they need to be escaped properly using quotes or back slash. The termination function is by entering ctrl-z to terminate a running tool, the char has to be input with enter key hit. The shell will try it’s best to stop the running thread, if it’s really not able to stop the thread, the program will stop for safety. User are not allowed to use ‘~’ to denote home directory. Shell does not support any type of logic operation like >.
@success
It will print the returning string, or nothing if returned null, or a blank line if returned empty string.
@exceptions
If any error occurs where status code not equal to zero, the error will print through stderr stream.


2. Cat

@usage  cat [ - ] [string | path]
@options
Cat [filename]          contents of file
Cat [filename1] [filename2] contents of both files
Cat - “text text”       output- text text, Anything preceded by ‘-’ is printed as it is
@note
If files extensions are such that they not readable properly (eg: pdf), it might display garbage values. ls | cat and ls | cat - will print as expected (contents of that directory or in other words standard input it receives from the ‘from’ tool). Prioritizes args over stdin i.e. if there are args in front, it will execute them and ignore stdin
eg: ls | cat file1.txt
will print the contents of file1 and not the contents of current directory@note
@success
@exceptions



3. Echo

Prints the user input as it is.
@usage  echo [ | string]
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


4. Grep

Searches for pattern in a file
@usage  grep [-A | -B | -C | -c | -o | -v | -help] pattern [path]
@options
grep -A NUM filename: Print NUM lines of trailing context after matching lines
grep -B NUM filename: Print NUM lines of leading context before matching lines
grep -C NUM filename: Print NUM lines of output context
grep -c “pattern” filename : Print a count of matching lines with pattern
grep -c ‘“pattern”’ filename : Print a count of matching lines with “pattern”(pattern surrounded by double quotes)
grep -c “pattern” file1 file2 : Print a count of matching lines containing pattern for both files
grep -o “pattern” filename: Show only the part of a matching line that matches PATTERN
grep -v “pattern” filename: Select non-matching (instead of matching) lines
grep -help : Brief information about supported options
grep -o -v “pattern filename: Provides the conjunction of results from both options
grep -<any option> ‘pattern’ filename: Provides the same output as compared to pattern surrounded by double quotes
grep -<any option> <pattern with one word> filename: Provides the same output even without surrounding quotes if the pattern consists of one word
grep -<any option> “pattern” file1 file2: Provides the output after executing the command on both files
grep filename: Prints the command since no option was provided
@note
@success
@exceptions



4. Move

Move a file/folder to a given location
@usage  move [path] [path] … [path to folder]
@options
move file1 file2 - Moves file1 contents into file2
move /../file1 file2 - Moves file1 contents into file2
move "file1" "file2" - Moves file1 contents into file2
move file1 newfile - Creates newfile and moves file1 contents into newfile
move file1 folder - Moves file1 into folder
move file1 newfolder - Moves file1 into newfolder
move folder1 folder2 - Moves contents of folder1 into folder2
move folder1 newfolder - Moves folder1 into newfolder
move file1 file2 file3 folder1 - Moves file1, file2, file3 in folder1
move folder1 folder2 file3 newfolder - Moves folder1, folder2, file3 into newfolder
@note
@success
@exceptions




5. Delete

Delete a file or folder
@usage  delete [path]
@options
delete file1  - Deletes file1
delete relativepath1  - Converts relativepath to absolutepath and deletes file1
delete /../file1  - Deletes file1
delete "file1"  - Deletes file1
delete newfile - Does nothing
delete folder1 - Deletes folder and all its contents
@note
@success
@exceptions




6. Copy

copy a file/folder to a given location
@usage  copy [path1] [path2] … [path to folder]
@options
copy file1 file2        Copies file1 contents into file2
copy /../file1 file2        Copies file1 contents into file2
copy "file1" "file2"        Copies file1 contents into file2
copy file1 newfile          Creates newfile and copies file1 contents into newfile
copy file1 folder       Copies file1 contents into folder
copy file1 newfolder        Creates newfolder and copies file1 contents into newfolder
copy folder1 folder2        Copies contents of folder1 into folder2
copy folder1 newfolder  Creates newfolder and copies folder1 contents into newfolder
copy file1 file2 file3 folder1  Copies/Replaces file1, file2, file3 in folder1
copy folder1 folder2 file3 newfolder - Creates and copies folder1, folder2, file3 into newfolder
@note
@success
@exceptions



7. PWD

Show the current directory
@usage  pwd
@note
pwd tool does not take in any arguments or stdin, thus it could not be piped to. It will set the initial path to the directory where the program start.
@success
    return a string showing the absolute path of current working directory.
@exceptions
    Cannot find working directory



8. Ls

Lists all the files and subdirectories in the current directory or provided class

@usage  ls [-a | -R] [path]
@options
ls :        Lists all files in current directory.
ls [path] :     Lists all files in the given path.
ls -a :         Lists all files including hidden files in current directory.
ls -a [path] :  Lists all files including hidden files in the given path
ls -R :         Lists all the files (absolute path) in the current directory and subdirectories
ls -R [path] :  Lists all the files (absolute path) in the given path.
ls -a -R :  Combination of functions above
ls file :       Prints file path if it exists in the file system.
@note
 [path] could be either an absolute file or a relative path. If multiple path is given as arguments, only the first argument will be entertained. Ls tool does not allow std, thus can not be pipe to. The recursive variable is printed in absolute path to show the hierarchical structure.
@success
    return the list of file names or path in the
@exceptions
    invalid input
path not exist
retrieve file list error





10. Cd

 Changes directory to a given location

@usage  cd [path]
@options
cd      Changes to the home directory.
cd [path] : Changes current path to the new path.
cd [.. | .] :   Changes to parent directory or stay at current directory.
@note
 [path] could be either an absolute file or a relative path. If multiple path is given as arguments, only the first argument will be entertained. Cd does not allow std, thus can not be pipe to. Cd tool will always try to return the Canonical Path of the new directory which is the most simplified and recognizable. It does not allow user to use ‘~’ to denote home directory.
@success
    returns an empty string, but will not create a new line in the output.
@exceptions
    invalid input
path not exist
path is not directory




11. Sort

 Check or sort one or more files
@usage
sort [options] [path]
@options
sort [path] :   sort the file and paste out the result.
sort -c  [path] :    check if the file is sorted or not, if not show the un-sorted line.
sort  -help : Brief information about supported options
@note
[path] could be either an absolute file or a relative path. If multiple path is given as arguments, they will be append together and sort as one file. Stdin must  be denoted as “-” when using with other file names, otherwise stdin is not entertained. You cannot use two "-" in the command.
@success
    returns the corresponding result as differed by functions.
@exceptions
    invalid input
path not exist
path is not file




12. Uniq

Return unique lines of one or more files
@usage
uniq [options] [path]
@options
uniq [path] :   sort the file and paste out the result.
uniq -i [path]  : Ignore differences in case when comparing lines.
uniq -f [NUM]  [path] :  Skips NUM fields on each line before checking for uniqueness. Use a null string for comparison if a line has fewer than n fields. Fields are sequences of non-space non-tab characters that are separated from each other by at least one space or tab.
uniq -help : Brief information about supported options
@note
[path] could be either an absolute file or a relative path. If multiple path is given as arguments, they will be append together and sort as one file. Stdin must  be denoted as “-” when using with other file names, otherwise stdin is not entertained. You can only use either a file or stdin. It does not work for multiple files.
@success
    returns the corresponding result as differed by functions.
@exceptions
    invalid input
path not exist
path is not file

13. Cut

Returns characters at specified positions from every line in a file
@usage
cut [options] [character_positions] [path]
cut [path]: prints all the file contents on the console
@options
cut -c 1,2-4,5 [path] : prints all the characters at positions from each line.
cut -d delim -f 1,2-4,5 [path]: separates characters by delimiter and prints    
command1 | cut : The output of command1 is treated as file contents to be used by cut
command1 | cut [options] : stdin used as file contents
command1 | cut - : "-" is replaced by file contents
command1 | cut -c [character_positions] - 
command1 | cut -d "," -f [character_positions]
@note
[path] could be either an absolute file or a relative path. Stdin must  be denoted as “-” when using with other file names, otherwise stdin is not entertained. 
@success
    returns the corresponding result as differed by functions.
@exceptions
    Invalid command
path does not exist
invalid options
	No arguments and no standard input.
missing arguments and stdin

14. Paste

returns contents of files in parallel on the console
@usage
paste [options] [file1 ..]

@options
paste -d delim [file1 ..]: file content printed in parallel separated by delimiter.
paste -s [file1 ..] : paste appends the data in serial rather than in parallel.
command1 | paste : The output of command1 is treated as file contents to be used by paste
command1 | paste [options] : stdin used as file contents
command1 | paste - [file1 ..] : "-" is replaced by file contents
command1 | paste [file1 ..] - : "-" is replaced by file contents
command1 | paste -s - [file1 ..] 
command1 | paste -d "," [file1 ..] - 
@note
[path] could be either an absolute file or a relative path. Stdin must  be denoted as “-” when using with other file names, otherwise stdin is not entertained. 
@success
    returns the corresponding result as differed by functions.
@exceptions
    Wrong command
invalid options
	Error: No such file or directory
path does not exist
	




