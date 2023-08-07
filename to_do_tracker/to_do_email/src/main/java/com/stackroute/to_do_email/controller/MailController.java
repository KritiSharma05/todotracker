package com.stackroute.to_do_email.controller;

import com.stackroute.to_do_email.model.EmailData;
import com.stackroute.to_do_email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/to-do-mail")
public class MailController {
    @Autowired
    private EmailService emailService;

    /*
    POST
     http://localhost:9999/to-do-mail/send-mail
     */
    @PostMapping("/send-mail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailData emailData){
        return new ResponseEntity<>(emailService.sendEmail(emailData), HttpStatus.MULTI_STATUS.OK);
    }

}
