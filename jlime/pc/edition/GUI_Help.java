/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlime.pc.edition;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/**
 *
 * @author <s>hotrodman106</s> Coolway99
 */
public class GUI_Help extends JFrame {
	private static final long serialVersionUID = 4988853886501631977L;
	public static final String r = "\n";
	private final HashMap<String, Component> componentList = new HashMap<>();
	private final JPanel modList = new JPanel();
	private final JLabel titleLabel = new JLabel();
	
	public GUI_Help() {
		modList.setLayout(new BoxLayout(modList, BoxLayout.Y_AXIS));
		setIconImage(new ImageIcon(getClass().getResource("assets/images/help.png")).getImage());
		String[] helpCommands = {"Standard Commands:",
				"/ping",
				"/pong",
				"/clear",
				"/linebreak",
				"/echo:[String]",
				"/gettime:[date String]",
				"/random:[Integer]",
				"/loop:[Integer],([Command])",
				"/if:[Integer][<,>,=,<=,>=][Integer],\n"
				+ "([True Command]),([False Command])",
				"/for:[Integer],[Integer],[Integer],([Command])",
				"Header Commands:",
				"!import:[key1],[key2],[key3]...",
				"!foreground:[int R],[int G],[int B]",
				"!background:[int R],[int G],[int B]",
				"!resetColors"};
		String[] helpText = {"",
				"PONG!",
				"PING!",
				"Clears the screen",
				"Adds a carriage return",
				"Writes a string to the console",
				"Outputs the date/time",
				"Outputs a random number up to the value specified",
				"Loops a command a set number of times",
				"Checks if a statement is true and, if so, runs a command",
				"Loops a command for a set number of times in certain increments",
				"",
				"Imports a module",
				"Changes the console's foreground color temporarily",
				"Changes the background color temporarily",
				"Resets the colors to the user setting"};
		
		titleLabel.setBackground(new java.awt.Color(255, 255, 255));
		titleLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		titleLabel.setText("JLime Command List");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		this.setTitle("JLime Help Window");
		this.setSize(800, 600);;
		this.add(titleLabel);
		this.add(new JScrollPane(modList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		this.addModule("base", helpCommands, helpText);
	}
	
	public void addModule(String modName, String[] names, String[] values){
		JTextArea modNames = new JTextArea(), modValues = new JTextArea();
		JPanel modPanel = new JPanel();
		modNames.setEditable(false);
		modValues.setEditable(false);
		StringBuilder temp = new StringBuilder(), temp2 = new StringBuilder();
		for(int x = 0; x < names.length; x++){
			temp.append(names[x]+r);
			temp2.append(values[x]+r);
		}
		temp.append(r);
		temp2.append(r);
		modNames.setText(temp.toString());
		modValues.setText(temp2.toString());
		modPanel.add(modNames);
		modPanel.add(modValues);
		modList.add(modPanel);
		componentList.put(modName, modPanel);
		modNames.setFont(new Font("Tahoma", Font.PLAIN, 18));
		modValues.setFont(new Font("Tahoma", Font.PLAIN, 18));
	}
	
	public void removeModules(String modName){
		modList.remove(componentList.remove(modName));
	}
}
