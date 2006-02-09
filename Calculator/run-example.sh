#!/bin/bash
LD_LIBRARY_PATH=./lib
CLASSPATH=./classes:./applet-classes:./lib/captransf.jar:./lib/base-core.jar:./lib/base-opt.jar:./lib/cryptix-jce-api.jar:./lib/cryptix-jce-provider.jar:./lib/pcsc-wrapper-2.0.jar
export LD_LIBRARY_PATH CLASSPATH
java fr.umlv.corba.calculator.CalculatorClient $*
#java opencard.cflex.test.SimpleStringClient $*
