package ${package};

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

import ${packagePrefix}constants.${className}Constants; //TODO changer

public class ${className}Applet extends Applet {
	
	public ${className}Applet() {
		//TODO
	}
	
	public ${className}Applet(byte buffer[],short offset,byte length) {
		//TODO
		if (buffer[offset] == (byte)0) {
			register();
		}
		else {
			register(buffer, (short)(offset+1) ,(byte)(buffer[offset]));
		}
	}

	public static void install(byte buffer[],short offset,byte length){
		// create a ${className} applet instance (card) *!*
		new ${className}Applet(buffer, offset, length);

	} // end of install method
	
	public void process(APDU apdu) throws ISOException {
		byte[] buffer = apdu.getBuffer();
		
		// Implement a select handler 
		if (selectingApplet()) {
			ISOException.throwIt(ISO7816.SW_NO_ERROR);
		}
		
		if (buffer[ISO7816.OFFSET_CLA] != ${className}Constants.${className?upper_case}_CLA) 
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		
		byte instruction = buffer[ISO7816.OFFSET_INS];
		
		switch (instruction) {
		<#list class.declaredMethods as method>
		case ${className}Constants.${method.name?upper_case}:
			${method.name}(apdu);
			break;
		</#list>
		case ${className}Constants.SELECT:
			select();
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	public boolean select() {
		//TODO
		return true;
	}
	
	<#list class.declaredMethods as method>
	public void ${method.name}(APDU apdu) {
		//TODO complete the method
		return;
	}
	
	</#list>
}
