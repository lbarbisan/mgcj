package fr.umlv.corba.calculator.proxy;


/**
* fr/umlv/corba/calculator/proxy/UnKnowErrorException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* mardi 21 février 2006 20 h 14 CET
*/

public final class UnKnowErrorException extends org.omg.CORBA.UserException
{
  public String message = null;

  public UnKnowErrorException ()
  {
    super(UnKnowErrorExceptionHelper.id());
  } // ctor

  public UnKnowErrorException (String _message)
  {
    super(UnKnowErrorExceptionHelper.id());
    message = _message;
  } // ctor


  public UnKnowErrorException (String $reason, String _message)
  {
    super(UnKnowErrorExceptionHelper.id() + "  " + $reason);
    message = _message;
  } // ctor

} // class UnKnowErrorException