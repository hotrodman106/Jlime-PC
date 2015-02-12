/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlime.pc.edition;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hotrodman106 (Coding and Java Docs)
 */
public class FileManager {
	/**
	 * Gets the contents of a saved jlime file.
	 * @param file Directory of file to be opened.
	 * @return Returns a string containing every line in chosen file.
	 * @throws IOException Throws IOException if file does not exist.
	 */
    public static  String readFile( String file ) throws IOException {
    BufferedReader reader = new BufferedReader( new FileReader (file));
    String         line = null;
    StringBuilder  stringBuilder = new StringBuilder();
    String         ls = "\n";

    while( ( line = reader.readLine() ) != null ) {
        stringBuilder.append( line );
        stringBuilder.append( ls );
    }
    reader.close();
    return stringBuilder.toString();
}

/**
 * Either overwrites or creates a new .jlime file containing all text typed by user.    
 * @param filename Name of file to write to.
 * @param input A String containing all code written.
 * @throws FileNotFoundException Throws FileNotFoundException if the file does not exist.
 */
    public static void writeFile(String filename, String input) throws FileNotFoundException{
      PrintWriter writer = new PrintWriter(filename);
      String[] lines = input.split("\n");
      for (int x = 0; x < lines.length; x++) {
      writer.println(lines[x]);
      }
      writer.close();
    }
    
    /**
     * Either overwrites or creates a new .jlimesettings file containing all of the user's settings.  
     * @param debug Should /debug commands be allowed while coding?
     * @param beta Should beta commands be allowed while coding? (Unused)
     * @param consoleB String containing the RGB value for the console's background color.
     * @param consoleF String containing the RGB value for the console's foreground color.
     * @param sWindowB String containing the RGB value for the scripting window's foreground color.
     * @param sWindowF String containing the RGB value for the scripting window's background color.
     * @param sWindow String containing the font and the font size for the scripting window. ( EX: Monospaced,17 )
     * @param console String containing the font and the font size for the console. ( EX: Monospaced,17 )
     * @throws FileNotFoundException Throws FileNotFoundException if settings file does not exist.
     */
    public static void writeSettings(Boolean debug, Boolean beta, String consoleB, String consoleF, String sWindowB, String sWindowF, String sWindow, String console) throws FileNotFoundException{
      PrintWriter writer = new PrintWriter("settings.jlimesettings");
      System.out.println("settings.jlimesettings");
      writer.println(debug);
      writer.println(beta);
      writer.println(consoleB);
      writer.println(consoleF);
      writer.println(sWindowB);
      writer.println(sWindowF);
      writer.println(sWindow);
      writer.println(console);
      writer.close();
    }
    
    /**
     * Reads the .jlimesettings file and sets the settings appropriately.
     * @param file File to be read from.
     * @throws IOException Throws IOException if file does not exist.
     */
    
    public static void readsettingsFile(String file) throws IOException {
    BufferedReader reader = new BufferedReader( new FileReader (file));
    String         line = null;
    String[] lines = new String[9];
    int x = 0;
    
    while( ( line = reader.readLine() ) != null ) {
    lines[x] = line;
    x++;
    System.out.println(line);
    }
    
    Boolean useDebug = Boolean.parseBoolean(lines[0]);
    Boolean useBeta = Boolean.parseBoolean(lines[1]);
    
    //Gets RGB values from file and creates a new color
    String[] consoleBRGB = lines[2].split(",");
    Color getconsoleB = new Color(Integer.parseInt(consoleBRGB[0]),Integer.parseInt(consoleBRGB[1]),Integer.parseInt(consoleBRGB[2]));
    
    String[] consoleFRGB = lines[3].split(",");
    Color getconsoleF = new Color(Integer.parseInt(consoleFRGB[0]),Integer.parseInt(consoleFRGB[1]),Integer.parseInt(consoleFRGB[2]));
    
    String[] sWindowBRGB = lines[4].split(",");
    Color getsWindowB = new Color(Integer.parseInt(sWindowBRGB[0]),Integer.parseInt(sWindowBRGB[1]),Integer.parseInt(sWindowBRGB[2]));
    
    String[] sWindowFRGB = lines[5].split(",");
    Color getsWindowF = new Color(Integer.parseInt(sWindowFRGB[0]),Integer.parseInt(sWindowFRGB[1]),Integer.parseInt(sWindowFRGB[2]));
    
    //Gets font and size and creates a new font
    String[] sWindowFont = lines[6].split(",");
    Font getsWindow = new Font(sWindowFont[0], Font.PLAIN, Integer.parseInt(sWindowFont[1]));

    String[] consoleFont = lines[7].split(",");
    Font getConsole = new Font(consoleFont[0], Font.PLAIN, Integer.parseInt(consoleFont[1]));
    
    //Use values
       GUI.useDebug = useDebug;
       GUI.jTextArea1.setFont(getConsole);
       GUI.jTextArea2.setFont(getsWindow);
       
       try{
       GUI.jTextArea1.setBackground(getconsoleB);
       }catch (NullPointerException e){
       GUI.jTextArea1.setBackground(Color.BLACK);
       }
       
       try{
       GUI.jTextArea1.setForeground(getconsoleF);
       }catch (NullPointerException e){
       GUI.jTextArea1.setForeground(Color.GREEN);
       }
       
       try{
       GUI.jTextArea2.setBackground(getsWindowB);
       }catch (NullPointerException e){
       GUI.jTextArea2.setBackground(Color.WHITE);
       }
       
       try{
       GUI.jTextArea2.setForeground(getsWindowF);
       }catch (NullPointerException e){
       GUI.jTextArea2.setForeground(Color.BLACK);
       }
    reader.close();

}
    
}
