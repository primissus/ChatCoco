  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.connection;

import cocoachat_server.CocoaServer;
import common.resources.XMLParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Many
 */
public class ConnectionClientServerSocket extends Thread{
    private CocoaServer cocoa;
    private ConnectionServerSocket server;
    private Socket socket;
    private int _id;
    private boolean run;
    public ConnectionClientServerSocket(Socket socket, CocoaServer cocoa, ConnectionServerSocket server) {
        this.socket = socket;        
        this.cocoa = cocoa;
        this.server = server;
    }       
    
    @Override
    public void run(){
        while(run){
            String data = receiveData();
            if(data!= null)
                cocoa.dataReceived(data, _id);
        }
    }
    
    public void sendData(String data){
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(data);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String receiveData(){
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            return in.readUTF();
        } catch (IOException ex) {
            try {
                socket.close();
                run=false;
                server.disconnectedUser(_id);
            } catch (IOException ex1) {
                Logger.getLogger(ConnectionClientServerSocket.class.getName()).log(Level.SEVERE, null, ex1);
            }            
        }
        return null;
    } 

    public void setRun(boolean run) {
        this.run = run;
    }

    public int get_id() {
        return _id;
    }    

    public void set_id(int _id) {
        this._id = _id;
    }
    
}
