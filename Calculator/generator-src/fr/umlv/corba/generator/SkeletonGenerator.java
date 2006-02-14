/**
 * 
 */
package fr.umlv.corba.generator;

import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * @author cedric
 *
 */
public class SkeletonGenerator {

	private Method[] methods = null;
	private Class clazz = null;
	public final static String POA = "POA";
	public final static String PACKAGE_IMPL_SUFFIX = "impl";
	
	private String packageName = null;
	private PrintStream p = new PrintStream(System.out);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	private void generateProxySkeleton(String interfaceName) {
		try {
			Class.forName(interfaceName).getDeclaredMethods();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void generateAppletSkeleton(String interfaceName) {
		
	}
	
	private void analyseInterface(String interfaceName) {
		try {
			clazz = Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		methods = clazz.getDeclaredMethods();
		
	}
	
	/**
	 * Recupere le nom du package associe a l'interface fournie en parametre
	 * @param interfaceName
	 * @return
	 */
	private String getPackageName(String interfaceName) {
		int index = interfaceName.lastIndexOf(".");
		if(index == -1)
			return PACKAGE_IMPL_SUFFIX;
		return interfaceName.substring(0,index)+"."+PACKAGE_IMPL_SUFFIX;
	}

}
