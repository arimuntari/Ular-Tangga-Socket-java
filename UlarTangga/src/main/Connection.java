/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import main.frame.MainGame;

/**
 *
 * @author Muntari
 */
public class Connection implements Runnable{
    Socket socket;
    PrintWriter writer; 
    ComboItem[] comboBox = new ComboItem[100];
    Thread t ;
    String user ;
    MainGame mainGame;
    GameAction gameAction;
    JComboBox combo;
    JRootPane pane;
    BufferedReader in;
    int value;
    public Connection(){
         t  = new Thread(this);
    }
    public boolean starts(JRootPane frame, String user){
        try {
            this.user = user;
            InetAddress host = InetAddress.getLocalHost();
            System.out.println("host: " + host);
            
            socket = new Socket(host.getHostName(), 80);
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            writer.println(user);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            t.start();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e);
        }
        return false;
    }
    public void send(String text){
       writer.println(text);
    }

    @Override
    public void run() {
        System.out.print("tes");
        String received;
        while(true){
            try {
                received = in.readLine();
                System.out.println(received);
                if(received.contains("#player") && received.contains("-")){
                    received = received.replace("#player", "");
                    String[] splitData = received.split(";");
                    for (int i = 0; i < splitData.length; i++) {
                        String[] text = splitData[i].split("-");
                        String id = text[0];
                        String value = text[1];
                        combo.addItem(new ComboItem(id, value));
                        //System.out.print(id+" | "+value);
                    }
                }else if(received.contains("#accept")){
                    int i  = Integer.valueOf(received.replace("#accept", ""));
                    int jawab = JOptionPane.showOptionDialog(pane, 
                    "Player mengajak bermain!?", 
                    "Ajakan Bermain", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null, null, null);

                    if(jawab == JOptionPane.YES_OPTION){
                        this.send("#yesBattle"+i);
                    }
                }else if(received.contains("#confirmed")){
                    int i  = Integer.valueOf(received.replace("#confirmed", ""));
                    this.send("#confirmed"+i);
                }else if(received.contains("#start")){
                    int i  = Integer.valueOf(received.replace("#start", ""));
                    value = i;
                    mainGame = new MainGame(this, i, user );
                    mainGame.setVisible(true);
                }else if(received.contains("#return")){
                    received = received.replace("#return", "");
                    String[] splitData = received.split(";");
                    gameAction.setYou(Integer.valueOf(splitData[0]));
                    //gameAction.setEnemy(Integer.valueOf(splitData[1]));
                    gameAction.gameResult();
                    
                    this.send("#enemyrun"+Integer.valueOf(splitData[0]));
                    
                }else if(received.contains("#done")){
                    int i  = Integer.valueOf(received.replace("#done", ""));
                    //cekWinner(i);
                }else if(received.contains("#quit")){
                    JOptionPane.showMessageDialog(combo, "Enemy Quit!!");
                    mainGame.dispose();
                    this.send("#resetgame");
                }else if(received.contains("#enemyrun")){
                    int i  = Integer.valueOf(received.replace("#enemyrun", ""));
                    gameAction.setEnemy(Integer.valueOf(i));
                    this.send("#enemyrun"+Integer.valueOf(i));
                    gameAction.gameResult();
                }
                System.out.println(received);
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void setComboBox(JComboBox box){
       combo = box;
    }
    public void setRootPane(JRootPane rootPane){
        pane = rootPane;
    }
    public void setGameAction(GameAction ga){
        gameAction=ga;
    }
    
    public void runGame(int number){
        send("#run"+number+"#return0");
    }
}
