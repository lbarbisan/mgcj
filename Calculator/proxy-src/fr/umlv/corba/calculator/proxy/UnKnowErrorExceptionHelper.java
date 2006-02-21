package fr.umlv.corba.calculator.proxy;


/**
* fr/umlv/corba/calculator/proxy/UnKnowErrorExceptionHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* mardi 21 février 2006 20 h 14 CET
*/

abstract public class UnKnowErrorExceptionHelper
{
  private static String  _id = "IDL:proxy/UnKnowErrorException:1.0";

  public static void insert (org.omg.CORBA.Any a, fr.umlv.corba.calculator.proxy.UnKnowErrorException that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static fr.umlv.corba.calculator.proxy.UnKnowErrorException extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "message",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.id (), "UnKnowErrorException", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static fr.umlv.corba.calculator.proxy.UnKnowErrorException read (org.omg.CORBA.portable.InputStream istream)
  {
    fr.umlv.corba.calculator.proxy.UnKnowErrorException value = new fr.umlv.corba.calculator.proxy.UnKnowErrorException ();
    // read and discard the repository ID
    istream.read_string ();
    value.message = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, fr.umlv.corba.calculator.proxy.UnKnowErrorException value)
  {
    // write the repository ID
    ostream.write_string (id ());
    ostream.write_string (value.message);
  }

}
