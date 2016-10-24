/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.ui;

import common.connection.Client;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author saul
 */
public class VentanaCliente extends JFrame {

    private GroupLayout main;
    private JTabbedPane tabs;
    public ArrayList<Chat> chats = new ArrayList<Chat>();
    private final Client _client;
    public int i = 0;
    private int id = 0;
    
    public VentanaCliente(int id){
        iniComponents();
        createForm();
        this.id = id;
        this._client = new Client(this, id);
        this._client.start();
    }
    
    public VentanaCliente(){
        iniComponents();
        createForm();
        this._client = new Client(this, 14);
    }
    
    private void iniComponents(){          
        tabs = new JTabbedPane();
       
    }
    
    public void addChat(int id) {
        if(this.i < 4) {
            this.chats.add(new Chat(id, this, this._client));
            this.tabs.add(this.chats.get(this.i++));
        }
    }
    
    public boolean hasChat(int id) {
        boolean r = false;
        for(int j = 0; j < this.i; j++) {
            if(this.chats.get(j).id == id) {
                r = true;
            }
        }
        return r;
    }
    
    public void setChats(int []ids, int id) {
        for(int i = 0; i < ids.length; i++) {
            if(ids[i] != id) {
                if(!hasChat(ids[i])) {
                    addChat(ids[i]);
                }
            }
        }
        
        for(int i = 0; i < this.i; i++) {
            if(!this.isIn(this.chats.get(i).id, ids)) {
                this.tabs.remove(this.chats.get(i));
                this.chats.remove(i);
                this.i--;
            }
        }
    }
    
    public Chat getChat(int id) {
        for(int i = 0; i < this.chats.size(); i++) {
            if(this.chats.get(i).id == id) {
                return this.chats.get(i);
            }
        }
        return null;
    }
    
    private boolean isIn(int d, int []a) {
        boolean r = false;
        for(int i = 0; i < a.length; i++) {
            if(d == a[i]) {
                r = true;
            }
        }
        return r;
    }
    
    private void createForm(){
        main = new GroupLayout(this.getContentPane());
        
        this.setSize(450,300);
        
        this.setLayout(main);
        
        setHGroup();
        setVGroup();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addChat(20);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    private void setHGroup(){
        main.setHorizontalGroup(
            main.createSequentialGroup()
            .addComponent(tabs)
        );
    }
    
    public int get_id(){
        return this.id;
    }
    
    private void setVGroup(){
        main.setVerticalGroup(
            main.createParallelGroup()
            .addComponent(tabs)
        );
    }
    
}
