/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocoachat_client;

import common.ui.VentanaCliente;
import javax.swing.JOptionPane;

/**
 *
 * @author GermanAMz
 */
public class Cocoachat_client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String id = JOptionPane.showInputDialog("introduce tu id");
        VentanaCliente v = new VentanaCliente(Integer.parseInt(id));
    }
    
}
