package com.stackroute.to_do_email.service;

import com.stackroute.to_do_email.model.EmailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendEmail(EmailData emailData) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailData.getReceiver());
            mailMessage.setText(emailData.getMessageBody());
            mailMessage.setSubject(emailData.getSubject());
            System.out.println(mailMessage);
            javaMailSender.send(mailMessage);
            return "Mail Sent to" + emailData.getReceiver();
        } catch (Exception e) {
            e.printStackTrace();
            return "Sending Mail Failed...";
        }
    }

    @Override
    public String sendEmailToAdmin(EmailData emailData) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("moonsaha0501@gmail.com");
            mailMessage.setTo(emailData.getReceiver());
            mailMessage.setText(emailData.getMessageBody());
            mailMessage.setSubject(emailData.getSubject());
            System.out.println(mailMessage);
            javaMailSender.send(mailMessage);
            return "Mail Sent to" + emailData.getReceiver();
        } catch (Exception e) {
            e.printStackTrace();
            return "Sending Mail Failed...";
        }

    }
}
