#!/bin/bash
LD_LIBRARY_PATH=./lib
CLASSPATH=./applet-classes:./classes:./lib/captransf.jar:./lib/base-core.jar:./lib/base-opt.jar:./lib/cryptix-jce-api.jar:./lib/cryptix-jce-provider.jar:./lib/pcsc-wrapper-2.0.jar
export LD_LIBRARY_PATH CLASSPATH
#java smartcard.OCF.app.LoadCFlexAccess32 $*
$JAVA_HOME/bin/java opencard.cflex.app.LoadCFlexAccess32 $*
