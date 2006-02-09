./compile.sh applet-src/fr/umlv/corba/calculator/applet/CalculatorApplet.java

echo "conversion de l'applet Calculator"
./converter.sh 0x00:0x01:0x02:0x03:0x04:0x05 fr.umlv.corba.calculator.applet.CalculatorApplet 0x00:0x01:0x02:0x03:0x04 fr.umlv.corba.calculator.applet

