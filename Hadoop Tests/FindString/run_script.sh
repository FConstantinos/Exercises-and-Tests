#!/bin/bash
NAME="FindString"
INPUT_DIR="eap_inp"
OUTPUT_DIR="eap_out"

rm -r $OUTPUT_DIR
hadoop jar $NAME".jar" $NAME $INPUT_DIR $OUTPUT_DIR ''"$1"''
cp eap_out/part-00000 output.txt

