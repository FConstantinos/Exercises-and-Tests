Description:

This program searches for a string in a number of files and outputs the lines where the string is found, quite similar to grep. Its input is the string to be found and the input files that are present in the folder "input". Its output is located in the "output.txt" file. In each line of the output file, the lines where the string was found are printed on the right and the byte offset of those lines is printed on the left. The byte offset is computed for the concatenation of the input files in their alphabetical order.

Implementation: 

Apache hadoop is used here for the implementation. Each mapper takes an input line. If the string is found, a (key,value) pair is produced, where the value is the line, and the key is the byte offset of the line. The reducers just output this pair to the designated output file. 

Compile: 

To compile the code, run the command "compile_script.sh". Java and hadoop must be installed.

Run:

To run the code, put the input files in the input directory and run the command "run_script.sh" with a string argument that denotes the string to be searched for. The output file can be found in output.txt.  For example, run the following command:

>bash run_script.sh "Lorem ipsum" for the two input files that are given as a test case.  


Notes:

It could be improved if instead of the byte offset, the file name and the line number are given as output.  
