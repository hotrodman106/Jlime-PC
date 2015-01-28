package jlime.pc.edition;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JTextArea;

public class ConsoleProxy {
	private static JTextArea console = null;
	public static void init(JTextArea console){
		ConsoleProxy.console = console;
	}
	public static void append(String s){
		console.append(s);
	}
	public static void setText(String s){
		console.setText(s);
	}
	public static String getText(){
		return console.getText();
	}
	public static void setForeground(Color color){
		console.setForeground(color);
	}
	public static void setBackground(Color color){
		console.setBackground(color);
	}
	public static void resetColors() throws IOException{
		try {
			FileManager.readsettingsFile("settings.jlimesettings");
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
