#!/bin/sh -x
CLASSPATH=../craftbukkit-1.1-R4.jar javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/PickupArrows
mv *.class me/exphc/PickupArrows
jar cf PickupArrows.jar me/ *.yml *.java
