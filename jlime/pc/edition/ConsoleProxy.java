package jlime.pc.edition;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JTextArea;

/**
 * A proxy for the console, meant to be used for cross-compatibility reasons
 *
 * @author Coolway99
 */
public class ConsoleProxy{
	private static JTextArea console = null;
	/**
	 * Variable to tell if the console is main or debug
	 *
	 * @author hotrodman106
	 */
	public static JTextArea debugConsole = null;
	
	/**
	 * Initialization, since all it does it make the console variable the
	 * console that is passed, can be used for changing where the output goes<br />
	 * <b>Coolway99 glares at hotrodman106</b>
	 *
	 * @param console The console for everything to output too
	 */
	public static void init(JTextArea console){
		ConsoleProxy.console = console;
	}
	
	/**
	 * Appends a value to either the main console or the debug console.
	 *
	 * @param s String to be appended.
	 */
	public static void append(String s){
		if(GUI_Console.isVisible == false){
			console.append(s);
		} else {
			debugConsole.append(s);
		}
	}
	
	/**
	 * Sets the text of either the main console or the debug console.
	 *
	 * @param s String to be set.
	 */
	public static void setText(String s){
		if(GUI_Console.isVisible == false){
			console.setText(s);
		} else {
			debugConsole.setText(s);
		}
	}
	
	/**
	 * Gets the text of either the main console or the debug console.
	 *
	 * @return Returns either the text of the debug console if it's open or the
	 * main console.
	 */
	public static String getText(){
		if(GUI_Console.isVisible == false){ return console.getText(); }
		return debugConsole.getText();
	}
	
	/**
	 * Sets the foreground color of either the main console or the debug
	 * console.
	 *
	 * @param color Color to be set.
	 */
	public static void setForeground(Color color){
		if(GUI_Console.isVisible == false){
			console.setForeground(color);
		} else {
			debugConsole.setForeground(color);
		}
	}
	
	/**
	 * Sets the background color of either the main console or the debug
	 * console.
	 *
	 * @param color Color to be set.
	 */
	public static void setBackground(Color color){
		if(GUI_Console.isVisible == false){
			console.setBackground(color);
		} else {
			debugConsole.setBackground(color);
		}
	}
	
	/**
	 * Sets all colors to values from settings file.
	 *
	 * @throws IOException If file is not found.
	 */
	public static void resetColors() throws IOException{
		try{
			FileManager.readsettingsFile("settings.jlimesettings");
		} catch(IOException e){
			e.printStackTrace();
			throw e;
		}
	}
}
