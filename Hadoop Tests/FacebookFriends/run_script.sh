#!/bin/bash
NAME="FacebookFriends"
INPUT_DIR="eap_inp"
OUTPUT_DIR="eap_out"

rm -r $OUTPUT_DIR
hadoop jar $NAME".jar" $NAME $INPUT_DIR $OUTPUT_DIR
cp $OUTPUT_DIR/part-00000 output.txt

