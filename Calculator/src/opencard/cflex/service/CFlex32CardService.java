/*
 * Created on 25 oct. 2005 by roussel
 */
package opencard.cflex.service;

import java.io.InputStream;

import opencard.core.service.CardServiceException;
import opencard.core.terminal.*;
import opencard.core.terminal.ResponseAPDU;


public interface CFlex32CardService {
  public void selectCardManager() throws CardServiceException ;
  
  public void selectApplication(byte[] aid) throws CardServiceException; 
  
  public void deleteApplication(byte[] aid) throws CardServiceException ;
  
  public void createSecureChannel(byte[] key_enc_auth, byte[] mac_)  throws  CardServiceException ;    
  
  public void allocateChannel();
  
  public void releaseChannel();

  public void installLoad(byte[] packageid, InputStream in, int staticsize) throws CardServiceException ;
  
  public void load() throws CardServiceException ;
          
  public void install(byte[] packageid,
                      byte[] appletid,
                      byte[] instanceaid,
                      int instancesize) throws CardServiceException ;
  
  public ResponseAPDU status(int type) throws CardServiceException;
  
  public ResponseAPDU sendAPDU(CommandAPDU cmd) throws CardTerminalException;
}
