/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.connection;

import common.resources.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User Sony
 */
public class SQLConnector{

    private Connection conn = null;
    private DriverManager driver;
    
    public SQLConnector() {
       
    }
    
    public void connect(String dBName,String user,String password){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/"+dBName+"?"
                    + "user="+user+"&password="+password);
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllFrom(String tableName){
        
        PreparedStatement preparedStatement;
        String select = "SELECT * FROM "+tableName;
        ResultSet resultSet;
        try {
            preparedStatement = conn.prepareStatement(select);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.first()){
                System.out.println("La tabla esta vacia");
                return;
            }
            while(!resultSet.isAfterLast()){
                System.out.println(resultSet.getString(1)+" "+resultSet.getString(2));
                resultSet.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Message[] getMessages(int user1, int user2){
        Message[] messages;
        String command = "SELECT org_id,mensaje FROM mensaje WHERE org_id="+user1+" AND des_id="+user2+" OR org_id="+user2+" AND des_id="+user1;
        PreparedStatement statement;
        
        try {
            statement = conn.prepareStatement(command);
            ResultSet results = statement.executeQuery();
            results.first();
            if(results.last()){
                messages = new Message[results.getRow()];
                results.first();               
                while(!results.isAfterLast()){
                    messages[results.getRow()-1] = new Message(results.getInt(1), results.getString(2));
                    results.next();
                }
                return messages;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getData(String table, String column, String comparingData){
        String command = "SELECT "+column+" FROM "+table+" WHERE "+comparingData;
        PreparedStatement statement;
        
        try {
            statement = conn.prepareStatement(command);
            ResultSet results = statement.executeQuery();
            results.first();
            return results.getString(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    public int getInt(String table, String column, String comparingData){
        String command = "SELECT "+column+" FROM "+table;
        if(!comparingData.isEmpty())
            command += "WHERE "+comparingData;
        PreparedStatement statement;
        
        try {
            statement = conn.prepareStatement(command);
            ResultSet result = statement.executeQuery();
            result.first();
            return result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public void updateData(String table, String update, String condition){
        String command = "UPDATE "+table+" SET "+update+" WHERE "+condition;
        PreparedStatement statement;
        
        try {
            statement = conn.prepareStatement(command);
            statement.execute();            
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteData(String table, String condition){
        String command = "DELETE "+table+" WHERE "+condition;
        PreparedStatement statement;
        
        try {
            statement = conn.prepareStatement(command);
            statement.execute();            
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int insertData(String table, String columns, String values){
        String command = "INSERT INTO "+table+" ("+columns+") VALUES ("+values+")";
        PreparedStatement statement;
        
        try {
            statement = conn.prepareStatement(command);
            statement.execute();
            
           
        } catch (SQLException ex) {
            if(ex.getErrorCode()==1062){
                return 2;
            }
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public String[] getColumnFrom(String table, String column){
        PreparedStatement preparedStatement;
        String select = "SELECT "+column+" FROM "+table;
        ResultSet resultSet;
        try {
            preparedStatement = conn.prepareStatement(select);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.first()){
                //System.out.println("La tabla esta vacia");
                return null;
            }
            resultSet.last();
            int length = resultSet.getRow();
            String [] rows = new String[length];
            resultSet.first();
            for(int i=0;!resultSet.isAfterLast();i++){
                rows[i] = resultSet.getString(1);
                resultSet.next();
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(SQLConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
