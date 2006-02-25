package fr.umlv.corba.calculator.proxy.client;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import fr.umlv.corba.calculator.proxy.ArithmeticException;
import fr.umlv.corba.calculator.proxy.CorbaCalculator;
import fr.umlv.corba.calculator.proxy.CorbaCalculatorHelper;
import fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators;
import fr.umlv.corba.calculator.proxy.UnKnowErrorException;
import fr.umlv.corba.calculator.proxy.ui.GraphicCalculator;

public class Client {

	/**
	 * @param args
	 * @throws InvalidName 
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		   
        Properties props = new Properties();
        props.put("org.omg.CORBA.ORBInitialHost","localhost");
        props.put("org.omg.CORBA.ORBInitialPort","1234");
       
        ORB orb = ORB.init(args,props);
        NamingContextExt context = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
       
        CorbaCalculator calculator = CorbaCalculatorHelper.narrow(context.resolve_str("Calculator"));
        
        calculator.reset();
       
        // on demarre le mode graphique
        new GraphicCalculator(calculator).getFrame().setVisible(true);
	}

}
