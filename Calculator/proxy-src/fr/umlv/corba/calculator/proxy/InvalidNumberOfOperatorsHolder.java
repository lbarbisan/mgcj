package fr.umlv.corba.calculator.proxy;

/**
* fr/umlv/corba/calculator/proxy/InvalidNumberOfOperatorsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* samedi 25 février 2006 19 h 16 CET
*/

public final class InvalidNumberOfOperatorsHolder implements org.omg.CORBA.portable.Streamable
{
  public fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators value = null;

  public InvalidNumberOfOperatorsHolder ()
  {
  }

  public InvalidNumberOfOperatorsHolder (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.type ();
  }

}
