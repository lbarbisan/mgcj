package fr.umlv.corba.calculator.applet;

import javacard.framework.APDU;

public interface Calculator {

	// This applet is designed to respond to the following
	// class of instructions.
	final static byte CALCULATOR_CLA = (byte) 0xA0;

	final static byte SET = (byte) 0x10;

	final static byte GET = (byte) 0x20;

	final static byte ADD = (byte) 0x30;

	final static byte SUB = (byte) 0x40;

	final static byte MULT = (byte) 0x50;

	final static byte DIV = (byte) 0x60;

	final static byte SELECT = (byte) 0xA4;
	
	final static byte RESET = (byte) 0xA5;

	void push(APDU apdu);

	void pop(APDU apdu);

	void add(APDU apdu);

	void sub(APDU apdu);

	void mult(APDU apdu);

	void div(APDU apdu);
}
