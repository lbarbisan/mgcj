package fr.umlv.corba.generator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CorbaProxyGenerator implements Generator {
	
	private final static String IMPORTS = "imports";
	private final static String PACKAGE = "package";
	//private final static String CLASS = "class";
	
	public Map <String, Object> getValues() {
		Map <String, Object> values = new HashMap <String, Object> ();

		// Class
		//values.put(CLASS, klass);
		
		// Package
		values.put(PACKAGE, "pakageA");

		// Import
		List <String> imports = new LinkedList <String> ();
		imports.add("import1");
		imports.add("import2");
		imports.add("import3");
		imports.add("import4");
		values.put(IMPORTS, imports);
		
        return values;
	}
}
