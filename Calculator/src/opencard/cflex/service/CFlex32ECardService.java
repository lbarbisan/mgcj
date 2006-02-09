/*
 * Created on 25 oct. 2005 by roussel
 */
package opencard.cflex.service;

public class CFlex32ECardService extends AbstractCFlex32CardService {

  private static final byte[] DEFAULT_CARD_MANAGER_AID_E32 = { (byte) 0xa0, (byte) 0x00,
      (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00 };

  protected byte[] getCardManagerAID() {
    return DEFAULT_CARD_MANAGER_AID_E32;
  }


}
