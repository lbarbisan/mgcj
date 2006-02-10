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
		System.err.println(byteValue);
		
		try {
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA,
					Calculator.SET, ZERO, ZERO, byteValue,
					byteValue.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#pop()
	 */
	public short pop() {
		try {
			byte[] bs = BYTE;
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA,
					Calculator.GET, ZERO, ZERO, bs, 2));
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
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA,
					Calculator.ADD, ZERO, ZERO, BYTE, 0));
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#minus()
	 */
	public void minus() {
		try {
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA,
					Calculator.MINUS, ZERO, ZERO, BYTE, 0));
		} catch (CardTerminalException e) {
			e.printStackTrace();
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
//		try {
//			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA,
//					Calculator.MULT, ZERO, ZERO, BYTE, 0));
//		} catch (CardTerminalException e) {
//			e.printStackTrace();
//		}
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
