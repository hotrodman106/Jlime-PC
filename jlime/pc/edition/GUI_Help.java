/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlime.pc.edition;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(GUI_Help.class.getResource("help.html").openStream()));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		StringBuilder help = new StringBuilder();
		String temp = null;
		try {
			temp = reader.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(temp != null){
			help.append(temp + r);
			try {
				temp = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("JLime Help Window");
		this.setSize(560, 440);
		this.add(tabbedPane);
		this.addDefaultTab("JLime", help.toString(), "html");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public void addModTab(Component component, String modName){
		JScrollPane scrollPane = new JScrollPane(component,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabbedPane.addTab(modName, scrollPane);
		componentList.put(modName, tabbedPane.indexOfComponent(scrollPane));
	}
	
	public void addDefaultTab(String modName, String text, String fileType){
		switch(fileType){
			case "txt":
				String[] in = text.split("\n");
				String title = in[0];
				DefaultListModel<String> listModel = new DefaultListModel<>();
				JList<String> list = new JList<>();
				JLabel label = new JLabel();
				JPanel panel = new JPanel();
				label.setFont(new Font("Tahoma", Font.PLAIN , 18));
				label.setText(title);
				panel.setBackground(new Color(255, 255, 255));
				panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				
				for(int x = 1; x < in.length; x++){
					listModel.addElement(in[x]+r);
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
						);
				this.addModTab(panel, modName);
				break;
			case "html":
				JEditorPane main = new JEditorPane();
				main.setContentType("text/html");
				main.setText(text);
				main.setEditable(false);
				main.setHighlighter(null);
				this.addModTab(main, modName);
				break;
			default:
				break;
		}
	}
	
	public void removeModules(String modName){
		tabbedPane.remove(componentList.remove(modName));
	}
}