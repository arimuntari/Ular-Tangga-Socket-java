/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.swing.JTextPane;

/**
 *
 * @author Muntari
 */
public class Console {
    JTextPane textPanel;
    public Console(JTextPane panel){
        this.textPanel = panel;
    }
    public void setConsole( String message){
        String text = textPanel.getText();
        textPanel.setText(message+"...\n"+text);
    }
    
}
