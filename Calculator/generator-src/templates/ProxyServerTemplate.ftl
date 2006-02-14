package ${package};

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;

import opencard.cflex.service.CFlex32CardService;
import opencard.core.service.CardRequest;
import opencard.core.service.CardServiceException;
import opencard.core.service.SmartCard;
import opencard.core.terminal.CardTerminal;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.CardTerminalRegistry;
import opencard.core.util.HexString;
import opencard.core.util.OpenCardPropertyLoadingException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;

import ${packagePrefix}impl.${className}Impl;

public class ${className}Server {

	private static final String DEFAULT_APPLET_ID = "000102030405";
	private static CFlex32CardService javacard = null;
		
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		props.put("org.omg.CORBA.ORBInitialPort", "1234");
		
		ORB orb = ORB.init(args, props);
		
		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		NamingContextExt context = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
		
		// on demarre l'applet
		startApplet(DEFAULT_APPLET_ID);
		Servant servant = new ${className}Impl(javacard);
		byte[] servantID = rootPOA.activate_object(servant);
		NameComponent[] name = context.to_name("${className}");
		
		context.rebind(name, rootPOA.id_to_reference(servantID));
		
		rootPOA.the_POAManager().activate();
		System.out.println("server is running");
		orb.run();
	}
	
	private static boolean startApplet(String applet_id) throws CardTerminalException,
	OpenCardPropertyLoadingException, CardServiceException,
	ClassNotFoundException, UnsupportedEncodingException{
		if (SmartCard.isStarted() == false) {
			SmartCard.start();
		}

		CardTerminalRegistry ctr = CardTerminalRegistry.getRegistry();
		for (Enumeration terminals = ctr.getCardTerminals(); terminals
				.hasMoreElements();) {
			CardTerminal terminal = (CardTerminal) terminals.nextElement();
			int slots = ${className}Server.printTerminalInfos(terminal);
			for (int j = 0; j < slots; j++) {
				${className}Server.printSlotInfos(terminal, j);
			}
		}
		System.out.println("Insert your card ...");
		CardRequest cr = new CardRequest(CardRequest.ANYCARD, null, null);
		SmartCard sm = SmartCard.waitForCard(cr);
		javacard = (CFlex32CardService) sm.getCardService(
				CFlex32CardService.class, true);
		javacard.selectApplication(HexString.parseHexString(applet_id));
		javacard.allocateChannel();
		return true;
	}

	private static int printTerminalInfos(CardTerminal terminal) {
		// First of all print all information stored about this reader
		System.err.println("Address: " + terminal.getAddress() + "\n"
				+ "Name:    " + terminal.getName() + "\n" + "Type:    "
				+ terminal.getType() + "\n" + "Slots:   " + terminal.getSlots()
				+ "\n");
		return terminal.getSlots();
	}

	private static void printSlotInfos(CardTerminal terminal, int aSlotID) {
		try {
			// First print the ID of the slot
			System.err.println("Info for slot ID: " + aSlotID);
			if (terminal.isCardPresent(aSlotID)) {
				System.err.println("card present: yes");
				// If there is a card in the slot print the ATR the OCF got form this
				// card
				System.err.println("ATR: "
						+ HexString
								.hexify(terminal.getCardID(aSlotID).getATR()));
				// As we do not have a driver for this card we cannot interpret this ATR
			} else
				System.err.println("card present: no");
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
	}
}