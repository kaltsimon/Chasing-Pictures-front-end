#!/bin/bash
# Usage: util/count.sh

java_lines=$(for f in $(find . -name "*.java" | grep -v "build"); do cat "$f"; done | wc -l | tr -d '[:space:]')
xml_lines=$(for f in $(find . -name "*.xml" | grep -v -e "build" -e ".idea"); do cat "$f"; done | wc -l | tr -d '[:space:]')

echo "$java_lines lines of Java code"
echo "$xml_lines lines of XML definitions"
