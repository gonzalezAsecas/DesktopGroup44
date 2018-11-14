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
import java.util.logging.Level;
import java.util.logging.Logger;
import message.User;

/**
 *
 * @author Jon Gonzalez
 */
public class LogicImplementation implements Logic{
    
    private static final Logger LOG = 
            Logger.getLogger(LogicImplementation.class.getName());
    
    private ClientSocket client;
    
    LogicImplementation() {
        this.client = new ClientSocket();
    }
    
    /**
     * The login of the user in the logic implementation
     * @param user
     * @return
     * @throws LoginNotExistingException
     * @throws WrongPasswordException
     * @throws Exception 
     */
    @Override
    public User loginUser(User user) throws LoginNotExistingException,
            WrongPasswordException, Exception {
            LOG.info("Login user in the logic implementation.");
            return client.logIn(user);
    }
    
    /**
     * The signup of the user in the logic implementation
     * @param user
     * @throws LoginExistingException
     * @throws EmailNotUniqueException
     * @throws Exception 
     */
    @Override
    public void signUpUser(User user) throws LoginExistingException, 
            EmailNotUniqueException, Exception {
        LOG.info("Signup user in the logic implementation.");
        client.signUp(user);
    }
}
