/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.ui;

import common.connection.Client;
import common.resources.Message;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author saul
 */
public class Chat extends JPanel{
    
    public int id;
    private VentanaCliente _w;
    private Client _c;
    private final JTextArea textArea;
    private final JTextField tfMensaje;
    private final JButton btnEnviar;
    
    public Chat(int id, VentanaCliente w, Client c){
        this._w = w;
        this.id = id;
        this._c = c;
        textArea = new JTextArea();
        tfMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        
        this.btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _c.sendMessage(id, tfMensaje.getText());
            }
        });
        GroupLayout pt = new GroupLayout(this);
        pt.setAutoCreateContainerGaps(true);
        pt.setAutoCreateGaps(true);
        
        pt.setHorizontalGroup(
            pt.createParallelGroup()
            .addComponent(textArea,200,200,200)
            .addGroup(
                pt.createSequentialGroup()
                .addComponent(tfMensaje,50,50,50)
                .addComponent(btnEnviar)
            )
        );
        
       pt.setHorizontalGroup(
            pt.createSequentialGroup()
            .addComponent(textArea,50,50,50)
            .addGroup(
                pt.createParallelGroup()
                .addComponent(tfMensaje)
                .addComponent(btnEnviar)
            )
        );
    }
    
    public void wrMessage(String content) {
        
    }
    
    public void wrMessage(Message m) {
        
    }
}
