package com.stackroute.to_do_email.rabbitmq;

import com.stackroute.to_do_email.model.EmailData;
import com.stackroute.to_do_email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailConsumer {
    private EmailService emailService;

    @Autowired
    public MailConsumer(EmailService emailService){
        this.emailService = emailService;
    }

    @RabbitListener(queues = "mail_queue")
    public void getEmailDtoFromQueue(EmailDTO emailDTO){
        EmailData emailData = new EmailData(emailDTO.getReceiver(),
                emailDTO.getMessageBody(),emailDTO.getSubject(),null);
        if(emailData.getReceiver().equals("dummycapstonproject@gmail.com")){
            emailService.sendEmailToAdmin(emailData);
        }
        else {
            System.out.println(emailService.sendEmail(emailData));
        }

    }


}
