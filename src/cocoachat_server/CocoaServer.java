/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocoachat_server;

import common.connection.ConnectionServerSocket;
import common.connection.SQLConnector;
import common.resources.Message;
import common.resources.XMLParser;

/**
 *
 * @author Many
 */
public class CocoaServer {
    
    private SQLConnector conn;
    private ConnectionServerSocket server;

    public CocoaServer() {
        conn = new SQLConnector();
        conn.connect("chat", "root", "");
        server = new ConnectionServerSocket(this);
        server.setRun(true);
        server.start();
    }
    
    public String getConnectedUsers(){
        XMLParser xml = new XMLParser();
        int[] connected = server.getConnectedClients();
        if(connected!=null){
            xml.createUserList(connected);    
            return xml.getDocumentString();
        }
        return "";
    }    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        CocoaServer server = new CocoaServer();       
    }
    
    public void sendMessage(String data, int origin){
        XMLParser parser = new XMLParser(data);
        int dest = parser.getUserId();
        //parser = new XMLParser(parser.getContent());
    //    System.out.println(origin+parser.getUserId()+parser.getText());
        conn.insertData("mensaje", "org_id, des_id, mensaje", String.valueOf(origin)+","+String.valueOf(parser.getUserId())+",\""+parser.getText()+"\"");
        parser.replaceDoo(XMLParser.RECEIVE_MESSAGE);
        parser.replaceUserId(origin);
        server.sendData(parser.getDocumentString(), dest);
    }
    
    public void dataReceived(String data, int origin){
        XMLParser parser = new XMLParser(data);
        switch(parser.getDoo()){ 
            case XMLParser.SEND_MESSAGE:
                    sendMessage(data, origin);
                break;
            case XMLParser.RECEIVE_CONVERSATION:
                server.sendData(getConversation(origin, parser.getUserId()), origin);
                break;
        }
    }
    
    public String getConversation(int origin, int destiny){
        Message[] mensajes = conn.getMessages(origin, destiny);
        XMLParser documento = new XMLParser();
        documento.createRest(XMLParser.RECEIVE_CONVERSATION,"");
        if(mensajes != null){
            for(Message mensaje : mensajes){
                XMLParser parser = new XMLParser();
                parser.createMesage(mensaje.getUser_id(), mensaje.getMessage());
                documento.appendContent(parser.getDocumentString());
            }
        }
        return documento.getDocumentString();
    }
    
}
