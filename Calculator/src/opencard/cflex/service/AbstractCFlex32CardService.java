/*
 * Created on 25 oct. 2005 by roussel
 */
package opencard.cflex.service;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Random;

import opencard.cflex.util.Crypto;
import opencard.core.service.*;
import opencard.core.terminal.*;
import opencard.core.util.*;
import opencard.opt.terminal.ISOCommandAPDU;


public abstract class AbstractCFlex32CardService extends CardService implements CFlex32CardService {

  private InputStream in = null;

  private int MAX_LOADFILE_BLOCK_LENGTH = 255 - 8;

  // zero byte constant (very useful!)
  private final static byte BZ = (byte) 0x00;

  // reserved for future use
  private final static byte RFU = BZ;

  public static byte CHLG_LEN, CRYPT_LEN, MAC_LEN;

  static {
    CHLG_LEN = CRYPT_LEN = MAC_LEN = (byte) 0x08;
  }

  // class bytes
  final byte ISO_CLA = BZ;

  final static byte OP_CLA = (byte) 0x80;

  final static byte OP_SEC_CLA = (byte) 0x84;

  final static byte MSC_CLA = (byte) 0xB0;

  // instructions
  final static byte SECURITY = (byte) 0x01;

  final static byte INS_SELECT_APP = (byte) 0xA4;

  final static byte INS_DELETE = (byte) 0xE4;

  final static byte INS_INIT_UPDATE = (byte) 0x50;

  final static byte INS_EXT_AUTH = (byte) 0x82;

  final byte INS_INSTALL = (byte) 0xE6;

  final byte INS_LOAD = (byte) 0xE8;

  final byte INS_STATUS = (byte) 0xF2;

  final byte INS_MSC_SETUP = (byte) 0x2A;

  // p1 values
  final byte P1_APPL = (byte) 0x04;

  final byte P1_DEF_KEYSET = BZ;

  final byte P1_DEF_SEC_LEVEL = BZ;

  final byte P1_INSTALL_LOAD = (byte) 0x02;

  final byte P1_INSTALL_INSTALL = (byte) 0x04;

  final byte P1_INSTALL_MAKESELECTABLE = (byte) 0x08;

  // p2 values
  final byte P2_DEF_KEYINDEX = BZ;

  // lc values
  final byte LC_CHLG_LENGTH = CHLG_LEN;

  final byte LC_EXT_AUTH_LENGTH = (byte) (CRYPT_LEN + MAC_LEN);

  // input data

  // le values
  final byte LE_INIT_UPDATE_LENGTH = (byte) 0x1C;

  byte[] next_icv, mac_session_key, session_key;


  protected abstract byte[] getCardManagerAID();

