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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Many
 */
public class ConnectionServerSocket extends Thread{
    
    private ServerSocket server;
    private ArrayList<ConnectionClientServerSocket> clients;      
    private boolean run;
    private CocoaServer cocoaServer;
    private boolean onLine;

    public ConnectionServerSocket(CocoaServer cocoaServer) {
        this.cocoaServer = cocoaServer;
        clients = new ArrayList<>();
    }
    
    @Override
    public void run(){
        Socket socket;        
        try {            
            server = new ServerSocket(1234);              
            while(run){                
                socket = server.accept();                
                ConnectionClientServerSocket client = new ConnectionClientServerSocket(socket, cocoaServer, this);
                XMLParser xml = new XMLParser(client.receiveData());
                int id = xml.getUserId();
                client.set_id(id);
                clients.add(client);                
                client.setRun(true);
                client.start();
                String con = cocoaServer.getConnectedUsers();                
                xml.createRest(XMLParser.CONNECTED_IDS, con);
                con = xml.getDocumentString();
                System.out.println(con);
                broadcastData(con);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRun(boolean run) {
        this.run = run;
    }       
    
    public void broadcastData(String data){       
        for(ConnectionClientServerSocket client : clients)
            client.sendData(data);
    }

    public boolean isOnLine() {
        return onLine;
    }
    
    public int[] getConnectedClients(){
        int[] connected;
        if(!clients.isEmpty()){
            connected = new int[clients.size()];
            for(int i=clients.size()-1; i>=0;i--){
                connected[i] = clients.get(i).get_id();
            }
            return connected;
        }
        return null;
    }
    
    public void sendData(String data, int dest){
        int i;
        boolean found = false;
        for(i=clients.size()-1; i>=0; i--){
            if(clients.get(i).get_id()==dest){
                found = true;
                break;
            }
        }
        if(found)
            clients.get(i).sendData(data);
    }
    
    public void disconnectedUser(int id){
        for(int i=0; i<clients.size();i++)
            if(clients.get(i).get_id() == id)
                clients.remove(i);
        this.broadcastData(cocoaServer.getConnectedUsers());
    }
    
}

