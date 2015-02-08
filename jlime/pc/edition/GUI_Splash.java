package jlime.pc.edition;
 
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

 public class GUI_Splash extends JWindow { 
	 URL image = getClass().getResource("assets/images/javalime.png");
	 public GUI_Splash() { 
		 try { 
			 setSize(480,530); 
			 setBackground(new Color(0, 255, 0, 0));
			 setLocationRelativeTo(null); 
			 show(); 
			 Thread.sleep(4000); 
			 dispose();
			 GUI z = new GUI();
			 z.setVisible(true);
} catch(Exception exception) { 
	JOptionPane.showMessageDialog((java.awt.Component) null,"Error"+exception.getMessage(), "Error:", JOptionPane.DEFAULT_OPTION); 
	} } 
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
