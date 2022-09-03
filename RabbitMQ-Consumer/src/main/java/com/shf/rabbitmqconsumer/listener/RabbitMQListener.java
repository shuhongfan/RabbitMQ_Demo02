package com.shf.rabbitmqconsumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class RabbitMQListener {
    @RabbitListener(queues = "boot_queue")
    public void receive(Message message) {
        System.out.println("Received message: " + message);
    }


}
