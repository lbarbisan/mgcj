package opencard.cflex.app;


import opencard.cflex.service.CFlex32CardService;
import opencard.core.service.CardRequest;
import opencard.core.service.SmartCard;
import opencard.core.terminal.*;
import opencard.core.util.HexString;
import opencard.core.event.CardTerminalEvent;
import opencard.core.event.CTListener;
import opencard.core.event.EventGenerator;

import java.util.*;
import java.io.*;

public class LoadCFlexAccess32 implements CTListener {

  public LoadCFlexAccess32() throws Exception {
    init();
  }

  private void init() throws Exception {
    if (SmartCard.isStarted() == false) {
      SmartCard.start();
    }
    CardTerminalRegistry ctr = CardTerminalRegistry.getRegistry();
    Enumeration terminals = ctr.getCardTerminals();
    CardTerminal terminal = (CardTerminal) terminals.nextElement();
    int slots = printTerminalInfos(terminal);
    for (int j = 0; j < slots; j++) {
      printSlotInfos(terminal, j);
    }
    System.out.println("Insert your card ...");
    CardRequest cr = new CardRequest(CardRequest.ANYCARD, null, null);
    sm = SmartCard.waitForCard(cr);
    EventGenerator.getGenerator().addCTListener(this);
  }

  public void load(InputStream bin, int staticsize) throws Exception {

    getLoader().createSecureChannel(getAuthKey(), getMacKey());

//    try {
//      getLoader().deleteApplication(getAppletId());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    try {
//      getLoader().deleteApplication(getPackageId());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    getLoader().installLoad(getPackageId(), bin, staticsize);
    getLoader().load();

  }

