#!/bin/sh -x
CLASSPATH=../craftbukkit-1.2.5-R1.0.jar javac *.java -Xlint:unchecked -Xlint:deprecation
rm -rf me 
mkdir -p me/exphc/PickupArrows
mv *.class me/exphc/PickupArrows
jar cf PickupArrows.jar me/ *.yml *.java LICENSE README.md ChangeLog
