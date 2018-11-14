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
import message.User;

/**
 *
 * @author Jon Gonzalez
 */
public interface Logic {
    public User loginUser(User user) throws LoginNotExistingException, 
            WrongPasswordException, Exception;
    public void signUpUser(User user)throws LoginExistingException,
            EmailNotUniqueException, Exception;
}
