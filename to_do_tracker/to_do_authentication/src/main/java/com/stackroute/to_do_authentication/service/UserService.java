package com.stackroute.to_do_authentication.service;

import com.stackroute.to_do_authentication.exception.UserAlreadyExistsException;
import com.stackroute.to_do_authentication.exception.UserNotFoundException;
import com.stackroute.to_do_authentication.feignclient.SignupData;
import com.stackroute.to_do_authentication.model.User;

public interface UserService {

    public abstract User registerUser(SignupData signupData) throws UserAlreadyExistsException;
    public abstract User login(String emailId, String password);
    public  abstract User resetPassword(User user, String currentPassword) throws UserNotFoundException;

    public  abstract boolean forgotPassword(User user) throws UserNotFoundException;


}