  public void delete(byte[] aid) throws Exception {

    getLoader().createSecureChannel(getAuthKey(), getMacKey());
    try {
      getLoader().deleteApplication(aid);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void install(int instance_size) throws Exception { // 20000 max so
    // far

    getLoader().createSecureChannel(getAuthKey(), getMacKey());
//    try {
//      getLoader().deleteApplication(getAppletId());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    getLoader().install(getPackageId(), getAppletId(), getAppletId(),
        instance_size);
  }

  private void status(int type) throws Exception {

    getLoader().createSecureChannel(getAuthKey(), getMacKey());
    try {
      ResponseAPDU res = getLoader().status(type);
      System.out.println("Response status : " + HexString.hexify(res.sw1())+ HexString.hexify(res.sw2()));
      byte[] data = res.data();
      if (data == null) return;
      for (int i=0;i<data.length;i++) {
        int len = data[i]&0xFF;
        i++;
        System.out.print("Data: ");
        for (int j=0;j<len;j++,i++) {
          System.out.print(HexString.hexify(data[i]));
        }
        System.out.print(" / State: " +HexString.hexify(data[i]));
        i++;
        System.out.println(" / Privileges: " +HexString.hexify(data[i]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

//  private void setup(int object_size) throws Exception {
//    getLoader().setup(getAppletId(), transportKey.getBytes(), chv0.getBytes(),
//        ublkchv0.getBytes(), chv1.getBytes(), ublkchv1.getBytes(), object_size, // 17408
//        // max
//        // so
//        // far
//        acl);
//
//  }

  /**
   * Gets invoked if a card is inserted.
   */
  public void cardInserted(CardTerminalEvent ctEvent) {
  }

  /**
   * Gets invoked if a card is removed.
   */
  public void cardRemoved(CardTerminalEvent ctEvent) {
    synchronized (monitor) {
      monitor.notifyAll();
    }
  } // cardRemoved

  private void shutdown() throws Exception {
    SmartCard.shutdown();
  }

  private int printTerminalInfos(CardTerminal terminal) {
    // First of all print all information stored about this reader
    System.out.println("Address: " + terminal.getAddress() + "\n" + "Name:    "
        + terminal.getName() + "\n" + "Type:    " + terminal.getType() + "\n"
        + "Slots:   " + terminal.getSlots() + "\n");
    return terminal.getSlots();
  }

  private void printSlotInfos(CardTerminal terminal, int aSlotID) {
    try {
      // First print the ID of the slot
      System.out.println("Info for slot ID: " + aSlotID);
      if (terminal.isCardPresent(aSlotID)) {
        System.out.println("card present: yes");
        // If there is a card in the slot print the ATR the OCF got form this
        // card
        System.out.println("ATR: "
            + HexString.hexify(terminal.getCardID(aSlotID).getATR()));
        // As we do not have a driver for this card we cannot interpret this ATR
      } else
        System.out.println("card present: no");
    } catch (CardTerminalException e) {
      e.printStackTrace();
    }
  }

  private byte[] getAuthKey() {

    return HexString.parseHexString(auth_enc_);

  }

  private byte[] getMacKey() {

    return HexString.parseHexString(mac_);

  }

  private byte[] getPackageId() {

    return HexString.parseHexString(packageid);

  }

  private byte[] getAppletId() {

    return HexString.parseHexString(appletid);

  }

  private CFlex32CardService getLoader() throws Exception {
    if (loader == null) {
      loader = (CFlex32CardService) sm.getCardService(
          CFlex32CardService.class, true);
    }
    return loader;
  }

  public static void main(String[] args) {
    try {
      if (!(args != null && args.length > 0)) {
        usage();
        System.exit(1);
      }
      parseCommandline(args, System.getProperties());
      LoadCFlexAccess32 lcf32 = new LoadCFlexAccess32();
      switch (cmd) {
      case ARG_DELETE:
        lcf32.delete(HexString.parseHexString(delete_));
        break;
      case ARG_LOAD:
        File cap = new File(capname_);
        FileInputStream in = new FileInputStream(cap);
        byte[] buf = new byte[(int) cap.length()];
        in.read(buf);
        if (staticsize_ != null)
          lcf32.load(new ByteArrayInputStream(buf), Integer
              .parseInt(staticsize_));
        else
          lcf32.load(new ByteArrayInputStream(buf), 0);
        break;
      case ARG_INSTALL:
        lcf32.install(Integer.parseInt(instancesize_));
        break;
//      case ARG_SETUP:
//        lcf32.setup(Integer.parseInt(objectsize_));
//        break;
      case ARG_STATUS:
        lcf32.status(HexString.parseHexString(statustype_)[0]);
        break;
      default:
        usage();
        System.exit(1);
      }
      lcf32.shutdown();
      System.exit(0);
    } catch (Exception e) {
      System.err.println(e);
      System.exit(1);
    }
  }

  private static void usage() {
    System.out
        .println("Usage: java "
            + LoadCFlexAccess32.class.getName()
            + " [OPTIONS]\n\n"
            + "Global options:\n"
            + "\t-auth_enc KEY\t\tAuthentication/Encoding key (default 404142434445464748494A4B4C4D4E4F)\n"
            + "\t-mac KEY\t\tMAC key for DAP verification (default 404142434445464748494A4B4C4D4E4F)\n\n"
            + "Load command:\n"
            + "\t-load CAPFILE\t\tload transformed CAP file\n"
            + "\t-statics STATICSSIZE\tstatics size for loadfile installation (default 6)\n"
            + "\t-package_id PKGID\tpackage ID (default A000000001)\n"
            + "\t-applet_id APPID\tapplet ID (default A00000000101)\n\n"
            + "Install command:\n"
            + "\t-install INSTANCESIZE\tallocation size for applet instance (default 21000)\n"
            + "\t-package_id PKGID\tpackage ID (default A000000001)\n"
            + "\t-applet_id APPID\tapplet ID (default A00000000101)\n\n"
            + "Setup command:\n"
            + "\t-setup OBJECTSIZE\tallocation size for objects and keys of CardEdge applet (default 10500)\n"
            + "\t-applet_id APPID\tapplet ID (default A00000000101)\n\n"
            + "Setup options:\n"
            + "\t-transport_key KEY\ttransport key of CardEdge instance (default Muscle00)\n"
            + "\t-chv0 KEY\t\tCHV0 (PIN 0) of CardEdge instance (default Muscle00)\n"
            + "\t-ublk_chv0 KEY\t\tunblock CHV0 (PIN 0) of CardEdge instance (default Muscle00)\n"
            + "\t-chv1 KEY\t\tCHV1 (PIN 1 or 'User PIN') of CardEdge instance  (default 00000000)\n"
            + "\t-ublk_chv1 KEY\t\tunblock CHV1 (PIN 1) of CardEdge instance (default 11111111)\n\n"
            + "Other commands:\n"
            + "\t-status TYPE (in hexadecimal)\t\tdisplay status APDU (default Hex 20)\n"
            + "\t-delete ID\t\tdelete an instance or package (no default)\n");
  }

  private SmartCard sm;

  private CFlex32CardService loader = null;

  private final static Object monitor = "synchronization monitor";

  /*
   * //simplepurse private static final byte[] packageid = {(byte)0x11,
   * (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, (byte)0x66 }; private
   * static final byte[] appletid = {(byte)0x11, (byte)0x22, (byte)0x33,
   * (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77 }; //wallet private static
   * final byte[] packageid = {(byte)0xa0, (byte)0x00, (byte)0x00, (byte)0x00,
   * (byte)0x62, (byte)0x03, (byte)0x01, (byte)0x0C, (byte)0x06 }; private
   * static final byte[] appletid = {(byte)0xa0, (byte)0x00, (byte)0x00,
   * (byte)0x00, (byte)0x62, (byte)0x03, (byte)0x01, (byte)0x0C, (byte)0x06,
   * (byte)0x01 };
   */

  // cardedge
  static String packageid = "A000000001";

  static String appletid = "A00000000101";

  static byte[] acl = { (byte) 0x00, (byte) 0x02, (byte) 0x01 };

  // command line stuff

  static String capname_ = "CardEdge.bin";

  static String staticsize_ = "6";

  static String instancesize_ = "21000";

  static String objectsize_ = "10500";

  static String statustype_ = "20";

  static String delete_ = null;

  static String auth_enc_ = "404142434445464748494A4B4C4D4E4F";

  static String mac_ = auth_enc_;

  static String transportKey = "Muscle00";

  static String chv0 = transportKey;

  static String ublkchv0 = transportKey;

  static String chv1 = "00000000";

  static String ublkchv1 = "11111111";

  static int cmd = -1;

  private final static int ARG_LOAD = 1;

  private final static int ARG_STATICS = 2;

  private final static int ARG_INSTALL = 3;

  private final static int ARG_SETUP = 4;

  private final static int ARG_STATUS = 5;

  private final static int ARG_DELETE = 6;

  private final static int ARG_AUTH_ENC = 7;

  private final static int ARG_MAC = 8;

  private final static int ARG_TKEY = 9;

  private final static int ARG_CHV0 = 10;

  private final static int ARG_UBLKCHV0 = 11;

  private final static int ARG_CHV1 = 12;

  private final static int ARG_UBLKCHV1 = 13;

  private final static int ARG_PKGID = 14;

  private final static int ARG_APPID = 15;

  private final static String[] ARGOPTIONS = { "-load", "-statics", "-install",
      "-setup", "-status", "-delete", "-auth_enc", "-mac", "-transport_key",
      "-chv0", "-ublock_chv0", "-chv1", "-ublock_chv1", "-package_id",
      "-applet_id" };

  private final static int[] ARGTAGS = { ARG_LOAD, ARG_STATICS, ARG_INSTALL,
      ARG_SETUP, ARG_STATUS, ARG_DELETE, ARG_AUTH_ENC, ARG_MAC, ARG_TKEY,
      ARG_CHV0, ARG_UBLKCHV0, ARG_CHV1, ARG_UBLKCHV1, ARG_PKGID, ARG_APPID };

  static void parseCommandline(String[] args, Properties props) {
    CmdLineParser cmdlparser = new CmdLineParser(args, ARGOPTIONS, ARGTAGS);
    String cmderror = "Invalid command line: ";
    String cmderrmissingval = cmderror + "missing value after ";

    while (cmdlparser.more()) {
      int option = cmdlparser.getNext();

      switch (option) {
      case ARG_LOAD:
        cmd = ARG_LOAD;
        if (cmdlparser.more())
          capname_ = cmdlparser.nextArgString();
        break;
      case ARG_STATICS:
        if (cmdlparser.more())
          staticsize_ = cmdlparser.nextArgString();
        break;
      case ARG_INSTALL:
        cmd = ARG_INSTALL;
        if (cmdlparser.more())
          instancesize_ = cmdlparser.nextArgString();
        break;
      case ARG_SETUP:
        cmd = ARG_SETUP;
        if (cmdlparser.more())
          objectsize_ = cmdlparser.nextArgString();
        break;
      case ARG_STATUS:
        cmd = ARG_STATUS;
        if (cmdlparser.more())
          statustype_ = cmdlparser.nextArgString();
        break;
      case ARG_DELETE:
        cmd = ARG_DELETE;
        if (cmdlparser.more()) {
          delete_ = cmdlparser.nextArgString();
        } else {
          System.out.println(cmderrmissingval + cmdlparser.currentArgString());
          usage();
          System.exit(1);
        }
        break;

      case ARG_AUTH_ENC:
        if (cmdlparser.more())
          auth_enc_ = cmdlparser.nextArgString();
        break;
      case ARG_MAC:
        if (cmdlparser.more())
          mac_ = cmdlparser.nextArgString();
        break;
      case ARG_TKEY:
        if (cmdlparser.more())
          transportKey = cmdlparser.nextArgString();
        break;
      case ARG_CHV0:
        if (cmdlparser.more())
          chv0 = cmdlparser.nextArgString();
        break;
      case ARG_UBLKCHV0:
        if (cmdlparser.more())
          ublkchv0 = cmdlparser.nextArgString();
        break;
      case ARG_CHV1:
        if (cmdlparser.more())
          chv1 = cmdlparser.nextArgString();
        break;
      case ARG_UBLKCHV1:
        if (cmdlparser.more())
          ublkchv1 = cmdlparser.nextArgString();
        break;
      case ARG_PKGID:
        if (cmdlparser.more())
          packageid = cmdlparser.nextArgString();
        break;
      case ARG_APPID:
        if (cmdlparser.more())
          appletid = cmdlparser.nextArgString();
        break;

      default: {
        String arg = cmdlparser.currentArgString();
        if (arg.startsWith("-") && arg.length() > 1) {
          System.out.println("warning: unknown option " + arg);
          break;
        }
      }
      } // switch arg
    } // for each command line argument

  } // parseCommandline

  static class CmdLineParser {
    public final static int UNKNOWN_OPTION = -1;

    public final static int OTHER_ARGUMENT = -2;

    public final static int NO_MORE_ARGS = -3;

    private String[] options_;

    private int[] tags_;

    private int argc_;

    private String[] argv_;

    private int cur_ = -1;

    public CmdLineParser(String[] argv, String[] options, int[] tags) {
      options_ = options;
      tags_ = tags;
      argc_ = argv.length;
      argv_ = argv;
    }

    public boolean more() {
      return (cur_ + 1 < argc_);
    }

    public int getNext() {
      if (!(++cur_ < argc_))
        return NO_MORE_ARGS;

      String curarg = argv_[cur_];

      if (options_ != null)
        for (int i = 0; i < options_.length; i++)
          if (curarg.equals(options_[i]))
            return tags_[i];

      if (curarg.startsWith("-"))
        return UNKNOWN_OPTION;

      return OTHER_ARGUMENT;
    }

    public String currentArgString() {
      return (cur_ < argc_) ? argv_[cur_] : null;
    }

    public String nextArgString() {
      return (++cur_ < argc_) ? argv_[cur_] : null;
    }
  }
}
