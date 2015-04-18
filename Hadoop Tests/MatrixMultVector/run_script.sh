#!/bin/bash
NAME="MatrixMultVector"
INPUT_DIR="eap_inp"
OUTPUT_DIR="eap_out"

rm -r $OUTPUT_DIR
hadoop jar $NAME".jar" $NAME $INPUT_DIR $OUTPUT_DIR < "vector.txt"
cp $OUTPUT_DIR/part-00000 output.txt
