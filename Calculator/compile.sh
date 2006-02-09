#!/bin/bash

echo javac -d applet-classes -classpath $JC21_HOME/lib/api21.jar -target 1.1 -source 1.3 -g -encoding ISO8859-1 $*
javac -d applet-classes -sourcepath applet-src -classpath $JC21_HOME/lib/api21.jar -target 1.1 -source 1.3 -g -encoding ISO8859-1 $*
echo Done


