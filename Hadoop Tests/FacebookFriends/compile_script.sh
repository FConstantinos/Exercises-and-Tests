#!/bin/bash
NAME="FacebookFriends"
CONCAT_DIR="input"
INPUT_FILE="FacebookFriends.txt"
INPUT_DIR="eap_inp"

rm -r $NAME"_classes"
mkdir $NAME"_classes"
javac -d $NAME"_classes/" $NAME".java"
jar -cvf $NAME".jar" -C $NAME"_classes/" .
rm $INPUT_DIR"/"$INPUT_FILE
find $CONCAT_DIR -type f -print0 | xargs -0 cat > $INPUT_DIR"/"$INPUT_FILE

