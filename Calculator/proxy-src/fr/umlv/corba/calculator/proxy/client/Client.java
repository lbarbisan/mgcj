package fr.umlv.corba.calculator.proxy.client;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import fr.umlv.corba.calculator.proxy.CorbaCalculator;
import fr.umlv.corba.calculator.proxy.CorbaCalculatorHelper;
import fr.umlv.corba.calculator.proxy.ui.GraphicCalculator;

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
        
        calculator.reset();
        calculator.push((short) 12);
        calculator.push((short) 12);
        calculator.add();
        System.out.println("La valeur " + calculator.pop());
        calculator.reset();
        //on demarre le mode graphique
        new GraphicCalculator(calculator).getFrame().setVisible(true);
	}

}
