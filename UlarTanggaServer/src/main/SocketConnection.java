/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JTextPane;

/**
 *
 * @author Muntari
 */
public class SocketConnection {
    private static int port = 80;
    private ServerSocket server;
    static Vector player = new Vector();
    static Vector name = new Vector();
    private PrintWriter out ;
    private Console csl;
    public SocketConnection(JTextPane textPanel){
        csl = new Console(textPanel);
    }
    protected void start() throws IOException {
        try {
            ServerSocket tmp = new ServerSocket(port);
            csl.setConsole("Start on port 80");
            server = tmp;
        } catch (Exception e) {
            csl.setConsole("Error: " + e);
            return;
        }
        
        while(true){
            Socket client = server.accept();
            String id = String.valueOf(player.size());
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String name=in.readLine();
            csl.setConsole(name+" has joined! with id "+id);
            this.name.add(name);
            
            PlayGame list = new PlayGame(client, csl, id, name);    
            player.add(list);
            list.start();
        }
    }
    protected void stop() {
        try {
            server.close();
            csl.setConsole("Stop on port 80");
        } catch (Exception e) {
            csl.setConsole("Error: " + e.getMessage());
        }
    }
    class PlayGame extends Thread{
    private String id;
    private String username; 
    private String enemy; 
    private Boolean turn =false; 
    private Integer value; 
    private Integer enemyBlock=0; 
    private Integer yourBlock=0;
    private Console csl;
    private Socket client;
        private BufferedReader in ;
        private PrintWriter out ;
        public PlayGame(Socket socket, Console csl, String id, String name){
            this.csl = csl;
            this.id = id;
            this.username = name;
            client = socket;
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(),true);
            } catch (Exception e) {
                csl.setConsole("Error "+e.getMessage());
            }
        }
        public Boolean cekTurn(){
            return turn;
        }
        public boolean cekGame(){
            if(enemyBlock>64 || yourBlock >64){
                return true;
            }
            return false;
        }
        public void resetGame(){
           turn = false;
           yourBlock = 0;
           enemyBlock = 0;
        }
        @Override
        public void run() {
            try {
                String received;
                while(true){
                    received = in.readLine();
                    if(received.equals("listPlayer")){
                        String list = "#player";
                        csl.setConsole(""+SocketConnection.name.size());
                        for(int i=0;i< SocketConnection.name.size();i++) {
                            if(!String.valueOf(i).equals(id) &&  SocketConnection.name.get(i) != null){
                                list += i+"-"+SocketConnection.name.get(i)+";";
                            }
                        }
                        csl.setConsole(list);
                        out.println(list);
                    }else if(received.contains("#battle")){
                         int i  = Integer.valueOf(received.replace("#battle", ""));
                         PlayGame playerBattle = (PlayGame) SocketConnection.player.get(i);
                         playerBattle.out.println("#accept"+id);
                    }else if(received.contains("#yesBattle")){
                        int i  = Integer.valueOf(received.replace("#yesBattle", ""));
                        
                        PlayGame playerBattle = (PlayGame) SocketConnection.player.get(i);
                        if(playerBattle != null){
                            playerBattle.out.println("#confirmed"+id);
                            enemy = String.valueOf(i);
                            out.println("#start0");
                            value = 0;
                            csl.setConsole( "acc player"+enemy);
                        }
                    }else if(received.contains("#confirmed")){
                        int i  = Integer.valueOf(received.replace("#confirmed", ""));
                        enemy = String.valueOf(i);
                        out.println("#start1");
                        value = 1;
                    }else if(received.contains("#resetgame")){
                        resetGame();
                        value = 1;
                    }else if(received.contains("#run")){
                        csl.setConsole(""+received);
                        String list ;
                        String[] splitData = received.split("#return");
                        String running = splitData[0];
                        int data = Integer.valueOf(splitData[1]);
                        int run  = Integer.valueOf(running.replace("#run", ""));
                        yourBlock +=run;
                        enemyBlock +=data;
                        if(yourBlock == 4){
                            yourBlock =12;
                        }
                        if(yourBlock == 10){
                            yourBlock =43;
                        }
                        if(yourBlock == 13){
                            yourBlock =7;
                        }
                        if(yourBlock == 15){
                            yourBlock =30;
                        }
                        if(yourBlock == 35){
                            yourBlock =50;
                        }
                        if(yourBlock == 52){
                            yourBlock =18;
                        }
                        if(yourBlock == 38){
                            yourBlock =20;
                        }
                        if(yourBlock == 60){
                            yourBlock =39;
                        }
                        int i = Integer.valueOf(enemy);
                        PlayGame playerBattle = (PlayGame) SocketConnection.player.get(i);
                        list = "#enemyrun"+yourBlock;
                        playerBattle.out.println(list);
                        System.out.println(list);
                        list = "#return"+yourBlock;
                        out.println(list);
                        System.out.println(list);
                        
                        if(cekGame()){
                            list = "#done";
                            if(value == 0){
                                playerBattle.out.println(list+"1");
                            }else{
                                playerBattle.out.println(list+"0");
                            }
                            out.println(list+""+value);
                        }
                    }else if(received.contains("#enemyrun")){
                       int b  = Integer.valueOf(received.replace("#enemyrun", ""));
                       enemyBlock += b;
                       //int i = Integer.valueOf(enemy);
                       //PlayGame playerBattle = (PlayGame) SocketConnection.player.get(i);
                       //String list = "#enemyrun"+enemyBlock;
                       //System.out.println("asd"+list);
                       //playerBattle.out.println(list);
                    }
                }
            } catch (Exception e) {
               csl.setConsole("error: "+e);
            }finally{
               try{
                    if (client != null){
                        csl.setConsole( username+" telah keluar!");
                        name.set(Integer.valueOf(id), null);
                        player.set(Integer.valueOf(id), null);
                        if(enemy != null){
                            int i  = Integer.valueOf(enemy);
                            PlayGame playerBattle = (PlayGame) SocketConnection.player.get(i);
                            playerBattle.out.println("#quit"+id);
                        }
                        resetGame();
                        client.close();
                    }
                }catch(IOException e){
                }
            }
        }
    }
}
