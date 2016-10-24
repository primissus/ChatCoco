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
import javax.swing.JScrollPane;
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
    private final JScrollPane scroll;
    
    public Chat(int id, VentanaCliente w, Client c){
        this._w = w;
        this.id = id;
        this._c = c;
        textArea = new JTextArea();
        tfMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        scroll = new JScrollPane(textArea);
        
        
        this.btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!tfMensaje.getText().isEmpty())
                _c.sendMessage(id, tfMensaje.getText());
                textArea.append("\n"+String.valueOf(w.get_id())+": "+tfMensaje.getText());
                tfMensaje.setText("");
            }
        });
        GroupLayout pt = new GroupLayout(this);
        pt.setAutoCreateContainerGaps(true);
        pt.setAutoCreateGaps(true);
        
        pt.setHorizontalGroup(
            pt.createParallelGroup()
            .addComponent(scroll,200,200,200)
            .addGroup(
                pt.createSequentialGroup()
                .addComponent(tfMensaje)
                .addComponent(btnEnviar)
            )
        );
        
       pt.setVerticalGroup(
            pt.createSequentialGroup()
            .addComponent(scroll,200,200,200)
            .addGroup(
                pt.createParallelGroup()
                .addComponent(tfMensaje,25,25,25)
                .addComponent(btnEnviar)
            )
        );
       this.setLayout(pt);
    }
    
    public void wrMessage(String content) {
        textArea.append("\n"+content);
    }
    
    public void wrMessage(Message m) {
        textArea.append("\n"+String.valueOf(m.getUser_id())+": "+m.getMessage());
    }
}
