package com.shf.rabbitmqproducer;

import com.shf.rabbitmqproducer.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitMqProducerApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "boot.haha",
                "Boot mq heelo ~~~");
    }


    /**
     * 确认模式
     * 1.确认模式开启
     * 2.在rabbitTemplate定义ConfirmCallBack回调函数
     */
    @Test
    public void testConfirm() {
//        定义回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 回调函数
             * @param correlationData 相关配置信息
             * @param b  是否成功
             * @param s 错误信息
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("correlationData: " + correlationData);
                System.out.println("b: " + b);
                System.out.println("s: " + s);
            }
        });

//        发送消息
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONFIRM_EXCHANGE_NAME,
                "confirm",
                "Boot mq heelo ~~~");
    }


    /**
     * 回退模式：当消息发送给Exchange后，Exchange路由到Queue失败时，才会执行ReturnCallBack
     * 步骤：
     * 1. 开启回退模式
     * 2. 设置ReturnCallBack
     * 3. 设置Exchange处理消息的模式
     * 1. 如果消息没有路由到Queue，则丢弃消息（默认）
     * 2. 如果消息没有路由到Queue，返回消息到发送方（returnCallback）
     */
    @Test
    public void testReturn() {
//        设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);

//        设置ReturnCallback
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 回调函数
             * @param message 消息对象
             * @param i 丢弃消息返回码
             * @param s 丢弃消息原因
             * @param s1 丢弃消息交换机
             * @param s2 丢弃消息路由键
             */
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("message: " + message);
                System.out.println("i: " + i);
                System.out.println("s: " + s);
                System.out.println("s1: " + s1);
                System.out.println("s2: " + s2);
            }
        });

//        3.发送消息
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONFIRM_EXCHANGE_NAME,
                "confirm",
                "Boot mq heelo ~~~");
    }

    @Test
    public void testSend() {
        for (int i = 0; i < 10; i++) {

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CONFIRM_EXCHANGE_NAME,
                    "confirm",
                    "Boot mq heelo ~~~");
        }
    }

    /**
     * 设置队列过期时间使用参数: x-message-ttl，单位: ms(毫秒)，会对整个队列消息统一过期。
     * 设置消息过期时间使用参数: expiration。单位: ms(毫秒)，当该消息在队列头部时（消费时)，会单独判断
     * 这—消息是否过期。
     * >如果两者都进行了设置,以时间短的为准。
     */
    @Test
    public void testSend2() {

        /**
         * 消息的后处理对象，设置一些的参数信息
         */
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
//                1. 设置message的信息
                message.getMessageProperties().setExpiration("10000");
//                2. 返回该消息
                return message;
            }
        };

//        消息单独过期
//        如果设置了消息的过期，时间，也设置了队列的过期时间，以时间短的为准
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.TTL_EXCHANGE_NAME,
                        "ttl.cache",
                        "Boot mq ttl ~~~",
                        messagePostProcessor);
            } else {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.TTL_EXCHANGE_NAME,
                        "ttl.cache",
                        "Boot mq ttl ~~~");
            }

        }

    }


    /**
     * 发送测试死信消息
     */
    @Test
    public void testDlx() {
//        1. 测试过期消息
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.DLX_EXCHANGE_NAME,
//                "test.dlx.cache",
//                "我是死信消息");

//        2.测试长度限制后，发送死信消息
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DLX_EXCHANGE_NAME,
                    "test.dlx.cache",
                    "我是死信消息");
        }

//        3.测试消息拒收
    }

    @Test
    public void testDelay() {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE_NAME,
                "order.32",
                "订单信息");
    }
}
