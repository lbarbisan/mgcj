package fr.umlv.corba.calculator.applet;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

public class CalculatorApplet extends Applet implements Calculator {
	
	private static final short MIN_OPERATORS = (short)2;
	private static final byte BUFFER_MAX_SIZE = (byte)2;
	private Stack stack;
	
	public CalculatorApplet() {
		stack = new ArrayStack();
	}
	
	public CalculatorApplet(byte buffer[],short offset,byte length) {
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
		new CalculatorApplet(buffer, offset, length);

	} // end of install method
	
	public void process(APDU apdu) throws ISOException {
		byte[] buffer = apdu.getBuffer();
		
		// Implement a select handler 
		if (selectingApplet()) {
			ISOException.throwIt(ISO7816.SW_NO_ERROR);
		}
		
		if (buffer[ISO7816.OFFSET_CLA] != Calculator.CALCULATOR_CLA) 
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		
		byte instruction = buffer[ISO7816.OFFSET_INS];
		
		switch (instruction) {
		case Calculator.SET:
			push(apdu);
			break;
		case Calculator.GET:
			pop(apdu);
			break;
		case Calculator.TOP:
			top(apdu);
			break;
		case Calculator.ADD:
			add(apdu);
			break;
		case Calculator.SUB:
			sub(apdu);
			break;
		case Calculator.MULT:
			mult(apdu);
			break;
		case Calculator.DIV:
			div(apdu);
			break;
		case Calculator.SELECT:
			select();
			break;
		case Calculator.RESET:
			reset();
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
	
	public boolean reset() {
		//vider la pile
		stack.clear();
		return true;
	}
	
	public void pop(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		
		if(stack.isEmpty()) {
			ISOException.throwIt(Calculator.SW_EMPTY_STACK);
		}
		
		short value = stack.pop();
		byte [] TheBuffer = Util.ShortToBytePair(value);
		
		// inform system that the applet has finished processing
		// the command and the system should now prepare to 
		// construct a response APDU which contains data field
		apdu.setOutgoing(); 

		// indicate the number of bytes in the data field
		apdu.setOutgoingLength((byte)TheBuffer.length); 

		// move the data into the APDU buffer starting at offset 0		
		for (byte index = 0; index < TheBuffer.length; index++)
			buffer[index] = TheBuffer[(byte)(index)];
		
		// send the numbers of bytes of data at offset 0 in the APDU buffer
		apdu.sendBytes((short)0, (short)TheBuffer.length); 
		
		return;
	}
	
	public void top(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		
		if(stack.isEmpty()) {
			ISOException.throwIt(Calculator.SW_EMPTY_STACK);
		}
		
		short value = stack.top();
		byte [] TheBuffer = Util.ShortToBytePair(value);
		
		// inform system that the applet has finished processing
		// the command and the system should now prepare to 
		// construct a response APDU which contains data field
		apdu.setOutgoing(); 

		// indicate the number of bytes in the data field
		apdu.setOutgoingLength((byte)TheBuffer.length); 

		// move the data into the APDU buffer starting at offset 0		
		for (byte index = 0; index < TheBuffer.length; index++)
			buffer[index] = TheBuffer[(byte)(index)];
		
		// send the numbers of bytes of data at offset 0 in the APDU buffer
		apdu.sendBytes((short)0, (short)TheBuffer.length); 
		
		return;
	}

	public void push(APDU apdu) {
	   	byte buffer[] = apdu.getBuffer(); 

	   	byte size = (byte)(apdu.setIncomingAndReceive());

		if (size != BUFFER_MAX_SIZE) { 
			ISOException.throwIt(Calculator.SW_ARITHMETIC_ERROR); 
		}
		
		byte[] TheBuffer = new byte[size];
		for (byte index = 0; index < size; index++) 
			TheBuffer[(byte)(index)] = buffer[(byte)(ISO7816.OFFSET_CDATA + index)];
		
		stack.push(Util.BytePairToShort(TheBuffer[0],TheBuffer[1]));
		
		return;
	}
	
	public void add(APDU apdu) {
		
		if(stack.isEmpty()) {
			ISOException.throwIt(Calculator.SW_EMPTY_STACK);
		}
		
		if(stack.size()<MIN_OPERATORS) {
			ISOException.throwIt(Calculator.SW_INVALID_NUMBER_OF_OPERATORS);
		}
		
		short first = stack.pop();
		short second = stack.pop();
		stack.push((short)(second+first));

		// return successfully
		return; 
	}

	public void sub(APDU apdu) {
		
		if(stack.isEmpty()) {
			ISOException.throwIt(Calculator.SW_EMPTY_STACK);
		}
		
		if(stack.size()<MIN_OPERATORS) {
			ISOException.throwIt(Calculator.SW_INVALID_NUMBER_OF_OPERATORS);
		}
		
		short first = stack.pop();
		short second = stack.pop();
		stack.push((short)(second-first));

		// return successfully
		return; 
	}
	
	public void div(APDU apdu) {
		
		if(stack.isEmpty()) {
			ISOException.throwIt(Calculator.SW_EMPTY_STACK);
		}
		
		if(stack.size()<MIN_OPERATORS) {
			ISOException.throwIt(Calculator.SW_INVALID_NUMBER_OF_OPERATORS);
		}
		
		short first = stack.pop();
		short second = stack.pop();
		
		if(first == (short)0) {
			ISOException.throwIt(Calculator.SW_ARITHMETIC_ERROR); //division par 0
		}
		
		stack.push((short)(second/first));
		
		// return successfully
		return; 
	}
	
	public void mult(APDU apdu) {
		
		if(stack.isEmpty()) {
			ISOException.throwIt(Calculator.SW_EMPTY_STACK);
		}
		
		if(stack.size()<MIN_OPERATORS) {
			ISOException.throwIt(Calculator.SW_INVALID_NUMBER_OF_OPERATORS);
		}
		
		short first = stack.pop();
		short second = stack.pop();
		stack.push((short)(second*first));

		// return successfully
		return; 
	}
}
