package fr.umlv.corba.calculator.proxy;

/**
* fr/umlv/corba/calculator/proxy/ArithmeticExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* samedi 25 février 2006 17 h 03 CET
*/

public final class ArithmeticExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public fr.umlv.corba.calculator.proxy.ArithmeticException value = null;

  public ArithmeticExceptionHolder ()
  {
  }

  public ArithmeticExceptionHolder (fr.umlv.corba.calculator.proxy.ArithmeticException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = fr.umlv.corba.calculator.proxy.ArithmeticExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    fr.umlv.corba.calculator.proxy.ArithmeticExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return fr.umlv.corba.calculator.proxy.ArithmeticExceptionHelper.type ();
  }

}
