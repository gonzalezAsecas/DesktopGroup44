/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.logic;

import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import exceptions.WrongPasswordException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.User;

/**
 * @version 1.0
 * @author Lander Lluvia
 */
public class ClientSocket {
    
    private final static Logger LOG = Logger.getLogger("serverLogger");
    
    private int port;
    private String ip;
    
    /**
     * Method that sends a message(user+numeric_identifier) and receives another
     * numeric identifier. Depending of its value, it will do nothing or 
     * return an exception.
     * @param user contains all the data necessary
     * @exception LoginExistingException in case that the user already exists
     * @exception EmailNotUniqueException in case that the email is already used
     * @exception Exception in case that another error happens
     */
    public void signUp(User user) throws LoginExistingException, 
            EmailNotUniqueException, Exception{
        config();
        Socket client = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try{
            client = new Socket(ip, port);
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            
            Message out = new Message(2, user);
            output.writeObject(out);
            
            Message in = (Message) input.readObject();
            
            int in_value = in.getMessage();
            
            switch(in_value){
                case 20:
                    break;
                case 21:
                    throw new LoginExistingException();
                case 22:
                    throw new EmailNotUniqueException();
                case -2:
                    throw new Exception();
            }
        }finally {
            try{
                if (client != null)
                    client.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
            }catch (IOException e) {
                LOG.severe("An IOException in ClientSocket signUp() has happened");
            }
        }
    }       
    
    /**
     * Method that sends a message(user+numeric_identifier) and receives another
     * numeric identifier. Depending of its value, it will do nothing or 
     * return an exception.
     * @param user contains all the data necessary
     * @return 
     * @exception LoginNotExistingException in case that the user doesn't exists
     * @exception WrongPasswordException in case that the password is wrong
     * @exception Exception in case that another error happens
     */
    public User logIn(User user) throws LoginNotExistingException, 
            WrongPasswordException, Exception{
        config();
        Socket client = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        Message in = new Message();
        try{
            client = new Socket(ip, port);
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
            
            Message out = new Message(1, user);
            output.writeObject(out);
            in = (Message) input.readObject();
            int in_value = in.getMessage();
            
            switch(in_value){
                case 10:
                    break;
                case 11:
                    throw new LoginNotExistingException();
                case 12:
                    throw new WrongPasswordException();
                case -1:
                    throw new Exception();
            }
        }finally {
            try {
                if(client != null)
                    client.close();
                if (input != null)
                    input.close();
                if(output != null)
                    output.close();
           }catch(IOException e){
                LOG.severe("An IOException in ClientSocket logIn() has happened");
           }
        }
        return (User) in.getData();
    }

    private void config() throws IOException {
        ResourceBundle urlFich= ResourceBundle.getBundle("project.logic.parameters");
        ip=urlFich.getString("servername");
        port=Integer.parseInt(urlFich.getString("port"));
        /*
        Properties prop= new Properties();
        try{
        FileInputStream fis= new FileInputStream("src/project/logic/parameters.properties");
        prop.load(fis);
        }catch(FileNotFoundException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        ip=prop.getProperty("servername");*/
    }

}
