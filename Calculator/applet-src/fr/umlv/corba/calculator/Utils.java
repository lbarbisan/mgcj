/**
 * 
 */
package fr.umlv.corba.calculator;

import opencard.core.terminal.CardTerminal;
import opencard.core.terminal.CardTerminalException;
import opencard.core.util.HexString;

/**
 * @author cedric
 *
 */
public class Utils {

	public static int printTerminalInfos(CardTerminal terminal) {
		// First of all print all information stored about this reader
		System.err.println("Address: " + terminal.getAddress() + "\n"
				+ "Name:    " + terminal.getName() + "\n" + "Type:    "
				+ terminal.getType() + "\n" + "Slots:   " + terminal.getSlots()
				+ "\n");
		return terminal.getSlots();
	}

	public static void printSlotInfos(CardTerminal terminal, int aSlotID) {
		try {
			// First print the ID of the slot
			System.err.println("Info for slot ID: " + aSlotID);
			if (terminal.isCardPresent(aSlotID)) {
				System.err.println("card present: yes");
				// If there is a card in the slot print the ATR the OCF got form this
				// card
				System.err.println("ATR: "
						+ HexString
								.hexify(terminal.getCardID(aSlotID).getATR()));
				// As we do not have a driver for this card we cannot interpret this ATR
			} else
				System.err.println("card present: no");
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
	}

}
