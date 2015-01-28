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
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author hotrodman106
 */
public class GUI_Settings extends javax.swing.JFrame {
private Color consoleBackground = Color.BLACK;
private Color consoleFontColor = Color.GREEN;
private Color scriptingWindowBackground = Color.WHITE;
private Color scriptingWindowFontColor = Color.BLACK;

    /**
     * Creates new form GUI_Settings
     */
    public GUI_Settings() {
        initComponents();
        
        setIconImage(new ImageIcon(getClass().getResource("settings.png")).getImage());
        BufferedReader reader = null;
    try {
        reader = new BufferedReader( new FileReader ("settings.jlimesettings"));
      
    String line = null;
    String[] lines = new String[9];
    int x = 0;
    
        while( ( line = reader.readLine() ) != null ) {
            lines[x] = line;
            x++;
            System.out.println(line);
        }
    
    
    jCheckBox1.setSelected(Boolean.parseBoolean(lines[0]));
    jCheckBox2.setSelected(Boolean.parseBoolean(lines[1]));
    
    String[] sWindowFont = lines[6].split(",");
    jSlider2.setValue(Integer.parseInt(sWindowFont[1]));
    switch(sWindowFont[0]){
        case "Tahoma":
            jComboBox2.setSelectedIndex(1);
        break;
            
        case "Arial":
            jComboBox2.setSelectedIndex(2);
        break;
            
        case "Monospaced":
            jComboBox2.setSelectedIndex(0);
        break;
    }

    String[] consoleFont = lines[7].split(",");
    jSlider1.setValue(Integer.parseInt(consoleFont[1]));
    switch(consoleFont[0]){
        case "Tahoma":
            jComboBox1.setSelectedIndex(1);
        break;
            
        case "Arial":
            jComboBox1.setSelectedIndex(2);
        break;
            
        case "Monospaced":
            jComboBox1.setSelectedIndex(0);
        break;
    }
    
    String[] consoleBRGB = lines[2].split(",");
    consoleBackground = new Color(Integer.parseInt(consoleBRGB[0]),Integer.parseInt(consoleBRGB[1]),Integer.parseInt(consoleBRGB[2]));
    
    String[] consoleFRGB = lines[3].split(",");
    consoleFontColor = new Color(Integer.parseInt(consoleFRGB[0]),Integer.parseInt(consoleFRGB[1]),Integer.parseInt(consoleFRGB[2]));
    
    String[] sWindowBRGB = lines[4].split(",");
    scriptingWindowBackground = new Color(Integer.parseInt(sWindowBRGB[0]),Integer.parseInt(sWindowBRGB[1]),Integer.parseInt(sWindowBRGB[2]));
    
    String[] sWindowFRGB = lines[5].split(",");
    scriptingWindowFontColor = new Color(Integer.parseInt(sWindowFRGB[0]),Integer.parseInt(sWindowFRGB[1]),Integer.parseInt(sWindowFRGB[2]));
      }catch (IOException ex) {
            try {
                FileManager.writeSettings(false, false, "0,0,0", "51,255,51", "255,255,255", "0,0,0", "Tahoma,14", "Monospaced,13");
            } catch (FileNotFoundException ex1) {}
    }
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings Window");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jlime/pc/edition/settings.png"))); // NOI18N
        jLabel1.setText(" Settings Window:");

        jCheckBox1.setText("Allow debug commands while scripting");
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        jCheckBox2.setText("Allow beta commands");
        jCheckBox2.setToolTipText("Currently No Beta Commands!");
        jCheckBox2.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Command Settings:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Appearance Settings:");

        jButton1.setText("Change Console Background Color");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Change Console Font Color");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setMaximumRowCount(3);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Monospaced", "Tahoma", "Arial" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Console Font:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Scripting Window Font:");

        jComboBox2.setMaximumRowCount(3);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Monospaced", "Tahoma", "Arial" }));

        jButton3.setText("Change Scripting Window Background Color");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Change Scripting Window Font Color");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Save Settings");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Console Font Size:");

        jSlider1.setMaximum(48);
        jSlider1.setMinimum(9);
        jSlider1.setPaintLabels(true);
        jSlider1.setValue(13);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Scripting Window Font Size:");

        jSlider2.setMaximum(48);
        jSlider2.setMinimum(9);
        jSlider2.setPaintLabels(true);
        jSlider2.setValue(14);

        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
            .addComponent(jSeparator3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jCheckBox1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jCheckBox2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSlider1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSlider2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        consoleBackground = JColorChooser.showDialog(this, "Choose Console Background Color", Color.black);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       consoleFontColor = JColorChooser.showDialog(this, "Choose Console Font Color", Color.green);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        scriptingWindowBackground = JColorChooser.showDialog(this, "Choose Scripting Window Background Color", Color.white);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        scriptingWindowFontColor = JColorChooser.showDialog(this, "Choose Scripting Window Font Color", Color.black);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       if(jCheckBox1.isSelected() == true){
           int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to enable debug commands? (This is not recommended!)", "Are you sure?", JOptionPane.YES_NO_OPTION);
           if(n == JOptionPane.NO_OPTION){
               jCheckBox1.setSelected(false);
           }
       }
       Font console = new Font(jComboBox1.getSelectedItem().toString(), Font.PLAIN, jSlider1.getValue());
       Font scriptingWindow = new Font(jComboBox2.getSelectedItem().toString(), Font.PLAIN, jSlider2.getValue());

       GUI.useDebug = jCheckBox1.isSelected();
       GUI.jTextArea1.setFont(console);
       GUI.jTextArea2.setFont(scriptingWindow);
       
       try{
       GUI.jTextArea1.setBackground(consoleBackground);
       }catch (NullPointerException e){
       GUI.jTextArea1.setBackground(Color.BLACK);
       }
       
       try{
       GUI.jTextArea1.setForeground(consoleFontColor);
       }catch (NullPointerException e){
       GUI.jTextArea1.setForeground(Color.GREEN);
       }
       
       try{
       GUI.jTextArea2.setBackground(scriptingWindowBackground);
       }catch (NullPointerException e){
       GUI.jTextArea2.setBackground(Color.WHITE);
       }
       
       try{
       GUI.jTextArea2.setForeground(scriptingWindowFontColor);
       }catch (NullPointerException e){
       GUI.jTextArea2.setForeground(Color.BLACK);
       }
       
    try {
        
        System.out.println(String.valueOf(consoleFontColor.getRed()) + "," + String.valueOf(consoleFontColor.getGreen()) + "," + String.valueOf(consoleFontColor.getBlue()));
        FileManager.writeSettings(jCheckBox1.isSelected(), jCheckBox2.isSelected(), 
                String.valueOf(consoleBackground.getRed()) + "," + String.valueOf(consoleBackground.getGreen()) + "," + String.valueOf(consoleBackground.getBlue()), 
                String.valueOf(consoleFontColor.getRed()) + "," + String.valueOf(consoleFontColor.getGreen()) + "," + String.valueOf(consoleFontColor.getBlue()),
                String.valueOf(scriptingWindowBackground.getRed()) + "," + String.valueOf(scriptingWindowBackground.getGreen()) + "," + String.valueOf(scriptingWindowBackground.getBlue()),
                String.valueOf(scriptingWindowFontColor.getRed()) + "," + String.valueOf(scriptingWindowFontColor.getGreen()) + "," + String.valueOf(scriptingWindowFontColor.getBlue()),
                jComboBox2.getSelectedItem().toString() + "," + String.valueOf(jSlider2.getValue()), 
                jComboBox1.getSelectedItem().toString() + "," + String.valueOf(jSlider1.getValue()));
    } catch (FileNotFoundException ex) {
        System.out.println("Settings file not found, using defaults!");
    }
       dispose();
       
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged

    }//GEN-LAST:event_jCheckBox1StateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Settings().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    // End of variables declaration//GEN-END:variables
}
