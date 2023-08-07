package com.stackroute.ToDo.service.service;

import com.stackroute.ToDo.service.exception.UserNotFoundException;
import com.stackroute.ToDo.service.model.Task;
import com.stackroute.ToDo.service.model.User;


import org.springframework.stereotype.Service;

//import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.List;

@Service
public interface EmailService {
    String sendSimpleMail(User user);
    String emailReminders() throws UserNotFoundException, ParseException;
    public void feedBack(String emailId);

    public void feedBackToAdmin(String message,String from);
    void updateProfile(User user);

    List<Task> getTasksWithNearDueDate(String userEmail) throws UserNotFoundException,ParseException;

    public List<Task> getTasksWithOverDue(String userEmail) throws UserNotFoundException,ParseException;
}
