package com.shf.rabbitmqproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.1消息的可靠投递小结
 * >设置ConnectionFactory的publisher-confirms="true"开启确认模式。
 * 使用rabbitTemplate.setConfirmCallback设置回调函数。当消息发送到exchange后回
 * 调confirm方法。在方法中判断ack，如果为true，则发送成功，如果为false，则发送失败,需要处理。
 *
 * 设置ConnectionFactory的publisher-returns="true"开启退回模式。
 * 使用rabbitTemplate.setReturnCallback设置退回函数，当消息从exchange路由到
 * queue失败后，如果设置了rabbitTemplate.setMandatory(true)参数，则会将消息退回给producer。并执行回调函数returnedMessage。
 *
 * 在RabbitMQ中也提供了事务机制，但是性能较差，此处不做讲解。使用channel下列方法，完成事务控制:
 * txSelect().用于将当前channel设置成transaction模式txCommit()，用于提交事务
 * txRollback(),用于回滚事务
 */
@SpringBootApplication
public class RabbitMqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqProducerApplication.class, args);
    }

}
