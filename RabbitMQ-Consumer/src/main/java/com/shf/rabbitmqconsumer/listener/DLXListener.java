package com.shf.rabbitmqconsumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DLXListener {
    @RabbitListener(queues = "test_queue_dlx",ackMode = "MANUAL",containerFactory = "mqContainerFactory")
    public void receive(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println(new String(message.getBody()));
            int i = 3 / 0;
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
//            4. 拒绝签收，不会再次投递到队列中
            channel.basicNack(deliveryTag, true, false);
        }

    }
}
