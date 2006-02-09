/*
 * Created on 22 oct. 2005 by roussel
 */
package opencard.cflex.test;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import opencard.core.service.*;
import opencard.core.terminal.*;
import opencard.core.util.*;
import opencard.opt.terminal.ISOCommandAPDU;
import opencard.cflex.service.CFlex32CardService;

public class SimpleStringClient {
  public static void main(String[] args) throws CardTerminalException, OpenCardPropertyLoadingException, CardServiceException, ClassNotFoundException, UnsupportedEncodingException {
    if (SmartCard.isStarted() == false) {
      SmartCard.start();
    }
    CardTerminalRegistry ctr = CardTerminalRegistry.getRegistry();
    for (Enumeration terminals = ctr.getCardTerminals();terminals.hasMoreElements();) {
      CardTerminal terminal = (CardTerminal) terminals.nextElement(); 
      int slots = printTerminalInfos(terminal);
      for (int j = 0; j < slots; j++) {
        printSlotInfos(terminal, j);
      }
    }
    System.out.println("Insert your card ...");
    CardRequest cr = new CardRequest(CardRequest.ANYCARD, null, null);
    SmartCard sm = SmartCard.waitForCard(cr);
    CFlex32CardService javacard = (CFlex32CardService) sm.getCardService(
        CFlex32CardService.class, true);
    javacard.selectApplication(HexString.parseHexString("A00000000101"));
    byte[] array = "Titi".getBytes("ASCII");
    try {
      javacard.allocateChannel();

      ResponseAPDU res = javacard.sendAPDU(new ISOCommandAPDU((byte)0x85,(byte)0x10,(byte)0,(byte)0,array,0));
      //System.err.println("\""+res+"\"");
      res = javacard.sendAPDU(new ISOCommandAPDU((byte)0x85,(byte)0x20,(byte)0,(byte)0,new byte[0],4));
      System.err.println("la phrase : "+new String(res.getBuffer()));
    } finally {
      javacard.releaseChannel();
    }
  }

  private static int printTerminalInfos(CardTerminal terminal) {
    // First of all print all information stored about this reader
    System.err.println("Address: " + terminal.getAddress() + "\n" + "Name:    "
        + terminal.getName() + "\n" + "Type:    " + terminal.getType() + "\n"
        + "Slots:   " + terminal.getSlots() + "\n");
    return terminal.getSlots();
  }
  
  private static void printSlotInfos(CardTerminal terminal, int aSlotID) {
    try {
      // First print the ID of the slot
      System.err.println("Info for slot ID: " + aSlotID);
      if (terminal.isCardPresent(aSlotID)) {
        System.err.println("card present: yes");
        // If there is a card in the slot print the ATR the OCF got form this
        // card
        System.err.println("ATR: "
            + HexString.hexify(terminal.getCardID(aSlotID).getATR()));
        // As we do not have a driver for this card we cannot interpret this ATR
      } else
        System.err.println("card present: no");
    } catch (CardTerminalException e) {
      e.printStackTrace();
    }
  }

}
