package fr.umlv.corba.calculator.proxy;


/**
* fr/umlv/corba/calculator/proxy/CorbaCalculatorPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from idl/calculator.idl
* samedi 25 février 2006 19 h 16 CET
*/

public abstract class CorbaCalculatorPOA extends org.omg.PortableServer.Servant
 implements fr.umlv.corba.calculator.proxy.CorbaCalculatorOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("push", new java.lang.Integer (0));
    _methods.put ("pop", new java.lang.Integer (1));
    _methods.put ("top", new java.lang.Integer (2));
    _methods.put ("add", new java.lang.Integer (3));
    _methods.put ("sub", new java.lang.Integer (4));
    _methods.put ("mult", new java.lang.Integer (5));
    _methods.put ("div", new java.lang.Integer (6));
    _methods.put ("reset", new java.lang.Integer (7));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // proxy/CorbaCalculator/push
       {
         try {
           short value = in.read_short ();
           this.push (value);
           out = $rh.createReply();
         } catch (fr.umlv.corba.calculator.proxy.ArithmeticException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.ArithmeticExceptionHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 1:  // proxy/CorbaCalculator/pop
       {
         try {
           short $result = (short)0;
           $result = this.pop ();
           out = $rh.createReply();
           out.write_short ($result);
         } catch (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 2:  // proxy/CorbaCalculator/top
       {
         try {
           short $result = (short)0;
           $result = this.top ();
           out = $rh.createReply();
           out.write_short ($result);
         } catch (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 3:  // proxy/CorbaCalculator/add
       {
         try {
           this.add ();
           out = $rh.createReply();
         } catch (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 4:  // proxy/CorbaCalculator/sub
       {
         try {
           this.sub ();
           out = $rh.createReply();
         } catch (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 5:  // proxy/CorbaCalculator/mult
       {
         try {
           this.mult ();
           out = $rh.createReply();
         } catch (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 6:  // proxy/CorbaCalculator/div
       {
         try {
           this.div ();
           out = $rh.createReply();
         } catch (fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.InvalidNumberOfOperatorsHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.ArithmeticException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.ArithmeticExceptionHelper.write (out, $ex);
         } catch (fr.umlv.corba.calculator.proxy.UnKnowErrorException $ex) {
           out = $rh.createExceptionReply ();
           fr.umlv.corba.calculator.proxy.UnKnowErrorExceptionHelper.write (out, $ex);
         }
         break;
       }

       case 7:  // proxy/CorbaCalculator/reset
       {
         this.reset ();
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:proxy/CorbaCalculator:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public CorbaCalculator _this() 
  {
    return CorbaCalculatorHelper.narrow(
    super._this_object());
  }

  public CorbaCalculator _this(org.omg.CORBA.ORB orb) 
  {
    return CorbaCalculatorHelper.narrow(
    super._this_object(orb));
  }


} // class CorbaCalculatorPOA
