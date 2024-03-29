package fr.umlv.corba.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratorManager {

	public static final String PACKAGE_SEPARATOR = ".";
	public final static String OPERATIONS_STRING = "Operations";
	public final static String DEFAULT_DESTINATION = "generated";
	
	private Class clazz;
	private String name;
	private String realName;
	private String packageName;
	private String packagePrefix;
	
	private String destination;
	private File dest;
	private Configuration cfg;
	
	public GeneratorManager(Class clazz,String destination,String packagePrefix) {
		this.clazz = clazz;
		this.name = clazz.getSimpleName();
		this.destination = destination;
		initGenerator(packagePrefix);
	}
	
	private void initGenerator(String packagePrefix) {
		setRealName();
		setPackageName(packagePrefix);
		
		cfg = new Configuration();
		//TODO mettre le nom du repertoire des templates dans un fichie properties
		URL url = GeneratorManager.class.getResource("/templates"); 
		try {
			cfg.setDirectoryForTemplateLoading(new File(url.getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		//Creation du repertoire
		dest = new File(destination);
		if(!dest.exists()) {
			dest.mkdirs();
		}
	}
	
	
	private void setPackageName(String packagePrefix) {
		
		String prefix = "";
		String localName = clazz.getName();
		int lastIndex = localName.lastIndexOf(PACKAGE_SEPARATOR); //on verifie que la classe a un prefixe package
		if(lastIndex != -1) {
			prefix = localName.substring(0,lastIndex+1);
		} 
		
		this.packageName = prefix;
		this.packagePrefix = prefix;
	}

	public String getPackageName() {
		return packageName;
	}
	
	public String getPackagePrefix() {
		return packagePrefix;
	}
	
	private void setRealName() {
		StringBuilder buffer = new StringBuilder(name);
		realName = buffer.substring(0,buffer.length()-OPERATIONS_STRING.length());
	}
	
	public String getRealName() {
		return realName;
	}
	
	/**
	 * @return Returns the clazz.
	 */
	public Class getClazz() {
		return clazz;
	}

	public void runGenerator() {
		try {
			runConstantsGenerator();
			runAppletGenerator();
			runServerGenerator();
			runServantGenerator();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws IOException 
	 * @throws TemplateException 
	 * 
	 */
	private void runConstantsGenerator() throws IOException, TemplateException {		
		/* Get or create a template */
		Template template = cfg.getTemplate("ConstantsTemplate.ftl");
		
		ConstantsGenerator constantsGenerator = new ConstantsGenerator(this);
		
		File dir = createDestination(constantsGenerator.getPackageName());
		
		/* Merge data model with template */
		Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir,constantsGenerator.getName())));
		template.process(constantsGenerator.getValues(), out);
		out.flush();
		out.close();
	}
	
	/**
	 * @throws IOException 
	 * @throws TemplateException 
	 * 
	 */
	private void runAppletGenerator() throws IOException, TemplateException {		
		/* Get or create a template */
		Template template = cfg.getTemplate("AppletTemplate.ftl");
		
		AppletGenerator appletGenerator = new AppletGenerator(this);
		
		File dir = createDestination(appletGenerator.getPackageName());
		
		/* Merge data model with template */
		Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir,appletGenerator.getName())));
		template.process(appletGenerator.getValues(), out);
		out.flush();
		out.close();
	}
	
	/**
	 * @throws IOException 
	 * @throws TemplateException 
	 * 
	 */
	private void runServantGenerator() throws IOException, TemplateException {	
		/* Get or create a template */
		Template template = cfg.getTemplate("ProxyServantTemplate.ftl");
		
		ProxyServantGenerator servantGenerator = new ProxyServantGenerator(this);
		
		File dir = createDestination(servantGenerator.getPackageName());
		
		/* Merge data model with template */
		Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir,servantGenerator.getName())));
		template.process(servantGenerator.getValues(), out);
		out.flush();
		out.close();
	}
	
	/**
	 * @throws IOException 
	 * @throws TemplateException 
	 * 
	 */
	private void runServerGenerator() throws IOException, TemplateException {
		/* Get or create a template */
		Template template = cfg.getTemplate("ProxyServerTemplate.ftl");
		
		ProxyServerGenerator serverGenerator = new ProxyServerGenerator(this);
		
		File dir = createDestination(serverGenerator.getPackageName());
		
		/* Merge data model with template */
		Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir,serverGenerator.getName())));
		template.process(serverGenerator.getValues(), out);
		out.flush();
		out.close();
	}
	
	/**
	 * @param packageName
	 * @return a File object
	 */
	private File createDestination(String packageName) {
		String path = packageName.replace(PACKAGE_SEPARATOR,File.separator);
		File dir = new File(dest,path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String destination = DEFAULT_DESTINATION;
		String klassName = null;
		
		if(args.length == 0) {
			System.out.println("Specifiez le nom de la classe.");
			return ;
		} else if (args.length > 1)
			try {
			for (int i = 0; i < args.length; i++) {
				if(args[i].equals("-d")) {
					destination = args[i+1];
					i++;
				} else {
					klassName = args[i];
				}
			}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("il manque une option.");
			}

		try {
			Class<?> klass = Class.forName(klassName);
			GeneratorManager manager = new GeneratorManager(klass,destination,null);
			manager.runGenerator();
		} catch (ClassNotFoundException e) {
			System.out.println("La classe n'a pas ete trouv�e dans le classpath.");
		}
		
	}

}
