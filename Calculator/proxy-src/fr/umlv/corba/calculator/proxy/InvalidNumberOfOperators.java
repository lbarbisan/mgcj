package fr.umlv.corba.calculator.proxy;


/**
* fr/umlv/corba/calculator/proxy/InvalidNumberOfOperators.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* samedi 25 février 2006 19 h 16 CET
*/

public final class InvalidNumberOfOperators extends org.omg.CORBA.UserException
{
  public String message = null;

  public InvalidNumberOfOperators ()
  {
    super(InvalidNumberOfOperatorsHelper.id());
  } // ctor

  public InvalidNumberOfOperators (String _message)
  {
    super(InvalidNumberOfOperatorsHelper.id());
    message = _message;
  } // ctor


  public InvalidNumberOfOperators (String $reason, String _message)
  {
    super(InvalidNumberOfOperatorsHelper.id() + "  " + $reason);
    message = _message;
  } // ctor

} // class InvalidNumberOfOperators
