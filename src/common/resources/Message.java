/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.resources;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *

 * @author Many
 */
public class Message {
    
    private int user_id;
    private String message;

    public Message(Element info) {
        this.user_id = Integer.parseInt(info.getElementsByTagName("user-id").item(0).getTextContent());
        this.message = info.getElementsByTagName("text").item(0).getTextContent();
    }
    
    public Message(int user_id, String message) {
        this.user_id = user_id;
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getMessage() {
        return message;
    }
}
