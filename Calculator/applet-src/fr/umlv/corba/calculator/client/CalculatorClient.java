package fr.umlv.corba.calculator.client;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javacard.framework.ISO7816;
import opencard.cflex.service.CFlex32CardService;
import opencard.core.service.CardRequest;
import opencard.core.service.CardServiceException;
import opencard.core.service.SmartCard;
import opencard.core.terminal.CardTerminal;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.CardTerminalRegistry;
import opencard.core.terminal.ResponseAPDU;
import opencard.core.util.HexString;
import opencard.core.util.OpenCardPropertyLoadingException;
import opencard.opt.terminal.ISOCommandAPDU;
import fr.umlv.corba.calculator.Utils;
import fr.umlv.corba.calculator.applet.Util;

public class CalculatorClient {

	public static void main(String[] args) throws CardTerminalException,
	OpenCardPropertyLoadingException, CardServiceException,
	ClassNotFoundException, UnsupportedEncodingException {
		if (SmartCard.isStarted() == false) {
			SmartCard.start();
		}
		
		CardTerminalRegistry ctr = CardTerminalRegistry.getRegistry();
		for (Enumeration terminals = ctr.getCardTerminals(); terminals
				.hasMoreElements();) {
			CardTerminal terminal = (CardTerminal) terminals.nextElement();
			int slots = Utils.printTerminalInfos(terminal);
			for (int j = 0; j < slots; j++) {
				Utils.printSlotInfos(terminal, j);
			}
		}
		System.out.println("Insert your card ...");
		CardRequest cr = new CardRequest(CardRequest.ANYCARD, null, null);
		SmartCard sm = SmartCard.waitForCard(cr);
		CFlex32CardService javacard = (CFlex32CardService) sm.getCardService(
				CFlex32CardService.class, true);
		javacard.selectApplication(HexString.parseHexString("000102030405"));
		
		byte[] n1 = fr.umlv.corba.calculator.applet.Util.ShortToBytePair((short) 15);
		byte[] n2 = fr.umlv.corba.calculator.applet.Util.ShortToBytePair((short) 0);
		
		try {
			javacard.allocateChannel();

			ResponseAPDU res = javacard.sendAPDU(new ISOCommandAPDU(
					(byte) 0xA0, (byte) 0x10, (byte) 0, (byte) 0, n1, 2));
			
			res = javacard.sendAPDU(new ISOCommandAPDU(
					(byte) 0xA0, (byte) 0x10, (byte) 0, (byte) 0, n2, 2));
			res = javacard.sendAPDU(new ISOCommandAPDU(
					(byte) 0xA0, (byte) 0x60, (byte) 0, (byte) 0, new byte[0], 0));
			System.out.println("Statut : " + res.sw());
			
			check(res.getBuffer());
			res = javacard.sendAPDU(new ISOCommandAPDU((byte) 0xA0,
					(byte) 0x20, (byte) 0, (byte) 0, new byte[0], 2));
			byte[] arrayResponse = res.getBuffer();
			short response = Util.BytePairToShort(arrayResponse[0],arrayResponse[1]);
			check(arrayResponse);
			System.err.println("le resultat : " + response);
		}  catch (ArithmeticException e) {
			e.printStackTrace();
		}finally {
			javacard.releaseChannel();
		}
	}
	
	private static boolean check(byte[] buffer) {
		
		short result = javacard.framework.Util.getShort(buffer,(short)0);
		if(result == ISO7816.SW_CONDITIONS_NOT_SATISFIED)
			System.err.println("ERROR");
		return false;
	}
}
