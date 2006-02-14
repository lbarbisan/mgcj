/**
 * 
 */
package fr.umlv.corba.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cedric
 *
 */
public class ProxyServerGenerator implements Generator {

	private final static String PACKAGE_APPLET_SUFFIX = "server";
	private final static String CLASS_SUFFIX = "Server";
	
	private Class clazz = null;
	private String packageName;
	
	//private String name = null;

	private GeneratorManager manager;
	
	public ProxyServerGenerator(GeneratorManager manager) {
		this.manager = manager;
		clazz = manager.getClazz();
		packageName = manager.getPackageName()+PACKAGE_APPLET_SUFFIX;
	}
	
	public Map<String, Object> getValues() {
		Map <String, Object> values = new HashMap <String, Object> ();

		values.put("package",packageName);
		values.put("packagePrefix",manager.getPackagePrefix());
		values.put("className",manager.getRealName());
		values.put("class",clazz);
		
		return values;
	}
	
	public String getName() {
		return manager.getRealName()+CLASS_SUFFIX+".java";
	}

	/**
	 * @return the packageName of the class denoted by this generator.
	 */
	public String getPackageName() {
		return packageName;
	}
}
