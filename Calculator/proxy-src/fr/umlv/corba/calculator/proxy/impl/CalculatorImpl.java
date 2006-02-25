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
import fr.umlv.corba.calculator.proxy.ArithmeticException;
import fr.umlv.corba.calculator.proxy.CorbaCalculatorPOA;
import fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators;
import fr.umlv.corba.calculator.proxy.UnKnowErrorException;

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
	public short pop() throws InvalidNumberOfOperators, UnKnowErrorException {
		try {
			sendAPDU(Calculator.GET,BYTE,2);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
		
		short status = (short) result.sw();
		if(status != (short)0x9000) { 
			switch (status) {
			case Calculator.SW_EMPTY_STACK :
				throw new InvalidNumberOfOperators("La pile est vide.");
			case Calculator.SW_INVALID_NUMBER_OF_OPERATORS :
				throw new InvalidNumberOfOperators("Nombre d'operateurs incorrects.");
			default:
				throw new UnKnowErrorException("Erreur inconnue");
			}
		}
		
		byte[] arrayResponse = result.getBuffer();
		short response = Util.BytePairToShort(arrayResponse[0],
				arrayResponse[1]);
		return response;
	}
	
	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#pop()
	 */
	public short top() throws InvalidNumberOfOperators, UnKnowErrorException {
		try {
			sendAPDU(Calculator.TOP,BYTE,2);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
		
		short status = (short) result.sw();
		if(status != (short)0x9000) { 
			switch (status) {
			case Calculator.SW_EMPTY_STACK :
				throw new InvalidNumberOfOperators("La pile est vide.");
			case Calculator.SW_INVALID_NUMBER_OF_OPERATORS :
				throw new InvalidNumberOfOperators("Nombre d'operateurs incorrects.");
			default:
				throw new UnKnowErrorException("Erreur inconnue");
			}
		}
		
		byte[] arrayResponse = result.getBuffer();
		short response = Util.BytePairToShort(arrayResponse[0],
				arrayResponse[1]);
		return response;
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#add()
	 */
	public void add() throws InvalidNumberOfOperators, UnKnowErrorException {
		try {
			sendAPDU(Calculator.ADD,BYTE,0);
		} catch (CardTerminalException e1) {
			e1.printStackTrace();
		}
		
		short status = (short) result.sw();
		if(status != (short)0x9000) { 
			switch (status) {
			case Calculator.SW_EMPTY_STACK :
				throw new InvalidNumberOfOperators("La pile est vide.");
			case Calculator.SW_INVALID_NUMBER_OF_OPERATORS :
				throw new InvalidNumberOfOperators("Nombre d'operateurs incorrects.");
			default:
				throw new UnKnowErrorException("Erreur inconnue");
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#minus()
	 */
	public void sub() throws InvalidNumberOfOperators, UnKnowErrorException {
		try {
			sendAPDU(Calculator.SUB,BYTE,0);
		} catch (CardTerminalException e1) {
			e1.printStackTrace();
		}
		
		short status = (short) result.sw();
		if(status != (short)0x9000) { 
			switch (status) {
			case Calculator.SW_EMPTY_STACK :
				throw new InvalidNumberOfOperators("La pile est vide.");
			case Calculator.SW_INVALID_NUMBER_OF_OPERATORS :
				throw new InvalidNumberOfOperators("Nombre d'operateurs incorrects.");
			default:
				throw new UnKnowErrorException("Erreur inconnue");
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#mult()
	 */
	public void mult() throws InvalidNumberOfOperators, UnKnowErrorException {
		try {
			sendAPDU(Calculator.MULT, BYTE,0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
		
		short status = (short) result.sw();
		if(status != (short)0x9000) { 
			switch (status) {
			case Calculator.SW_EMPTY_STACK :
				throw new InvalidNumberOfOperators("La pile est vide.");
			case Calculator.SW_INVALID_NUMBER_OF_OPERATORS :
				throw new InvalidNumberOfOperators("Nombre d'operateurs incorrects.");
			default:
				throw new UnKnowErrorException("Erreur inconnue");
			}
		}
	}

	/* (non-Javadoc)
	 * @see fr.umlv.corba.calculator.client.CalculatorOperations#div()
	 */
	public void div() throws ArithmeticException, InvalidNumberOfOperators, UnKnowErrorException {
		try {
			sendAPDU(Calculator.DIV, BYTE,0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		} 
		
		short status = (short) result.sw();
		if(status != (short)0x9000) { 
			switch (status) {
			case Calculator.SW_ARITHMETIC_ERROR :
				throw new ArithmeticException("Division par 0 impossible.");
			case Calculator.SW_EMPTY_STACK :
				throw new InvalidNumberOfOperators("La pile est vide.");
			case Calculator.SW_INVALID_NUMBER_OF_OPERATORS :
				throw new InvalidNumberOfOperators("Nombre d'operateurs incorrects.");
			default:
				throw new UnKnowErrorException("Erreur inconnue");
			}
		}
	}

	public void reset() {
		try {
			sendAPDU(Calculator.RESET, BYTE,0);
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
