package fr.umlv.corba.generator;

import java.io.File;
import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

public class GeneratorTest_DELETE {

	public static void main(String[] args) throws IOException, TemplateException {
		/* Create and adjust the configuration */
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File("./templates"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        /* Get or create a template */
        //Template temp = cfg.getTemplate("testTemplate.ftl");

        //CorbaProxyGenerator gen = new CorbaProxyGenerator();        
        
        /* Merge data model with template */
        //Writer out = new OutputStreamWriter(new FileOutputStream(new File("./" + "JavaClassTest.java")));
        //temp.process(gen.getValues(CorbaCalculatorOperations.class), out);
        //out.flush();  
	}
}
