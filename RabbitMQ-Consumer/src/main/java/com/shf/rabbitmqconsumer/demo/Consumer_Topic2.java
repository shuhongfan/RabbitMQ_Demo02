package com.shf.rabbitmqconsumer.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import lombok.SneakyThrows;

import java.io.IOException;

public class Consumer_Topic2 {

    @SneakyThrows
    public static void main(String[] args) {
//        1.创建工厂
        ConnectionFactory factory = new ConnectionFactory();

//        2.设置参数
        factory.setHost("192.168.120.20");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("root");
        factory.setPassword("root");

//        3.创建连接
        Connection connection = factory.newConnection();

//        4.创建channel
        Channel channel = connection.createChannel();

        /**
         * 声明一个队列
         * 参形：
         * queue – 队列的名称
         * durable - 如果我们声明一个持久队列，则为真（该队列将在服务器重新启动后继续存在）
         * exclusive - 如果我们声明一个独占队列，则为真（仅限于此连接）
         * autoDelete – 如果我们声明一个自动删除队列，则为 true（服务器将在不再使用时将其删除）
         * arguments – 队列的其他属性（构造参数）
         * 返回值：
         * 声明队列已成功声明的声明确认方法
         * 抛出：
         * IOException – 如果遇到错误
         */
//        5.创建队列Queue
        channel.queueDeclare(
                "hello_world",
                true,
                false,
                false,
                null);

        String queueName1 = "test_topic_queue1";
        String queueName2 = "test_topic_queue2";

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
//                System.out.println("consumerTag = " + consumerTag);
//                System.out.println("envelope = " + envelope);
//                System.out.println("properties = " + properties);
                System.out.println("body = " + new String(body));
            }
        };
        channel.basicConsume(queueName2, consumer);

    }
}
