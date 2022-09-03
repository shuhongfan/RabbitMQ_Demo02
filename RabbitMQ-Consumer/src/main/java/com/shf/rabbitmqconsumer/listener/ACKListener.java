package com.shf.rabbitmqconsumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class ACKListener {
    /**
     * ackMode = "MANUAL" 手动确认
     *
     *  1.2 Consumer Ack 小结
     * 在rabbitlistener-container标签中设置acknowledge属性，设置ack方式 none:自动确认，manual:手动确认
     * 如果在消费端没有出现异常，则调用channel.basicAck(deliveryTag,false);方法确认签收消息
     * 如果出现异常，则在catch中调用basicNack或 basicReject，拒绝消息，让MQ重新发送消息。
     *
     * @param message
     */
    @RabbitListener(queues = "test_queue_confirm", ackMode = "MANUAL")
    public void receiveConfirm(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
//        1.接收并转换消息
            System.out.println("Received message: " + message);
            System.out.println(new String(message.getBody()));

//        2. 处理业务逻辑
            System.out.println("处理业务逻辑");
//            int i = 3 / 0; // 手动模拟错误

//        3.手动签收
            channel.basicAck(deliveryTag, true);
            System.out.println("手动签收成功");
        } catch (Exception e) {
            /**
             * 4. 消息拒绝，重新回到队列
             */
            channel.basicNack(
                    deliveryTag,
                    true,
                    true); // 重回队列
        }
    }
}
