#!/bin/bash
if [[ $# != 4 ]]
then
    echo converter.sh '<applet_id> <applet_class> <pkg_id> <pkg_name>'
    exit 1
fi

echo converter -nobanner -exportpath ./export/ -out EXP CAP -applet $1 $2 $4 $3 1.0
JC_PATH=$JC21_HOME/lib/apdutool.jar:$JC21_HOME/lib/apduio.jar:$JC21_HOME/lib/converter.jar:$JC21_HOME/lib/jcwde.jar:$JC21_HOME/lib/scriptgen.jar:$JC21_HOME/lib/offcardverifier.jar:$JC21_HOME/lib/api21.jar:$JC21_HOME/lib/capdump.jar:$CLASSPATH
JFLAGS="-classpath $JC_PATH"

java $JFLAGS com.sun.javacard.converter.Converter -classdir ./applet-classes -nobanner -exportpath $JC21_HOME/api21_export_files:applet-classes/${APPLET} -out EXP JCA CAP -applet $1 $2 $4 $3 1.0

echo

APPLET=$(echo $4 | sed 's/\./\//g')
CAP_DIR=${APPLET}/javacard
CAP_PATH=${CAP_DIR}/$(basename ${APPLET})
echo java -jar lib/captransf.jar $(find $JC21_HOME/api21_export_files -follow -name '*.exp') applet-classes/${CAP_PATH}.cap 
java -jar lib/captransf.jar $(find $JC21_HOME/api21_export_files -follow -name '*.exp') $(find applet-classes -follow -name '*.exp') applet-classes/${CAP_PATH}.cap
echo
echo jar xf applet-classes/${CAP_PATH}.cap.transf
jar xf applet-classes/${CAP_PATH}.cap.transf
echo
echo rm applet-classes/${CAP_PATH}.cap.transf
rm applet-classes/${CAP_PATH}.cap.transf
echo 
echo cat ${CAP_DIR}/Header.cap ${CAP_DIR}/Directory.cap ${CAP_DIR}/Import.cap ${CAP_DIR}/Applet.cap ${CAP_DIR}/Class.cap ${CAP_DIR}/Method.cap ${CAP_DIR}/StaticField.cap ${CAP_DIR}/ConstantPool.cap ${CAP_DIR}/RefLocation.cap ${CAP_DIR}/Descriptor.cap' > applet-classes/'$CAP_PATH.ijc

cat ${CAP_DIR}/Header.cap ${CAP_DIR}/Directory.cap ${CAP_DIR}/Import.cap ${CAP_DIR}/Applet.cap ${CAP_DIR}/Class.cap ${CAP_DIR}/Method.cap ${CAP_DIR}/StaticField.cap ${CAP_DIR}/ConstantPool.cap ${CAP_DIR}/RefLocation.cap ${CAP_DIR}/Descriptor.cap > applet-classes/$CAP_PATH.ijc

echo
echo rm -rf ${CAP_DIR}
#rm -rf ${CAP_DIR}
echo
echo Done

