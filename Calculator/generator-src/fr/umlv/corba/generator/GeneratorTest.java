package fr.umlv.corba.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratorTest {

	public static void main(String[] args) throws IOException, TemplateException {
		/* Create and adjust the configuration */
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File("./templates"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        /* Get or create a template */
        Template temp = cfg.getTemplate("testTemplate.ftl");

        CorbaProxyGenerator gen = new CorbaProxyGenerator();        
        
        /* Merge data model with template */
        Writer out = new OutputStreamWriter(new FileOutputStream(new File("./" + "JavaClassTest.java")));
        temp.process(gen.getValues(fr.umlv.corba.calculator.proxy.CorbaCalculatorOperations.class), out);
        out.flush();  
	}
}
