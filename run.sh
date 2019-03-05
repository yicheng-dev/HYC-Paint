OUTPUT_DIR=out/

test -e $OUTPUT_DIR || mkdir $OUTPUT_DIR
javac -d $OUTPUT_DIR $(find src -name "*java")
java -ea -classpath $OUTPUT_DIR main.Main $0 $1
rm -rf $OUTPUT_DIR
