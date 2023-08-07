package com.stackroute.to_do_email.service;

import com.stackroute.to_do_email.model.EmailData;
import org.springframework.stereotype.Service;


@Service
public interface EmailService {
    public String sendEmail(EmailData emailData);
    public String sendEmailToAdmin(EmailData emailData);


}
