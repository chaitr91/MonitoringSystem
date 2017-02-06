#!/bin/bash
javac Generate.java
java Generate
cat out* > $1
rm -f out*
rm -f *.class
