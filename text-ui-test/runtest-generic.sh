#!/usr/bin/env bash

# Check if correct number of arguments provided
if [ $# -ne 2 ]; then
    echo "Usage: $0 <input-file> <expected-file>"
    echo "Example: $0 input.txt EXPECTED.TXT"
    exit 1
fi

INPUT_FILE=$1
EXPECTED_FILE=$2
ACTUAL_FILE="ACTUAL-$(basename "$INPUT_FILE" .txt).TXT"

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./$ACTUAL_FILE" ]
then
    rm "$ACTUAL_FILE"
fi

# delete data directory from previous run to ensure clean test state
if [ -d "data" ]
then
    rm -rf data
fi

# compile the code into the bin folder, terminates if error occurred
javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/*.java
if [ $? -ne 0 ]
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input file and redirect the output to the actual file
java -classpath ../bin Duke < "$INPUT_FILE" > "$ACTUAL_FILE"

# compare the output to the expected output
diff "$ACTUAL_FILE" "$EXPECTED_FILE"
if [ $? -eq 0 ]
then
    echo "Test result: PASSED ($INPUT_FILE)"
    exit 0
else
    echo "Test result: FAILED ($INPUT_FILE)"
    exit 1
fi