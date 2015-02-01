package jlime.pc.edition;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

/**
 * Created by Coolway99 on 2015-01-25
 *
 * @author Coolway99 (xxcoolwayxx@gmail.com)
 */
public class ModuleManager{
	private static HashMap<String, Method> methodList = new HashMap<>();
	private static HashMap<File, ArrayList<String>> fileList = new HashMap<>();
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Module{
		HelpLocation help() default HelpLocation.none;
		double version() default 1.0;
		double minProgVersion() default GUI.version;
		enum HelpLocation{
			internal, external, none
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ModInit{}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Parser{}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Help{}
	
	public static void init(File directory){
		for(File file : directory.listFiles()){
			add(file);
		}
	}
	public static void add(File file){
		try{
			fileList.put(file, new ArrayList<String>());
			URLClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()},
					ModuleManager.class.getClassLoader());
			Class<?> clazz = null;
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> entries = jarFile.entries();
			WatchList helpList = new WatchList();
			
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				String filetype = name.substring(name.lastIndexOf('.'));
				name = name.substring(0, name.lastIndexOf('.'));
				switch(filetype){
					case ".class":
						name = name.replaceAll("/", ".");
						if(loader == null){
							clazz = Class.forName(name);
						} else {
							clazz = Class.forName(name, true, loader);
						}
						if(clazz.getAnnotation(Module.class) != null){
							Module annotation =  clazz.getAnnotation(Module.class);
							Module.HelpLocation help = annotation.help();
							if(annotation.minProgVersion() > GUI.version){
								JOptionPane.showMessageDialog(null, "Warning, module file "+file.getName()+" is for a newer version of JLime"
										+ "\nPlease update JLime or tell the Mod Author about this problem"
										+ "\n Current Version:"+GUI.version+", Required version:"+annotation.minProgVersion(), "Module Error",
										JOptionPane.ERROR_MESSAGE);
									return;
							}
							String modName = null;
							Method modMethod = null;
							Component modHelp = null;
							for(Method method : clazz.getMethods()){
								if(method.getAnnotation(ModInit.class) != null){
									modName = (String) method.invoke(null);
								}
								if(method.getAnnotation(Parser.class) != null){
									modMethod = method;
								}
								switch(help){
									case internal:
										if(method.getAnnotation(Help.class) != null){
											modHelp = (Component) method.invoke(null);
										}
										break;
									default:
										break;
								}
								if(modName != null && modMethod != null){
									switch(help){
										case internal:
											if(modHelp == null){
												continue;
											}
											GUI.gui.help.addModTab(modHelp, modName);
											break;
										case external:
											helpList.load(modName);
											break;
										case none:
											break;
									}
									methodList.put(modName, modMethod);
									fileList.get(file).add(modName);
									break;
								}
							}
						}
						break;
					case ".txt":
						helpList.put(file);
						break;
					default:
						break;
				}
			}
			if(fileList.get(file).isEmpty()){
				fileList.remove(file);
			}
			if(!helpList.clean()){
				JOptionPane.showMessageDialog(null, "Warning, module file "+file.getName()+" had issues loading help files!"
						+ "\nPlease tell the module author about this problem", "Module Error",
						JOptionPane.ERROR_MESSAGE);
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
	public static String[] getList(){
		return methodList.keySet().toArray(new String[0]);
	}
	public static File[] getFiles(){
		return fileList.keySet().toArray(new File[0]);
	}
	public static void remove(File file){
		for(String name : fileList.get(file)){
			methodList.remove(name);
			GUI.gui.help.removeModules(name);
		}
		fileList.remove(file);
	}
}
class WatchList{
	private final ArrayList<String> watching = new ArrayList<>();
	private final HashMap<String, File> potentialHelpList = new HashMap<>();
	
	public void put(File file) throws IOException{
		String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
		if(watching.contains(name)){
			loadHelp(name, file);
			watching.remove(name);
		} else {
			potentialHelpList.put(name, file);
		}
	}
	
	public void load(String name) throws IOException{
		Boolean b = this.loadHelp(name);
		if(!b){
			watching.add(name);
		}
	}
	
	private boolean loadHelp(String s) throws IOException{
		File file = potentialHelpList.remove(s);
		if(file == null){
			return false;
		}
		return this.loadHelp(s, file);
	}
	
	private boolean loadHelp(String s, File file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String title = reader.readLine();
		ArrayList<String> in = new ArrayList<>();
		String temp = reader.readLine();
		while(temp != null){
			in.add(temp);
			temp = reader.readLine();
		}
		GUI.gui.help.addDefaultTab(s, title, in.toArray(new String[0]));
		reader.close();
		return true;
	}
	
	public boolean clean() throws IOException{
		Boolean b = watching.isEmpty();
		if(b){
			return true;
		}
		for(String s : watching){
			Boolean b2 = loadHelp(s);
			if(b2 == false){
				return false;
			}
		}
		return true;
	}
}