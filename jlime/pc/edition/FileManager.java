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
 *
 * @author hotrodman106
 */
public class FileManager {

    static String readFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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

    
    public static void writeFile(String filename, String input) throws FileNotFoundException{
      PrintWriter writer = new PrintWriter(filename);
      String[] lines = input.split("\n");
      for (int x = 0; x < lines.length; x++) {
      writer.println(lines[x]);
      }
      writer.close();
    }
    
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
