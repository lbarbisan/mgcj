package fr.umlv.corba.calculator;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

public class CalculatorAppletOld extends Applet {
	
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

	//private static CalculatorAppletOld userApplet;
	
	private Stack stack;
	
	public CalculatorAppletOld() {
		stack = new LinkedStack();
	}
	
	public CalculatorAppletOld(byte buffer[],short offset,byte length) {
		stack = new LinkedStack();
		
		if (buffer[offset] == (byte)0) {
			register();
		}
		else {
			register(buffer, (short)(offset+1) ,(byte)(buffer[offset]));
		}
	}

	public static void install(byte buffer[],short offset,byte length){
		
		// create a Calculator applet instance (card) *!*
		new CalculatorAppletOld(buffer, offset, length);

		// create a Calculator applet instance (Simulator) *!*
		//userApplet = new CalculatorAppletOld();

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
			setInteger(apdu);
			break;
		case GET:
			getInteger(apdu);
			break;
		case ADD:
			add(apdu);
			break;
		case MINUS:
			minus(apdu);
			break;
		case MULT:
			multi(apdu);
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
		stack.clear();
		return true;
	}
	
	private void getInteger(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		
		//byte byteRead = buffer[ISO7816.OFFSET_LC];
		
		byte TheBuffer = stack.pop();
		
		/*if (byteRead == (byte)0) {
			ISOException.throwIt((short)(0x6200 + TheBuffer));
		}*/
		
		// inform system that the applet has finished processing
		// the command and the system should now prepare to 
		// construct a response APDU which contains data field
		apdu.setOutgoing(); 

		// indicate the number of bytes in the data field
		apdu.setOutgoingLength((byte)1); 

		// move the data into the APDU buffer starting at offset 0		
		buffer[0] = TheBuffer;
		
		// send 1 byte of data at offset 0 in the APDU buffer
		apdu.sendBytes((short)0, (short)1); 
		//apdu.sendBytes((short)0, (short)byteRead); 
		
		return;
	}

	private void setInteger(APDU apdu) {
	   	byte buffer[] = apdu.getBuffer(); 

		byte byteRead = (byte)(apdu.setIncomingAndReceive()); 

		if (byteRead == (byte)0) { 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 
		}
		
		byte TheBuffer = buffer[ISO7816.OFFSET_CDATA];
		stack.push(TheBuffer);
		
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

		if(stack.isEmpty() || stack.size()<(byte)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		byte first = stack.pop();
		byte second = stack.pop();
		stack.push((byte)(second+first));

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

		if(stack.isEmpty() || stack.size()<(byte)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		byte first = stack.pop();
		byte second = stack.pop();
		stack.push((byte)(second-first));

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

		if(stack.isEmpty() || stack.size()<(byte)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		byte first = stack.pop();
		byte second = stack.pop();
		stack.push((byte)(second/first));

		// return successfully
		return; 
	}
	
	public void multi(APDU apdu) {
		
		// indicate that this APDU has incoming data and
		// receive data starting from the offset 
		// ISO7816.OFFSET_CDATA
		byte byteRead = (byte)(apdu.setIncomingAndReceive()); 

		// it is an error if the number of data bytes read does not 
		// match the number in Lc byte
		if (byteRead != (byte) 0) 
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH); 

		if(stack.isEmpty() || stack.size()<(byte)2) {
			ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
		}
		
		byte first = stack.pop();
		byte second = stack.pop();
		stack.push((byte)(second*first));

		// return successfully
		return; 
	}
    
	public interface Stack {

		/**
		 * 
		 * @param obj 
		 */
		void push(byte obj);

		/**
		 * 
		 * @return
		 */
		byte pop();

		/**
		 * 
		 * @return
		 */
		byte first();
		
		/**
		 * 
		 * @return
		 */
		boolean isEmpty();
		
		void clear();
		
		byte size();
	}
	public class LinkedStack implements Stack {
		
		// Pile de données
		private byte[] pile;
		final short size = 10;
		short index = 0;
		
		public LinkedStack() {
			pile = new byte[size];
		}

		/**
		 * @see fr.umlv.corba.calculator.stack.Stack#push()
		 */
		public void push(byte value) {
			pile[index++] = value;
			if(index==size) {
				byte[] tmp = new byte[size+size];
				for (short i = 0; i < (short)pile.length; i++) {
					tmp[i] = pile[i];
				}
				pile = tmp;
			}
		}

		/**
		 * @see fr.umlv.corba.calculator.stack.Stack#pop()
		 */
		public byte pop() {
			return pile[--index];
		}

		/**
		 * @see fr.umlv.corba.calculator.stack.Stack#first()
		 */
		public byte first() {
			return pile[(short)(index-(short)1)];
		}	
		
		/**
		 * @see fr.umlv.corba.calculator.stack.Stack#isEmpty()
		 */
		public boolean isEmpty() {
			return index==0;
		}
		
		public void clear() {
			index=0;
		}
		
		public byte size() {
			return (byte)(index);
		}
	}
	
}
