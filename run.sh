#!/usr/bin/env bash
OUTPUT_DIR=out/

test -e $OUTPUT_DIR || mkdir $OUTPUT_DIR
javac -d $OUTPUT_DIR $(find src -name "*java")

if [ -n "$1" -a -n "$2" -a -n "$3" ]; then
    echo "Command-line Interface mode on..."
	java -ea -classpath $OUTPUT_DIR main.Main $1 $2 $3
else
    echo "Graphical User Interface mode on..."
    java -ea -classpath $OUTPUT_DIR main.Main
fi

rm -rf $OUTPUT_DIR
