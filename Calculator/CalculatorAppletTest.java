package fr.umlv.corba.calculator.applet;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

public class CalculatorAppletTest extends Applet implements Calculator {
	
	//  This applet is designed to respond to the following
	//  class of instructions.
	
	final static byte CALCULATOR_CLA = (byte)0xA0;
	
	final static byte SET = (byte)0x10;
	final static byte GET = (byte)0x20;
	final static byte ADD = (byte)0x30;
	final static byte MINUS = (byte)0x40;
	final static byte MULT = (byte)0x50;
	final static byte DIV = (byte)0x60;
	final static byte SELECT = (byte) 0xA4;

	private static CalculatorAppletTest userApplet;
	
	private Stack stack;
	
	public CalculatorAppletTest() {
		stack = new ArrayStack();
	}
	
	public CalculatorAppletTest(byte buffer[],short offset,byte length) {
		stack = new ArrayStack();
		
		if (buffer[offset] == (byte)0) {
			register();
		}
		else {
			register(buffer, (short)(offset+1) ,(byte)(buffer[offset]));
		}
	}

	public static void install(byte buffer[],short offset,byte length){
		
		// create a Calculator applet instance (card) *!*
		//new CalculatorAppletTest(buffer, offset, length);

		// create a Calculator applet instance (Simulator) *!*
		userApplet = new CalculatorAppletTest();

	} // end of install method
	
	public void process(APDU apdu) throws ISOException {
		byte[] buffer = apdu.getBuffer();
		
		// Implement a select handler 
		if (selectingApplet()) {
			ISOException.throwIt(ISO7816.SW_NO_ERROR);
		}
		
		if (buffer[ISO7816.OFFSET_CLA] != CALCULATOR_CLA) 
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		
		byte instruction = buffer[ISO7816.OFFSET_INS];
		
		switch (instruction) {
		case SET:
			push(apdu);
			break;
		case GET:
			pop(apdu);
			break;
		case ADD:
			add(apdu);
			break;
		case MINUS:
			minus(apdu);
			break;
		case MULT:
			mult(apdu);
			break;
		case DIV:
			div(apdu);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	public boolean select() {
		//vider la pile
		userApplet.stack.clear();
		return true;
	}
	
	public void pop(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		
		//byte byteRead = buffer[ISO7816.OFFSET_LC];
		
		short value = userApplet.stack.pop();
		byte [] TheBuffer = ShortToBytePair(value);
		
		/*if (byteRead == (byte)0) {
			ISOException.throwIt((short)(0x6200 + TheBuffer));
		}*/
		
		// inform system that the applet has finished processing
		// the command and the system should now prepare to 
		// construct a response APDU which contains data field
		apdu.setOutgoing(); 

		// indicate the number of bytes in the data field
		apdu.setOutgoingLength((byte)TheBuffer.length); 

		// move the data into the APDU buffer starting at offset 0		
		for (byte index = 0; index <= TheBuffer.length; index++) 
			buffer[index] = TheBuffer[(byte)(index + 1)];
		
		// send the numbers of bytes of data at offset 0 in the APDU buffer
		apdu.sendBytes((short)0, (short)TheBuffer.length); 
		
		return;
	}

	public void push(APDU apdu) {
	   	byte buffer[] = apdu.getBuffer(); 

	   	byte size = (byte)(apdu.setIncomingAndReceive());

		if (size != (byte)2) { 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 
		}
		
		byte[] TheBuffer = new byte[size];
		for (byte index = 0; index < size; index++) 
			TheBuffer[(byte)(index + 1)] = buffer[(byte)(ISO7816.OFFSET_CDATA + index)];
		
		userApplet.stack.push(BytePairToShort(TheBuffer[0],TheBuffer[1]));
		
		return;
	}
	
	public void add(APDU apdu) {
		
		// indicate that this APDU has incoming data and
		// receive data starting from the offset 
		// ISO7816.OFFSET_CDATA
		byte byteRead = (byte)(apdu.setIncomingAndReceive()); 

		// it is an error if the number of data bytes read does not 
		// match the number in Lc byte
		if (byteRead != (byte) 0) 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 

		if(userApplet.stack.isEmpty() || userApplet.stack.size()<(short)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		short first = userApplet.stack.pop();
		short second = userApplet.stack.pop();
		userApplet.stack.push((short)(second+first));

		// return successfully
		return; 
	}

	public void minus(APDU apdu) {
		
		// indicate that this APDU has incoming data and
		// receive data starting from the offset 
		// ISO7816.OFFSET_CDATA
		byte byteRead = (byte)(apdu.setIncomingAndReceive()); 

		// it is an error if the number of data bytes read does not 
		// match the number in Lc byte
		if (byteRead != (byte) 0) 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 

		if(userApplet.stack.isEmpty() || userApplet.stack.size()<(short)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		short first = userApplet.stack.pop();
		short second = userApplet.stack.pop();
		userApplet.stack.push((short)(second-first));

		// return successfully
		return; 
	}
	
	public void div(APDU apdu) {
		
		// indicate that this APDU has incoming data and
		// receive data starting from the offset 
		// ISO7816.OFFSET_CDATA
		byte byteRead = (byte)(apdu.setIncomingAndReceive()); 

		// it is an error if the number of data bytes read does not 
		// match the number in Lc byte
		if (byteRead != (byte) 0) 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 

		if(userApplet.stack.isEmpty() || userApplet.stack.size()<(short)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		short first = userApplet.stack.pop();
		short second = userApplet.stack.pop();
		userApplet.stack.push((short)(second/first));

		// return successfully
		return; 
	}
	
	public void mult(APDU apdu) {
		
		// indicate that this APDU has incoming data and
		// receive data starting from the offset 
		// ISO7816.OFFSET_CDATA
		byte byteRead = (byte)(apdu.setIncomingAndReceive()); 

		// it is an error if the number of data bytes read does not 
		// match the number in Lc byte
		if (byteRead != (byte) 0) 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 

		if(userApplet.stack.isEmpty() || userApplet.stack.size()<(short)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		short first = userApplet.stack.pop();
		short second = userApplet.stack.pop();
		stack.push((short)(second*first));

		// return successfully
		return; 
	}

	/**
     * Converts a short into a 2 byte array
     * 
     * @param i short to convert
     */
    public static byte[] ShortToBytePair(short i)
    {
        byte[] retVal = new byte[2];
        retVal[0] = (byte)((short)((i & (short)0xFFFF) >> (short)8));
        retVal[1] = (byte)(i & (short)0x00FF);
        return retVal;
    }
    
    /**
     * Converts a byte pair to a short.
     * 
     * @param msb most significant byte
     * @param lsb least significant byte
     */
    public static short BytePairToShort(byte msb, byte lsb)
    {
        short smsb, slsb;
        smsb = (short)((msb & (short)0x00FF) << (short)8);
        slsb = (short)(lsb & (short)0x00FF);
        return (short) (smsb | slsb);
    }
	
}
