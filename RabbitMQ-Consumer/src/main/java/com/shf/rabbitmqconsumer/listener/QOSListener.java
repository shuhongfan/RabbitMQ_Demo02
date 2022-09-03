package com.shf.rabbitmqconsumer.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import java.io.IOException;

@Component
public class QOSListener {
    @RabbitListener(queues = "test_queue_confirm",ackMode = "MANUAL",containerFactory = "mqContainerFactory")
    public void receiveConfirm(Message message, Channel channel) throws IOException {
        System.out.println(new String(message.getBody()));

        channel.basicAck(
                message.getMessageProperties().getDeliveryTag(),
                true);
    }
}
