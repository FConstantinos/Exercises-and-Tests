Description:

This program is designed to find the common friends of a two member friend relationship. The input is a file where each line is of the form A -> B C D ..., where B C D ... are friend of A. The output is of the form (A, B) -> C E W ... where C E W are common friends of A and B and A,B are friends

Implementation: 

Apache hadoop is used here for the implementation. Each mapper takes an input line. The keys produced by a single line are all the possible couples between the first individual in the line and her friends. The values of each key is the set of all other friends. The reducer takes the two values of the same key and finds their intersection. An example run would be:

Input:
1 → 2 3 5
2 → 1 3 7
3 → 2 5 7
4 →
5 → 7

Mapper (key,value) pairs:
( (1,2), (3,5) )
( (1,3), (2,5) )
( (1,5), (2,3) )
( (1,2), (3,7) )
( (2,3), (1,7) )
( (2,7), (1,3) )
( (2,3), (5,7) )
( (3,5), (2,7) )
( (3,7), (2,5) )

Reducer output:
( (1,2), (3) )
( (2,3), (7) )

Compile: 

To compile the code, run the command "compile_script.sh". Java and hadoop must be installed.

Run:

To run the code, put the input files in the input directory and run the command "run_script.sh" The output file can be found in output.txt. 


Notes:

It could be updated for finding the common friends of any pair of individuals, not necessarily friends between them.


