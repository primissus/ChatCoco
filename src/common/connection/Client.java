/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.connection;

import common.resources.Message;
import common.resources.XMLParser;
import common.ui.Chat;
import common.ui.VentanaCliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GermanAMz
 */
public class Client extends Thread {
    private Socket _socket;
    private boolean _connOk = false;
    private final VentanaCliente _w;
    private final int _id;
    
    public Client(VentanaCliente w, int id) {
        this._w = w;
        this._id = id;
    }

    @Override
    public void run() {
        this.init();
    }
    
    private void init() {
        while(true) {
            try {
                this._socket = new Socket("25.109.73.99", 1234);
                this._connOk = true;
                this.sendMeId();
                this._w.setTitle("Cliente" + this._id + " {CONECTADO}");
                while(true) {
                    XMLParser rest = this.getXml();
                    switch(rest.getDoo()) {
                        case XMLParser.CONNECTED_IDS:
                            int ids[] = rest.getIDs();
                            this._w.setChats(ids, this._id);
                            for(int i = 0; i < ids.length; i++) {
                                if(ids[i] != _id){
                                    this.requestConversation(ids[i]);
                                    Chat c = this._w.getChat(ids[i]);
                                    XMLParser conversation = this.getXml();
                                    Message []messages = conversation.getMessages();
                                    for(int y = 0; y < messages.length; y++) {
                                        c.wrMessage(messages[y]);
                                    }
                                }
                            }
                            break;
                        case XMLParser.RECEIVE_MESSAGE:
                            int origin = rest.getUserId();
                            String content = rest.getText();
                            for(int i = 0; i < this._w.i; i++) {
                                Chat c = this._w.chats.get(i);
                                if(origin == c.id && origin != this._id) {
                                    c.wrMessage(new Message(origin,content));
                                }
                            }
                            break;
                        case XMLParser.RECEIVE_CONVERSATION:
                            break;
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    public boolean isConnOk() {
        return _connOk;
    }
    
    private String reciveString() {
	try {
	    DataInputStream iS = new DataInputStream(this._socket.getInputStream());
	    return iS.readUTF();
	} catch (IOException ex) {
	    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
    private void sendString(String str) {
	try {
	    DataOutputStream oS = new DataOutputStream(this._socket.getOutputStream());
	    oS.writeUTF(str);
	} catch (IOException ex) {
	    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    public int reciveId() {
        return this.getXml().getUserId();
    }
    
    public void requestConversation(int id) {
        XMLParser o = new XMLParser();
        XMLParser y = new XMLParser();
        y.createUserId(id);
        o.createRest(XMLParser.RECEIVE_CONVERSATION, y.getDocumentString());
        this.sendString(o.getDocumentString());
    }
    
    public void sendMeId() {
        XMLParser o = new XMLParser();
        o.createUserId(this._id);
        this.sendString(o.getDocumentString());
    }
    
    public void sendMessage(int dest, String content) {
        XMLParser o = new XMLParser();
        o.createMesage(dest, content);
        XMLParser y = new XMLParser();
        y.createRest(XMLParser.SEND_MESSAGE, o.getDocumentString());
        this.sendString(y.getDocumentString());
    }
    
    private XMLParser getXml() {
	return new XMLParser(this.reciveString());
    }
}
