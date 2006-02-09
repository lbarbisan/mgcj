package opencard.cflex.factory;

import opencard.cflex.service.*;
import opencard.cflex.util.*;

import java.util.*;
import opencard.core.service.*;
import opencard.core.util.*;
import opencard.core.terminal.CardID;

/**
 * Creates instances of the card service Java CardLoaderProxy for Java Cards.
 * 
 * @author Thomas Schaeck (schaeck@de.ibm.com)
 * @author Rinaldo Di Giorgio(rinaldo.digiorgio@sun.com)
 * 
 * @version $Id: CFlexAccessCardServiceFactory.java,v 1.3 2005/04/16 16:46:04
 *          martin Exp $
 */
public class CFlexAccessCardServiceFactory extends CardServiceFactory {

  private static Tracer ctracer = new Tracer(
      CFlexAccessCardServiceFactory.class);

  // http://www.cyberflex.com/Support/Documentation/CyberflexPG_4-5.pdf
  // The answer to reset code (ATR) of a Cyberflex Access Developer 32K card
  // begins with: 3B 17 13 9C 12
  private final static int DEVELOPER32K = 0x01;

  private final static String DEVELOPER32K_ATR = "3B17139C120000000000";

  // The ATR mask is: FF FF 00 FF 00 00 00 00 00 00
  private final static String DEVELOPER32K_ATRMASK = "FFFF00FF000000000000";

  // � The answer to reset code (ATR) of a Cyberflex Access e-gate 32K card is:
  // 3B 75 94 00 00 62 02 02 00 80
  private final static int E32K = 0x02;

  private final static String E32K_ATR = "3B759400006202020080";

  // The ATR mask is: FF FF FF 00 00 FF FF FF 00 00
  private final static String E32K_ATRMASK = "FFFFFF0000FFFFFF0000";

  // � The answer to reset code (ATR) of a Cyberflex Access 32K v4 card is:
  // 3b 76 00 00 00 00 9c 11 01 00 00
  private final static int V432K = 0x03;

  private final static String V432K_ATR = "3B76000000009C110100";

  private final static String V432K_ATRMASK = "FFFFFFFFFFFFFFFFFFFF";

  // � The answer to reset code (ATR) of a Cyberflex Access 64K v1 card is:
  // 3b 75 00 00 00 29 05 01 01 01 (for the non-FIPS-compliant card
  // softmask 1.1)
  // 3b 75 00 00 00 29 05 01 02 01 (for the FIPS-compliant card,
  // softmask 2.1)
  // The ATR mask is: FF FF 00 00 00 FF FF FF 00 00
  private final static int V164K = 0x04;

  private final static String V164K_ATR = "3B750000002905010101";

  private final static String V164K_ATRMASK = "FFFF000000FFFFFF0000";

  // http://www.cyberflex.com/Support/Documentation/Cyber_64kv2_4-5.pdf
  // The answer to reset code (ATR) of a Cyberflex Access 64K v2 card will
  // resemble this example:
  // 3B 95 95 40 FF AE 01 01 02 03
  // AE will be present every time. The following 0101 represents the hardmask
  // version, which might change infrequently. The last two bytes, 0203 in the
  // example, indicate the softmask version, which usually changes when problems
  // are corrected. The bytes before AE are protocol bytes defined in ISO 7816.
  private final static int V264K = 0x05;

  private final static String V264K_ATR = "3B959540FFAE01010203";

  private final static String V264K_ATRMASK = "FFFFFFFFFFFF00000000";

  private static Card[] cards = 
    { new Card(DEVELOPER32K, DEVELOPER32K_ATR, DEVELOPER32K_ATRMASK),
      new Card(E32K, E32K_ATR, E32K_ATRMASK),
      new Card(V432K, V432K_ATR, V432K_ATRMASK),
      new Card(V164K, V164K_ATR, V164K_ATRMASK),
      new Card(V264K, V264K_ATR, V264K_ATRMASK)
    };

  private static class Card {
    private final int id;
    private final byte[] atr;
    private final byte[] mask;
    Card(int id, String atr, String mask) {
      this.id = id;
      this.atr = HexString.parseHexString(atr);
      this.mask = HexString.parseHexString(mask);
    }
  }

  /**
   * Constructs an object of this class.
   */
  public CFlexAccessCardServiceFactory() {
  }

  protected CardType getCardType(CardID cid, CardServiceScheduler scheduler) {
    byte[] cidAtr = cid.getATR();
    for (Card card : cards) {
      if (Crypto.areEqual(Crypto.and(cidAtr, card.mask), Crypto
          .and(card.atr, card.mask))) {
        ctracer.debug("getCardType", "known card type " + cid);
        return new CardType(card.id);
      }
    }
    return CardType.UNSUPPORTED;
  }

  protected Enumeration getClasses(CardType type) {
    ctracer.debug("getClasses", "card type is " + type.getType());
    switch (type.getType()) {
    case E32K:
    case V432K:
    case V164K:
    case V264K: 
      ctracer.info("getClasses","E32K");
      return Collections.enumeration(Collections.singleton(CFlex32ECardService.class));
    case DEVELOPER32K: 
      ctracer.info("getClasses","D32K");
      return Collections.enumeration(Collections.singleton(CFlex32DCardService.class));
    default:
      return null;
    }
  }

}