  public void selectApplication(byte[] aid) throws CardServiceException {

    allocateCardChannel();
    try {

      byte[] cmd = { ISO_CLA, INS_SELECT_APP, P1_APPL, BZ, (byte) aid.length };

      byte[] selectApplication = new byte[cmd.length + aid.length];
      int offset = 0;
      System.arraycopy(cmd, 0, selectApplication, offset, cmd.length);
      offset += cmd.length;
      System.arraycopy(aid, 0, selectApplication, offset, aid.length);
      offset += aid.length;

      ResponseAPDU resp = sendAPDU(new CommandAPDU(selectApplication));
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to select application: " + resp);
      }

    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }
  }

  public void deleteApplication(byte[] aid) throws CardServiceException {
    allocateCardChannel();
    try {
      byte[] inputData = new byte[1 + 1 + aid.length];
      int offset = 0;
      inputData[offset++] = (byte) 0x4F;
      inputData[offset++] = (byte) aid.length;

      System.arraycopy(aid, 0, inputData, offset, aid.length);

      byte[] cmd = { OP_SEC_CLA, INS_DELETE, BZ, BZ,
          (byte) (inputData.length + 8) };

      byte[] deleteApplication = new byte[cmd.length + inputData.length];

      offset = 0;
      System.arraycopy(cmd, 0, deleteApplication, offset, cmd.length);
      offset += cmd.length;
      System.arraycopy(inputData, 0, deleteApplication, offset,
          inputData.length);
      offset += inputData.length;

      byte[] command_mac = Crypto.calculateMAC(mac_session_key,
          deleteApplication, next_icv);

      next_icv = command_mac;
      ctracer.debug("deleteApplication", "Command for MAC: "
          + HexString.hexify(deleteApplication));
      ctracer.debug("deleteApplication", "Command MAC: "
          + HexString.hexify(command_mac));
      byte[] delete = new byte[deleteApplication.length + command_mac.length];

      System.arraycopy(deleteApplication, 0, delete, 0,
          deleteApplication.length);
      System.arraycopy(command_mac, 0, delete, deleteApplication.length,
          command_mac.length);

      ResponseAPDU resp = sendAPDU(new CommandAPDU(delete));
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to delete AID "
            + HexString.hexify(aid) + " Response: " + resp);
      }

    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } catch (Exception e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }

  }

  public void selectCardManager() throws CardServiceException {
    allocateCardChannel();
    try {
      byte[] cmd = { ISO_CLA, INS_SELECT_APP, P1_APPL, BZ,
          (byte) getCardManagerAID().length };
      byte[] selectCardmanager = new byte[cmd.length
          + getCardManagerAID().length
      // + 1
      ];
      int offset = 0;
      System.arraycopy(cmd, 0, selectCardmanager, offset, cmd.length);
      offset += cmd.length;
      System.arraycopy(getCardManagerAID(), 0, selectCardmanager, offset,
          getCardManagerAID().length);
      offset += getCardManagerAID().length;

      // selectCardmanager[offset] = expected;

      System.out.println("selectCardmanager1");
      ResponseAPDU resp = sendAPDU(new CommandAPDU(selectCardmanager));
      System.out.println("selectCardmanager2");
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to Select CardManager: " + resp);
      }

      // MAX_LOADFILE_BLOCK_LENGTH = resp.data()[expected-1];

    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        System.out.println("selectCardmanager2");
        // releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());

      }
    }
  }

  public void createSecureChannel(byte[] key_enc_auth, byte[] key_mac)
      throws CardServiceException {
    allocateCardChannel();
    try {
      byte [] host_challenge = createHostChallenge();
      byte[] initializeUpdate = { OP_CLA, INS_INIT_UPDATE, P1_DEF_KEYSET,
          P2_DEF_KEYINDEX, LC_CHLG_LENGTH, host_challenge[0],
          host_challenge[1], host_challenge[2], host_challenge[3],
          host_challenge[4], host_challenge[5], host_challenge[6],
          host_challenge[7], LE_INIT_UPDATE_LENGTH };
      ResponseAPDU resp = sendAPDU(new CommandAPDU(initializeUpdate));
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to Send InitializeUpdate: "
            + resp);
      }

      byte[] card_challenge = new byte[CHLG_LEN];
      System.arraycopy(resp.data(), 12, card_challenge, 0, CHLG_LEN);
      byte[] card_cryptogram = new byte[CRYPT_LEN];
      System.arraycopy(resp.data(), 20, card_cryptogram, 0, CRYPT_LEN);

      mac_session_key = Crypto.getSessionKey(key_mac, host_challenge,
          card_challenge);

      session_key = Crypto.getSessionKey(key_enc_auth, host_challenge,
          card_challenge);

      if (!(Crypto.verifyCardCryptogram(session_key, host_challenge,
          card_challenge, card_cryptogram))) {
        throw new CardServiceException("Unable to verify card cryptogram");
      }

      byte[] host_cryptogram = Crypto.calculateHostCryptogram(session_key,
          card_challenge, host_challenge);

      ctracer.debug("createSecureChannel", "Host crypto: "
          + HexString.hexify(host_cryptogram));

      byte[] command = { OP_SEC_CLA, INS_EXT_AUTH, SECURITY, RFU,
          LC_EXT_AUTH_LENGTH, host_cryptogram[0], host_cryptogram[1],
          host_cryptogram[2], host_cryptogram[3], host_cryptogram[4],
          host_cryptogram[5], host_cryptogram[6], host_cryptogram[7] };

      byte[] iv = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
          (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

      byte[] command_mac = Crypto
          .calculateMAC(mac_session_key, command, iv);

      ctracer.debug("createSecureChannel", "Command for MAC: "
          + HexString.hexify(command));
      ctracer.debug("createSecureChannel", "Command MAC: "
          + HexString.hexify(command_mac));

      byte[] externalAuthenticate = { command[0], command[1], command[2],
          command[3], command[4], command[5], command[6], command[7],
          command[8], command[9], command[10], command[11], command[12],
          command_mac[0], command_mac[1], command_mac[2], command_mac[3],
          command_mac[4], command_mac[5], command_mac[6], command_mac[7] };

      next_icv = command_mac;

      resp = sendAPDU(new CommandAPDU(externalAuthenticate));
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to Send External Authenticate: "
            + resp);
      }
    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } catch (Exception e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }
    return;
  }

  public void allocateChannel() {
    super.allocateCardChannel();
  }

  public void releaseChannel() {
    super.releaseCardChannel();
  }

  public void installLoad(byte[] packageid, InputStream in, int staticsize)
      throws CardServiceException {

    this.in = in;

    allocateCardChannel();
    try {

      int capsize = in.available();
      capsize += staticsize;
      int hibyte = capsize >> 8;
      int lobyte = capsize - (hibyte << 8);
      byte[] params = { (byte) 0xEF, (byte) 0x04, (byte) 0xC6, (byte) 0x02,
          (byte) hibyte, (byte) lobyte };

      byte[] loadInput = new byte[1 + // package aid length value
          packageid.length + // package aid
          1 + // security domain id length
          0 + // security domain id value (default 00h)
          1 + // load file hash length value
          0 + // load file hash value (default 00h)
          1 + // params length value
          params.length + 1 // load token length value (00h)
      ];

      byte[] cmd = { OP_SEC_CLA, INS_INSTALL, P1_INSTALL_LOAD, RFU };
      int offset = 0;
      loadInput[offset++] = (byte) packageid.length;

      System.arraycopy(packageid, 0, loadInput, offset, packageid.length);
      offset += packageid.length;

      loadInput[offset++] = BZ;
      loadInput[offset++] = BZ;
      loadInput[offset++] = (byte) params.length;

      System.arraycopy(params, 0, loadInput, offset, params.length);
      offset += params.length;

      loadInput[offset++] = BZ;

      byte[] installLoad = new byte[cmd.length + 1 + // inputLoad length value
          loadInput.length];
      offset = 0;
      System.arraycopy(cmd, 0, installLoad, offset, cmd.length);
      offset += cmd.length;
      installLoad[offset++] = (byte) (loadInput.length + 8);

      System.arraycopy(loadInput, 0, installLoad, offset, loadInput.length);

      byte[] command_mac = Crypto.calculateMAC(mac_session_key,
          installLoad, next_icv);

      next_icv = command_mac;

      ctracer.debug("installLoad", "Command for MAC: "
          + HexString.hexify(installLoad));
      ctracer.debug("installLoad", "Command MAC: "
          + HexString.hexify(command_mac));

      byte[] final_installLoad = new byte[installLoad.length + 8];
      System
          .arraycopy(installLoad, 0, final_installLoad, 0, installLoad.length);
      System.arraycopy(command_mac, 0, final_installLoad, installLoad.length,
          command_mac.length);

      ResponseAPDU resp = sendAPDU(new CommandAPDU(final_installLoad));
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to Send Install:Load: " + resp);
      }
    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } catch (Exception e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }
    return;
  }

  public void load() throws CardServiceException {

    allocateCardChannel();
    try {
      byte[] cmd = { OP_SEC_CLA, INS_LOAD };

      byte[] loadCommand = new byte[cmd.length + 1 + 1 + 1
          + MAX_LOADFILE_BLOCK_LENGTH];

      int cmdoffset = 0;
      System.arraycopy(cmd, 0, loadCommand, cmdoffset, cmd.length);
      cmdoffset += cmd.length;

      int p1 = cmdoffset++;
      int p2 = cmdoffset++;

      int lc = cmdoffset++;

      loadCommand[lc] = (byte) (MAX_LOADFILE_BLOCK_LENGTH + 8);

      byte[] loadFileBlock = new byte[MAX_LOADFILE_BLOCK_LENGTH];
      int p2_blockcount = 0;

      // first block
      loadCommand[p1] = (byte) 0x00;// first/intermediate block
      loadCommand[p2] = (byte) p2_blockcount++;// first block num
      int fboffset = 0;
      loadFileBlock[fboffset++] = (byte) 0xC4;// cap file follows tag
      int capsize = this.in.available();
      if (capsize < 128) {
        loadFileBlock[fboffset++] = (byte) capsize;// cap file < 128 byte
      } else if (capsize >= 128 && capsize < 256) {
        loadFileBlock[fboffset++] = (byte) 0x81;// cap file < 256 byte
        loadFileBlock[fboffset++] = (byte) capsize;
      } else if (capsize >= 256 && capsize < 65535) {
        loadFileBlock[fboffset++] = (byte) 0x82;// ...
        int hibyte = capsize >> 8;
        int lobyte = capsize - (hibyte << 8);
        loadFileBlock[fboffset++] = (byte) hibyte;
        loadFileBlock[fboffset++] = (byte) lobyte;
      }

      int capoffset = 0;
      byte[] firstbytes = new byte[loadFileBlock.length - fboffset];
      this.in.read(firstbytes);

      System.arraycopy(firstbytes, capoffset, loadFileBlock, fboffset,
          loadFileBlock.length - fboffset);
      capoffset += loadFileBlock.length - fboffset;

      int loadFileBlockCmdOffset = cmdoffset;

      System.arraycopy(loadFileBlock, 0, loadCommand, cmdoffset,
          loadFileBlock.length);
      cmdoffset += loadFileBlock.length;

      byte[] command_mac = Crypto.calculateMAC(mac_session_key,
          loadCommand, next_icv);

      next_icv = command_mac;

      byte[] final_load = new byte[loadCommand.length + 8];

      System.arraycopy(loadCommand, 0, final_load, 0, loadCommand.length);
      System.arraycopy(command_mac, 0, final_load, loadCommand.length,
          command_mac.length);

      ResponseAPDU resp = sendAPDU(new CommandAPDU(final_load));
      System.out.print("Sending");
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to send first load file block: "
            + resp);
      }

      int blockcount = (capsize - capoffset) / MAX_LOADFILE_BLOCK_LENGTH;
      int numlastblockbytes = (capsize - capoffset) % MAX_LOADFILE_BLOCK_LENGTH;
      byte[] capbytes = new byte[MAX_LOADFILE_BLOCK_LENGTH];
      for (int i = 0; i < blockcount; i++) {
        Thread.sleep(500);
        loadCommand[p2] = (byte) p2_blockcount++;// block num
        this.in.read(capbytes);
        System.arraycopy(capbytes, 0, loadCommand, loadFileBlockCmdOffset,
            MAX_LOADFILE_BLOCK_LENGTH);

        // System.out.println(HexString.hexify(loadCommand));

        command_mac = Crypto.calculateMAC(mac_session_key, loadCommand,
            next_icv);
        next_icv = command_mac;

        System.arraycopy(loadCommand, 0, final_load, 0, loadCommand.length);
        System.arraycopy(command_mac, 0, final_load, loadCommand.length,
            command_mac.length);

        resp = sendAPDU(new CommandAPDU(final_load));
        System.out.print(".");
        if (resp.sw() != 0x9000) {
          throw new CardServiceException(
              "Unable to send intermediate load file block");
        }

      }
      Thread.sleep(500);

      byte[] lastLoadCommand = new byte[cmd.length + 1 + 1 + 1
          + numlastblockbytes];

      System.arraycopy(cmd, 0, lastLoadCommand, 0, cmd.length);

      lastLoadCommand[p1] = (byte) 0x80; // final load block flag
      lastLoadCommand[p2] = (byte) p2_blockcount;// block num
      lastLoadCommand[lc] = (byte) (numlastblockbytes + 8);

      capbytes = new byte[this.in.available()];
      this.in.read(capbytes);

      System.arraycopy(capbytes, 0, lastLoadCommand, loadFileBlockCmdOffset,
          capbytes.length);
      command_mac = Crypto.calculateMAC(mac_session_key, lastLoadCommand,
          next_icv);
      next_icv = command_mac;

      byte[] final_lastLoadCommand = new byte[lastLoadCommand.length + 8];

      System.arraycopy(lastLoadCommand, 0, final_lastLoadCommand, 0,
          lastLoadCommand.length);
      System.arraycopy(command_mac, 0, final_lastLoadCommand,
          lastLoadCommand.length, command_mac.length);

      resp = sendAPDU(new CommandAPDU(final_lastLoadCommand));
      System.out.println();
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to send last load file block: "
            + resp);
      }
    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } catch (Exception e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }
    return;
  }

  public void install(byte[] packageid, byte[] appletid, byte[] instanceaid,
      int instancesize) throws CardServiceException {
    allocateCardChannel();
    try {
      byte privmask = (byte) 0x02; // ???

      int hibyte = instancesize >> 8;
      int lobyte = instancesize - (hibyte << 8);

      byte[] params = {
      /*
       * (byte)0xC9, (byte)0x01, (byte)0x00, (byte)0xEF, (byte)0x04, (byte)0xC8,
       * (byte)0x02, (byte)hibyte, (byte)lobyte
       */
      (byte) 0xEF, (byte) 0x04, (byte) 0xC8, (byte) 0x02, (byte) hibyte,
          (byte) lobyte, (byte) 0xC9, (byte) 0x01, (byte) 0x00 };

      byte[] loadInput = new byte[1 + // packageid length value
          packageid.length + // load file package id
          1 + // load file applet id length value
          appletid.length + // load file applet id
          1 + // explicit applid length, if not explicitly different (00h)
          instanceaid.length + // not different from load file applet id -> no
                                // value
          1 + // privileges length value (01h)
          1 + // privileges byte
          1 + // params length value
          params.length + // params bytes
          1 // return token length value (00h)
      ];

      int offset = 0;
      loadInput[offset++] = (byte) packageid.length;

      System.arraycopy(packageid, 0, loadInput, offset, packageid.length);
      offset += packageid.length;

      loadInput[offset++] = (byte) appletid.length;

      System.arraycopy(appletid, 0, loadInput, offset, appletid.length);
      offset += appletid.length;

      loadInput[offset++] = (byte) instanceaid.length;

      System.arraycopy(instanceaid, 0, loadInput, offset, instanceaid.length);
      offset += instanceaid.length;

      // loadInput[offset++] = (byte)BZ;
      loadInput[offset++] = (byte) 0x01;
      loadInput[offset++] = privmask;

      loadInput[offset++] = (byte) params.length;

      System.arraycopy(params, 0, loadInput, offset, params.length);
      offset += params.length;

      loadInput[offset++] = BZ;

      byte[] cmd = { OP_SEC_CLA, INS_INSTALL,
          P1_INSTALL_INSTALL | P1_INSTALL_MAKESELECTABLE, RFU };

      byte[] install = new byte[cmd.length + 1 + // Lc
          loadInput.length
      // + 1 // Le
      ];

      offset = 0;
      System.arraycopy(cmd, 0, install, offset, cmd.length);
      offset += cmd.length;

      install[offset++] = (byte) (loadInput.length + 8/* +1 */);

      System.arraycopy(loadInput, 0, install, offset, loadInput.length);
      offset += loadInput.length;

      // install[offset] = (byte)0x01;
      byte[] command_mac = Crypto.calculateMAC(mac_session_key, install,
          next_icv);
      next_icv = command_mac;

      byte[] final_install = new byte[install.length + 8];

      System.arraycopy(install, 0, final_install, 0, install.length);
      System.arraycopy(command_mac, 0, final_install, install.length,
          command_mac.length);

      ResponseAPDU resp = sendAPDU(new CommandAPDU(final_install));
      if (resp.sw() != 0x9000) {
        throw new CardServiceException("Unable to Send Install: " + resp);
      }
    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } catch (Exception e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }
    return;
  }


  public ResponseAPDU status(int type) throws CardServiceException {
    allocateCardChannel();
    try {
      byte[] cmd = { OP_SEC_CLA, INS_STATUS, (byte) type, BZ, (byte) 0x0A,
          (byte) 0x4F, BZ };

      byte[] command_mac = Crypto.calculateMAC(mac_session_key, cmd,
          next_icv);

      ctracer.debug("getStatus", "Command for MAC: " + HexString.hexify(cmd));
      ctracer.debug("getStatus", "Command MAC: "
          + HexString.hexify(command_mac));

      next_icv = command_mac;

      byte[] status = { cmd[0], cmd[1], cmd[2], cmd[3], cmd[4], cmd[5], cmd[6],
          command_mac[0], command_mac[1], command_mac[2], command_mac[3],
          command_mac[4], command_mac[5], command_mac[6], command_mac[7] };

      ResponseAPDU resp = sendAPDU(new CommandAPDU(status));
      switch(resp.sw()) {
      case 0x9000:
      case 0x6A88:
        return resp;
      default:
        throw new CardServiceException("Unable to get status: " + resp);          
      }
    } catch (CardTerminalException e) {
      throw new CardServiceException(e.toString());
    } catch (Exception e) {
      throw new CardServiceException(e.toString());
    } finally {
      try {
        releaseCardChannel();
      } catch (CardServiceRuntimeException e) {
        throw new CardServiceException(e.toString());
      }
    }
  }

  /**
   * Method to send an APDU and get the response
   * 
   * 
   * @param apdu
   *          to send.
   * 
   * @return the response APDU
   * 
   * @exception CardTerminalException
   *              is raised if the APDU could not be send or received.
   * 
   */
  public ResponseAPDU sendAPDU(CommandAPDU commandAPDU) throws CardTerminalException {

    ctracer.debug("sendAPDU",
        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>> Start Transaction Number "
            + ++transactionNumber);
    ctracer.debug("sendAPDU", commandAPDU.toString());

    if (getCardChannel() == null)
      throw new CardTerminalException("Card channel  is not available");

    ResponseAPDU responseAPDU = getCardChannel().sendCommandAPDU(commandAPDU);

    if (responseAPDU == null)
      throw new CardTerminalException("ResponseAPDU is not available");

    ctracer.debug("sendAPDU", responseAPDU.toString());

    if (responseAPDU.sw1() == (byte) 0x61) {
      ISOCommandAPDU apduToSend = new ISOCommandAPDU((byte) 0x00, (byte) 0xC0,
          (byte) 0x00, (byte) 0x00, (int) responseAPDU.sw2());

      responseAPDU = getCardChannel().sendCommandAPDU(apduToSend);
      ctracer.debug("GetResponse", responseAPDU.toString());

    }

    ctracer.debug("sendAPDU",
        "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< End Transaction Number "
            + transactionNumber);

    return (responseAPDU);
  }

  private byte[] createHostChallenge() {
    byte[] buf = new byte[CHLG_LEN];
    byte[] seed = Long.toString(System.currentTimeMillis()).getBytes();
    Random rand = new SecureRandom(seed);
    rand.nextBytes(buf);
    return buf;
  }

  private int transactionNumber = 0;

  private static Tracer ctracer = new Tracer(CFlex32CardService.class);

  
}
