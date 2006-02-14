/**
 * 
 */
package fr.umlv.corba.calculator.proxy.impl;

import opencard.cflex.service.CFlex32CardService;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.ResponseAPDU;
import opencard.opt.terminal.ISOCommandAPDU;
import fr.umlv.corba.calculator.applet.Calculator;
import fr.umlv.corba.calculator.applet.Util;
import fr.umlv.corba.calculator.proxy.CorbaCalculatorPOA;

/**
 * @author cedric
 *
 */
public class CalculatorImpl extends CorbaCalculatorPOA {

	private static final byte[] BYTE = new byte[0];

	public static final byte ZERO = (byte) 0;

	private CFlex32CardService javacard;

	private ResponseAPDU result;

	public CalculatorImpl(CFlex32CardService javacard) {
		super();
		this.javacard = javacard;
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#push(short)
	 */
	public void push(short value) {
		byte[] byteValue = Util.ShortToBytePair(value);
		try {
			sendAPDU(Calculator.SET,byteValue,byteValue.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#pop()
	 */
	public short pop() {
		try {
			sendAPDU(Calculator.GET,BYTE,2);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
		byte[] arrayResponse = result.getBuffer();
		short response = Util.BytePairToShort(arrayResponse[0],
				arrayResponse[1]);
		return response;
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#add()
	 */
	public void add() {
		try {
			sendAPDU(Calculator.ADD,BYTE,0);
		} catch (CardTerminalException e1) {
			e1.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#minus()
	 */
	public void sub() {
		try {
			sendAPDU(Calculator.SUB,BYTE,0);
		} catch (CardTerminalException e1) {
			e1.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#mult()
	 */
	public void mult() {
		try {
			sendAPDU(Calculator.MULT, BYTE,0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#div()
	 */
	public void div() {
		try {
			sendAPDU(Calculator.DIV, BYTE,0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		} 
	}

	/* (non-Javadoc)
	 * @see opencard.cflex.service.CFlex32CardService#sendAPDU(opencard.core.terminal.CommandAPDU)
	 */
	public ResponseAPDU sendAPDU(byte instruction, byte[] buffer, int length) throws CardTerminalException {
		try {
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA,
					instruction, ZERO, ZERO, buffer,
					length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
