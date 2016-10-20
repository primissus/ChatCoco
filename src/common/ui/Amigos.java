/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.ui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author saul
 */
public class Amigos extends JPanel{
     
    public String amigo1;
    public String amigo2;
    public String amigo3;
    
    public Amigos(){
        amigo1 = "Amigo1(offline)";
        amigo2 = "Amigo2(offline)";
        amigo3 = "Amigo3(offline)";
    }
    
    @Override
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.drawString(amigo1, 10, 10);
        g.drawString(amigo2, 10, 30);
        g.drawString(amigo3, 10, 50);
    }
}
