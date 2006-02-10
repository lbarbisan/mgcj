package fr.umlv.corba.generator;

import java.util.Map;

public interface Generator {
	public Map<String, Object> getValues(Class klass);
}
