package fr.umlv.corba.calculator.proxy;


/**
* fr/umlv/corba/calculator/proxy/ArithmeticException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* samedi 25 février 2006 19 h 16 CET
*/

public final class ArithmeticException extends org.omg.CORBA.UserException
{

  //pour la division par 0
  public String message = null;

  public ArithmeticException ()
  {
    super(ArithmeticExceptionHelper.id());
  } // ctor

  public ArithmeticException (String _message)
  {
    super(ArithmeticExceptionHelper.id());
    message = _message;
  } // ctor


  public ArithmeticException (String $reason, String _message)
  {
    super(ArithmeticExceptionHelper.id() + "  " + $reason);
    message = _message;
  } // ctor

} // class ArithmeticException
