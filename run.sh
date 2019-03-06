#!/usr/bin/env bash
OUTPUT_DIR=out/

if [ -n "$0" ]; then 
	echo "$0 or $1 is empty" 
	exit 0 
fi 

test -e $OUTPUT_DIR || mkdir $OUTPUT_DIR
javac -d $OUTPUT_DIR $(find src -name "*java")
java -ea -classpath $OUTPUT_DIR main.Main $0 $1
rm -rf $OUTPUT_DIR
