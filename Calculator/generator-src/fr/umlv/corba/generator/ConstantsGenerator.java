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
public class ConstantsGenerator {
	private final static String PACKAGE_APPLET_SUFFIX = "constants";
	private final static String CLASS_SUFFIX = "Constants";
	private final static String DEFAULT_CLA = "A0";
	
	private Class clazz = null;
	private String packageName;
	
	//private String name = null;

	private GeneratorManager manager;
	
	public ConstantsGenerator(GeneratorManager manager) {
		this.manager = manager;
		clazz = manager.getClazz();
		packageName = manager.getPackageName()+PACKAGE_APPLET_SUFFIX;
	}
	
	public Map<String, Object> getValues() {
		Map <String, Object> values = new HashMap <String, Object> ();
		
		values.put("package",packageName);
		values.put("package_suffix",PACKAGE_APPLET_SUFFIX);
		values.put("className",manager.getRealName());
		values.put("class",clazz);
		values.put("CLA",DEFAULT_CLA);
		return values;
	}
	
	/**
	 * @return the name of the class denoted by this generator
	 */
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
