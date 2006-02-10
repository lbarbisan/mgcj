package ${package};

<#list imports as import>
import ${import};
</#list> 

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
	
	${otherPart}
}