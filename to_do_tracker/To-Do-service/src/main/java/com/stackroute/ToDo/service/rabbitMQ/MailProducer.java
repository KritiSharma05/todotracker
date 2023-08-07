package com.stackroute.ToDo.service.rabbitMQ;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailProducer {
    //dependencies : rabbitTemplate, exchange
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange directExchange;
    public void sendEmailDtoQueue(EmailDTO emailDTO){
        //binding name : mail_binding
        rabbitTemplate.convertAndSend(directExchange.getName(),"mail_binding",emailDTO);
    }
}
