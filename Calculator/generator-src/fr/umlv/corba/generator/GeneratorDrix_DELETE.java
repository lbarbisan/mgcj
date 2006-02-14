package fr.umlv.corba.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class GeneratorDrix_DELETE {
	
	public final static String POA = "POA";
	public final static String PACKAGE_IMPL_SUFFIX = "Impl";
	public final static String OPERATIONS_STRING = "Operations";
	
	private String name;
	private String realName;
	private String packageName;
	private Method[] methods;
	
	private PrintStream printer = new PrintStream(System.out);
	
	public GeneratorDrix_DELETE(Class classe) {
		this.name = classe.getSimpleName();
		this.realName = name.substring(0,name.length()-OPERATIONS_STRING.length());
		this.packageName = classe.getName().substring(0,classe.getName().lastIndexOf('.'));
		this.methods = classe.getDeclaredMethods();
	}
	
	private void generateSkeletons() throws Exception {
		generateProxyServerSkeleton();
		generateAppletSkeleton();
		generateProxyServantSkeleton();
	}
	
	private void generateAppletSkeleton() {
		ArrayList<String> methodsList = getAppletMethods();
	}
	
	private void generateProxyServantSkeleton() {
		ArrayList<String> methodsList = getImplMethods();	
	}
	
	public void generateProxyServerSkeleton() throws Exception {
		URL url = this.getClass().getResource("/ressources/ServerImportHeader.properties");
		File file = new File(url.getFile());
		Properties prop = new Properties();
		prop.load(new FileInputStream(file));
		
		printer.println("package " + packageName + ";\n");
		
		String importPackages = prop.getProperty("import");
		printer.println(importPackages);
		
		printer.println("\nimport " + packageName + "." +realName + PACKAGE_IMPL_SUFFIX + ";\n");
		
		printer.println("public class " + realName + "Server {\n");
		
		printer.println("private static final String DEFAULT_APPLET_ID = \"000102030405\";");
		printer.println("private static CFlex32CardService javacard = null;\n");
		
		String server_first_part = prop.getProperty("server_first_part");
		printer.println(server_first_part);
		
		printer.println("Servant servant = new " + realName+ "Impl(javacard);");
		printer.println("byte[] servantID = rootPOA.activate_object(servant);\n");

		printer.println("NameComponent[] name = context.to_name(\""+realName+"\");\n");
		
		String server_second_part = prop.getProperty("server_second_part");
		printer.println(server_second_part);
		String otherMethods = prop.getProperty("otherMethods");
		printer.println(otherMethods);
		
		printer.println("\n}//fin du fichier");
	}
	
	private ArrayList<String> getImplMethods(){
		ArrayList<String> list = new ArrayList<String>();
		Method methode;
		String prototype;
		ArrayList<String> params;
		ArrayList<String> except;
		
		for(int i = 0; i < methods.length; i++){
			methode = methods[i];
			prototype = "";
			except = getExceptionType(methode);
			params = getMethodParameter(methode);
			prototype += methode.getReturnType().toString()+ " " + methode.getName() + "(";
			// On ajoute les paramètres
			if(params.size()>0){
				for(int k = 0 ; k< params.size() ;k++){
					prototype += params.get(k) + " arg" + Integer.valueOf(k) + ", ";
				}
				prototype = prototype.substring(0,prototype.lastIndexOf(","));
			}
			prototype += ")";
			// On ajoute les exceptions
			if(except.size()>0){
				prototype += "throws ";
				for (Iterator iter = except.iterator(); iter.hasNext();) {
					prototype += iter.next() +",";
				}
				prototype = prototype.substring(0,prototype.lastIndexOf(","));
			}
			//prototype+=" {\n}";
			list.add(prototype);
		}
		return list;
	}
	
	private ArrayList<String> getAppletMethods(){
		ArrayList<String> list = new ArrayList<String>();
		Method methode;
		String prototype;
		
		for(int i = 0; i < methods.length; i++){
			methode = methods[i];
			prototype = "";
			prototype += "void " + methode.getName() + "(APDU apdu){\n}";
			if(!list.contains(prototype))
				list.add(prototype);
		}
		return list;
	}
	
	private ArrayList<String> getMethodParameter(Method method){
		ArrayList<String> list = new ArrayList<String>();
		  Class[] tabTypes = method.getParameterTypes();
		  for(int i = 0; i < tabTypes.length; i++){
			  list.add(tabTypes[i].getName());
		  }
		return list;
	}
	
	private ArrayList<String> getExceptionType(Method method){
		ArrayList<String> list = new ArrayList<String>();
		  Class[] tabTypes = method.getExceptionTypes();
		  for(int i = 0; i < tabTypes.length; i++){
			  list.add(tabTypes[i].getName());
		  }
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GeneratorDrix_DELETE generator = new GeneratorDrix_DELETE(Class.forName("fr.umlv.corba.calculator.proxy.CorbaCalculatorOperations"));
			//System.out.println("Classe: " + generator.name);
			//System.out.println("Package: " + generator.packageName);
			//generator.generateProxyServerSkeleton();
			for (Iterator iter = generator.getImplMethods().iterator(); iter.hasNext();) {
				System.out.println(iter.next());
			}
			
			for (Iterator iter = generator.getAppletMethods().iterator(); iter.hasNext();) {
				System.out.println(iter.next());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
