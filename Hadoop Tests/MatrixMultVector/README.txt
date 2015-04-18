Description:

This program performs matrix vector multiplication. The input vector is given in the vector.txt file. The input matrix is given in the Matrix folder, comprised of multiple files. The vector.txt is of the form:

# of rows
row 1 element
row 2 element
...

The matrix files are of the form:
value -> row column

The matrix files are of the form:
value -> row column

The output can be found on the output.txt file and is of the form:

row 1 result
row 2 result
...

Implementation: 

Apache hadoop is used here for the implementation. Each mapper takes an input line, which is a simple element of the matrix. Then it multiplies this element with the corresponding vector row (the column of the element). The key value pair produced is ( row, result) where row is the row of the matrix and result is the result of the multiplication. The reducer then adds all values with the same key (vector row) and outputs that to the resulting vector.

Vector:
3
1
2
3

Input:
element → row column
1 -> 1 1
2 -> 1 2
1 -> 2 2
1 -> 3 3
1 -> 3 2

Mapper (key,value) pairs:
( 1, 1 )
( 1, 4 )
( 2, 2 )
( 3, 3 )
( 3, 2 )

Reducer output:
( 1, 5)
( 2, 2)
( 3, 5)

Compile: 

To compile the code, run the command "compile_script.sh". Java and hadoop must be installed.

Run:

To run the code, put the input files in the input directory and run the command "run_script.sh" The output file can be found in output.txt. 




