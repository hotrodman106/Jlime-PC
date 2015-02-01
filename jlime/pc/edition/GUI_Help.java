/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlime.pc.edition;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

/**
 *
 * @author hotrodman106 
 * @author Coolway99
 */
public class GUI_Help extends JFrame {
	private static final long serialVersionUID = 4988853886501631977L;
	public static final String r = "\n";
	private final HashMap<String, Integer> componentList = new HashMap<>();
	private final JTabbedPane tabbedPane = new JTabbedPane();
	
	public GUI_Help() {
		setIconImage(new ImageIcon(getClass().getResource("assets/images/help.png")).getImage());
		
		String[] helpLines = {"Standard Commands:",
				"/ping PONG!",
				"/pong PING!",
				"/clear Clears the screen",
				"/linebreak Adds a carriage return",
				"/echo:[String] Writes a string to the console",
				"/gettime:[date String] Outputs the date/time",
				"/random:[Integer] Outputs a random number up to the value specified",
				"/loop:[Integer],([Command]) Loops a command a set number of times",
				"/if:[Integer][<,>,=,<=,>=][Integer],([True Command]),([False Command]) Checks if a statement is true and, if so, runs a command",
				"/for:[Integer],[Integer],[Integer],([Command]) Loops a command for a set number of times in certain increments",
				"Header Commands:",
				"!import:[key1],[key2],[key3]... Imports a module",
				"!foreground:[int,],[int G],[int B] Changes the console's foreground color temporarily",
				"!background:[int,],[int G],[int B] Changes the background color temporarily",
		"!resetColors resets the colors to the user setting"};
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("JLime Help Window");
		this.setSize(560, 440);
		this.add(tabbedPane);
		this.addDefaultTab("JLime", "JLime Command List", helpLines);
	}
	
	public void addModTab(Component component, String modName){
		JScrollPane scrollPane = new JScrollPane(component,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabbedPane.addTab(modName, scrollPane);
		componentList.put(modName, tabbedPane.indexOfComponent(scrollPane));
	}
	
	public void addDefaultTab(String modName, String title, String[] text){
		/*DefaultListModel<String> listModel = new DefaultListModel<>();
		JList<String> list = new JList<>();
		JLabel label = new JLabel();
		JPanel panel = new JPanel();
		label.setFont(new Font("Tahoma", Font.PLAIN , 18));
		label.setText(title);
		panel.setBackground(new Color(255, 255, 255));
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		for(int x = 0; x != text.length; x++){
			listModel.addElement(text[x]+r);
		}
		list.setModel(listModel);
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGap(195)
						.addComponent(label)
						.addGap(195))
				.addComponent(list));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(label)
				.addGap(3, 3, 3)
				.addComponent(list)
				);*/
		JEditorPane main = new JEditorPane();
                try {
        //Sets help menu to help.html :)
        main.setPage(GUI_Help.class.getResource("help.html"));
         } catch (IOException ex) {}
		this.addModTab(main, modName);
	}
	
	public void removeModules(String modName){
		tabbedPane.remove(componentList.remove(modName));
	}
}