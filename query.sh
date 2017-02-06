#!/bin/bash
javac Query.java
java Query $1
rm -f *.class
