package jlime.pc.edition;

import java.awt.Component;
import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import jlime.pc.edition.ModuleManager.Module.HelpType;

/**
 * The class that manages the modules, the command parser consults
 * this if it cannot find the specified command
 * @author Coolway99
 */
public class ModuleManager{
	private static HashMap<String, Method> methodList = new HashMap<>();
	private static HashMap<File, ArrayList<String>> fileList = new HashMap<>();
	
	/**
	 * The annotation that must go on the classes that are a module, it tells the module manager
	 * to look deeper into them for the ModInit, Parser, and Help methods
	 * @author Coolway99
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Module{
		/**
		 * What type the internal help method is, can be of type component, html, or txt
		 * @see HelpType
		 * @return The type that the internal help method is
		 */
		HelpType help();
		/**
		 * The version of the module, has no use right now
		 * @return The module version
		 */
		double version() default 1.0;
		/**
		 * The minimum version of JLime this module is compatible with, will be mandatory in the 
		 * future, for now defaults to the current version. If the current version is lower, the 
		 * module will fail to load.
		 * @return The minimum JLime version this is compatible with
		 */
		double minProgVersion() default GUI.version;
		/**
		 * An Enum containing all the possible types Help can be
		 * @see #help()
		 * @author Coolway99
		 */
		enum HelpType{
			component, html, txt
		}
	}
	
	/**
	 * The annotation that signifies that this is the method that inits the module, and said method
	 * should return the modname (the unique name that is used to point to that module at runtime).
	 * Any module initialization should take place here, as when this method is found it is ran<br />
	 * <h1>WARNING</h1>This method is ran as soon as it is found, this means that some class files
	 * in the same jar may not have been loaded yet!<br />
	 * @author Coolway99
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ModInit{}
	
	/**
	 * The method that does the blunt work, is called with all the data required to process commands
	 * Should return -1 if it consumed the command call (as in, "yes I have the requested command
	 * and have ran it"), -2 if there is an error (you must output the error to the consoleOutput though),
	 * and -3 if "no, I didn't have anything for that, keep looking".
	 * @author Coolway99
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Parser{}
	
	/**
	 * Proper documentation is key, not having it is a pain in the butt. Therefore, help files are
	 * mandatory for all files and will be called through this method. Simply put, it will return a 
	 * component or a string depending on what you specified in the Method annotation, and will put
	 * it into the help menu for the user to read. 
	 * @see HelpType
	 * @author Coolway99
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Help{}
	
	/**
	 * Initialization, will scan the given directory and call add(File) on any module found there
	 * @param directory
	 */
	public static void init(File directory){
		for(File file : directory.listFiles()){
			add(file);
		}
	}
	
	/**
	 * Attempt to add any module files. If it finds no modules then the jar is unloaded
	 * @param file The jarfile to check for modules
	 */
	public static void add(File file){
		try{
			fileList.put(file, new ArrayList<String>());
			URLClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()},
					ModuleManager.class.getClassLoader());
			Class<?> clazz = null;
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if(!name.endsWith(".class")){
					continue;
				}
				name = name.substring(0, name.lastIndexOf('.'));
				name = name.replaceAll("/", ".");
				if(loader == null){
					clazz = Class.forName(name);
				} else {
					clazz = Class.forName(name, true, loader);
				}
				if(clazz.getAnnotation(Module.class) != null){
					Module annotation =  clazz.getAnnotation(Module.class);
					Module.HelpType help = annotation.help();
					if(annotation.minProgVersion() > GUI.version){
						JOptionPane.showMessageDialog(null, "Warning, module file "+file.getName()+" is for a newer version of JLime"
								+ "\nPlease update JLime or tell the Mod Author about this problem"
								+ "\n Current Version:"+GUI.version+", Required version:"+annotation.minProgVersion(), "Module Error",
								JOptionPane.ERROR_MESSAGE);
						loader.close();
						jarFile.close();
						return;
					}
					String modName = null;
					Method modMethod = null;
					Method modHelp = null;
					for(Method method : clazz.getMethods()){
						if(method.getAnnotation(ModInit.class) != null){
							modName = (String) method.invoke(null);
						}
						if(method.getAnnotation(Parser.class) != null){
							modMethod = method;
						}
						if(method.getAnnotation(Help.class) != null){
							modHelp = method;
						}
						if(modName != null && modMethod != null && modHelp != null){
							switch(help){
								case component:
									GUI.gui.help.addModTab((Component) modHelp.invoke(null),
											modName);
									break;
								case txt:
									GUI.gui.help.addDefaultTab(modName,
											(String) modHelp.invoke(null), "txt");
									break;
								case html:
									GUI.gui.help.addDefaultTab(modName,
											(String) modHelp.invoke(null), "html");
									break;
							}
							methodList.put(modName, modMethod);
							fileList.get(file).add(modName);
							break;
						}
					}
				}
				break;
			}
			if(fileList.get(file).isEmpty()){
				fileList.remove(file);
			}
			jarFile.close();
			loader.close();
		} catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Warning, module file "+file.getName()+" errored when loading"
					+ "\nPlease tell the module author about this problem", "Module Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Is called by CommandParser to check the specified modules to see if they contain the command
	 * @param modules The list of modules to be checked for the command
	 * @param cmd The command to be run
	 * @param args The arguments for the command
	 * @param startDepth The starting depth to pass to the CommandParser
	 * @param consoleOutput The array to-be-outputted to the console, any console output adds to this
	 * @return The code returned by the modules provided it's not -3
	 */
	public static int run(ArrayList<String> modules, String cmd, String[] args, int startDepth, ArrayList<String> consoleOutput){
		try{
			for(String key : modules){
				Method method = methodList.get(key);
				int y = (int) method.invoke(null, cmd, args, startDepth, consoleOutput);
				if(y != -3){
					return y;
				}
			}
		} catch(Exception e){
			return -2;
		}
		consoleOutput.add("OI! Command not valid!\n");
		return -2;
	}
	/**
	 * Gets the list of loaded modules
	 * @return The list of loaded modules
	 */
	public static String[] getList(){
		return methodList.keySet().toArray(new String[0]);
	}
	
	/**
	 * The list of files that are actually loaded since files can have more than one module
	 * @return The list of files
	 */
	public static File[] getFiles(){
		return fileList.keySet().toArray(new File[0]);
	}
	
	/**
	 * Remove the file and all modules loaded by it
	 * @param file The file to be removed
	 */
	public static void remove(File file){
		for(String name : fileList.get(file)){
			methodList.remove(name);
			GUI.gui.help.removeModules(name);
		}
		fileList.remove(file);
	}
}