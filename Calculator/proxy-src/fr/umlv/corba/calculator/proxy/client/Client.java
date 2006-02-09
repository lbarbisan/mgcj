package fr.umlv.corba.calculator.proxy.client;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import fr.umlv.corba.calculator.proxy.CorbaCalculator;
import fr.umlv.corba.calculator.proxy.CorbaCalculatorHelper;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		   
        Properties props = new Properties();
        props.put("org.omg.CORBA.ORBInitialHost","localhost");
        props.put("org.omg.CORBA.ORBInitialPort","1234");
       
        ORB orb = ORB.init(args,props);
        NamingContextExt context = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
       
        CorbaCalculator calculator = CorbaCalculatorHelper.narrow(context.resolve_str("Calculator"));
       
        calculator.push((short) 15);
        //calculator.push((short) 10);
        //calculator.mult();
        
        System.out.println("le resultat est : " + calculator.pop());
	}

}
