package com.shf.rabbitmqproducer.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

public class Producer_Routing {
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

//        DIRECT("direct") 定向
//        FANOUT("fanout") 扇形，发送消息到每一个与之绑定队列
//        TOPIC("topic") 通配符的方式
//        HEADERS("headers") 参数匹配
        String exchangeName = "test_direct";

//        5.创建交换机
        channel.exchangeDeclare(
                exchangeName,
                BuiltinExchangeType.DIRECT,
                true,
                false,
                false,
                null);

//        6.创建队列
        String queueName1 = "test_direct_queue1";
        String queueName2 = "test_direct_queue2";
        channel.queueDeclare(queueName1, true, false, false, null);
        channel.queueDeclare(queueName2, true, false, false, null);

//        7.绑定队列和交换机
        channel.queueBind(queueName1, exchangeName, "error");
        channel.queueBind(queueName2, exchangeName, "info");
        channel.queueBind(queueName2, exchangeName, "error");
        channel.queueBind(queueName2, exchangeName, "warning");

//        8.发送消息
        String body = "hello world";
        channel.basicPublish(
                exchangeName,
                "info",
                null,
                body.getBytes(StandardCharsets.UTF_8));

//        9.释放资源
        channel.close();
        connection.close();
    }
}
