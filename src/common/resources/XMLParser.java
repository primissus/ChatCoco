/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author User Sony
 */
public class XMLParser {
    
    public static final int RECEIVE_ID = 0;
    public static final int SEND_MESSAGE = 1;
    public static final int RECEIVE_MESSAGE = 2;
    public static final int RECEIVE_CONNECTED = 3;
    public static final int RECEIVE_CONVERSATION = 4;
    public static final int CONNECTED_IDS = 5;
    
    private Document document;
    
    public XMLParser() {}
    public XMLParser(String content) {
        this.document = this.createParsedDoc(content);
    }
    
    private Document createParsedDoc(String content) {
        try {
            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(content)));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getContent() {
        return this.document.getElementsByTagName("content").item(0).getTextContent();
    }
    
    public String getText() {
        return this.document.getElementsByTagName("text").item(0).getTextContent();
    }
    
    public int getUserId() {
        return Integer.parseInt((this.document.getElementsByTagName("user-id")).item(0).getTextContent());
    }
    
    public int getDoo() {
        return Integer.parseInt((this.document.getElementsByTagName("doo")).item(0).getTextContent());
    }
    
    public void replaceDoo(int newDoo) {
        this.document.getElementsByTagName("doo").item(0).setTextContent(String.valueOf(newDoo));
    }
    
    public void replaceUserId(int newID) {
        this.document.getElementsByTagName("user-id").item(0).setTextContent(String.valueOf(newID));
    }
    
    public Message[] getMessages() {
        Message l[];
        NodeList list = this.document.getElementsByTagName("message");
        l = new Message[list.getLength()];
        for(int i = 0; i < l.length; i++) {
            l[i] = new Message((Element) list.item(i));
        }
        return l;
    }
    
    public void appendContent(String extraContent){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Node el = document.getElementsByTagName("content").item(0);
            if(el == null){
                el = document.createElement("content");
                Node rest = document.getElementsByTagName("rest").item(0);
                rest.appendChild(el);
            }
            Document aux = builder.parse(new ByteArrayInputStream(extraContent.getBytes()));
            Node node = this.document.importNode(aux.getDocumentElement(), true);
            el.appendChild(node);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createRest(int doo, String content){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("rest");
            document.appendChild(root);
            Element el;
            el = document.createElement("doo");
            el.setTextContent(String.valueOf(doo));
            root.appendChild(el);
            el = document.createElement("content");
            Document aux = builder.parse(new ByteArrayInputStream(content.getBytes()));
            Node node = this.document.importNode(aux.getDocumentElement(), true);
            el.appendChild(node);
            root.appendChild(el);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createMesage(int destUser, String text){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("message");
            document.appendChild(root);
            Element el;
            el = document.createElement("user-id");
            el.setTextContent(String.valueOf(destUser));
            root.appendChild(el);          
            el = document.createElement("text");
            el.setTextContent(text);
            root.appendChild(el);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createUserId(int id){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("user");
            document.appendChild(root);
            Element el;
            el = document.createElement("user-id");
            el.setTextContent(String.valueOf(id));
            root.appendChild(el);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createUserList(int[] IDs) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("users");
            document.appendChild(root);            
            for(int i=0;i<IDs.length;i++){
                Attr attr = document.createAttribute("user"+(i+1));
                attr.setValue(String.valueOf(IDs[i]));
                root.setAttributeNode(attr);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int[] getIDs() {
        int[] ids;
        NodeList els = this.document.getElementsByTagName("users");
        Node el = els.item(0);
        NamedNodeMap attrs = el.getAttributes();
        if(attrs.getLength() == 0)
            return null;
        ids = new int[attrs.getLength()];
        for(int i=0;i<attrs.getLength();i++){
            ids[i] = Integer.parseInt(attrs.item(i).getNodeValue());
        }
        return ids;
    }
    
    public String getDocumentString() {
        TransformerFactory t = TransformerFactory.newInstance();
	try {
	    Transformer tf = t.newTransformer();
	    tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    StringWriter writer = new StringWriter();
	    tf.transform(new DOMSource(document), new StreamResult(writer));
	    String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
	    return output;
	} catch (TransformerConfigurationException ex) {
	    Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
	} catch (TransformerException ex) {
	    Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
}
