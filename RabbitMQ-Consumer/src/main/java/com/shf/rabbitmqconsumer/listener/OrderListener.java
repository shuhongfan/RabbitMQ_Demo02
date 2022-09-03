package com.shf.rabbitmqconsumer.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    @RabbitListener(queues = "order_queue_dlx", ackMode = "MANUAL", containerFactory = "mqContainerFactory")
    public void receive(Message message) {
        System.out.println(new String(message.getBody()));
    }
}
