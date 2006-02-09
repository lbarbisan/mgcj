/*
 * Created on 25 oct. 2005 by roussel
 */
package opencard.cflex.service;


public class CFlex32DCardService extends AbstractCFlex32CardService {
  // http://www.flexforum.com/forums/cyberflexgeneral/127.html#1
  private static byte[] DEFAULT_CARD_MANAGER_AID_D32 = { (byte) 0xa0, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01 };

  protected byte[] getCardManagerAID() {
    return DEFAULT_CARD_MANAGER_AID_D32;
  }

}
