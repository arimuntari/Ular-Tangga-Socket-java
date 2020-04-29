/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Muntari
 */
public class GameAction {
    int you, enemy;
    private JLabel lblYou, lblEnemy;
    int[] cars = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    public GameAction(JLabel lblYou, JLabel lblEnemy){
        this.you = 0;
        this.enemy = 0;
        this.lblYou = lblYou;
        this.lblEnemy = lblEnemy;
        //gameResult();
    }
    
    public void gameResult(){
        move();
    }
    public void setYou(int move){
        this.you =move;
    }
    public void setEnemy(int move){
        this.enemy = move;
    }
    public void move(){
        int moveYouX=0;
        int moveEnemyX=0;
        int moveYouY= 350-(you/8*50);
        if(you==0){
            moveYouY = 350;
            moveYouX = 0;
        }else{
            if((you/8)%2 == 0){
                if(you%8==0){
                    moveYouY+=50;
                    moveYouX = 50+(you%8)*50;      
                }else{
                    moveYouX = 0+(you%8)*50;     
                }
            }else{
                if(you%8==0){
                    moveYouY+=50;
                    moveYouX = 400-(you%8)*50; 
                }else{
                    moveYouX = 450-(you%8)*50;    
                }
            }
            
        }
        int moveEnemyY= 370-(enemy/8*50);
        if(you==0){
            moveEnemyY = 370;
            moveYouX = 0;
        }else{
            if((enemy/8)%2 == 0){
                if(enemy%8==0){
                    moveEnemyY+=50;
                    moveEnemyX = 50+(enemy%8)*50;      
                }else{
                    moveEnemyX = 0+(enemy%8)*50;     
                }
            }else{
                if(enemy%8==0){
                    moveEnemyY+=50;
                    moveEnemyX = 400-(enemy%8)*50; 
                }else{
                    moveEnemyX = 450-(enemy%8)*50;    
                }
            }
        }
        //System.out.println("Dice"+you+"#"+moveYouY+"Y" +moveYouX+"X");
        this.lblYou.setBounds(moveYouX, moveYouY, lblYou.getHeight(), lblYou.getWidth());
        this.lblEnemy.setBounds(moveEnemyX, moveEnemyY, lblEnemy.getHeight(), lblEnemy.getWidth());

    }
}
