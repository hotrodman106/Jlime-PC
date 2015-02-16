package jlime.pc.edition;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.UIManager;

@SuppressWarnings("javadoc")
public class GUI_Splash extends JWindow { 
	private static final long serialVersionUID = -1825564813327818918L;
	 URL image = getClass().getResource("assets/images/javalime.png");
	 JProgressBar progress = new JProgressBar();
	 public GUI_Splash() { 
		 try { 
			 setSize(480,530); 
			 setBackground(new Color(0, 255, 0, 0));
			 setLocationRelativeTo(null); 
			 setVisible(true);
			 Thread.sleep(2500);
			 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Preload complete");
		    GUI.main(null); 
		    dispose();
} catch(Exception exception) { 
	JOptionPane.showMessageDialog((java.awt.Component) null,"Error: "+exception.getMessage(), "Error:", JOptionPane.DEFAULT_OPTION); 
	System.exit(-1);
	} } 
	 @Override
	public void paint(Graphics g) { 
		 Image img = null;
		
		try {
			img = ImageIO.read(image);
		} catch (IOException e) {}
		 g.drawImage(img,0,0,this); 
		 
	 } 
	 public static void main(String[]args) {
	     GUI_Splash sp = new GUI_Splash(); 
		 } 
	 }

