package com.shf.rabbitmqproducer.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

public class Producer_HelloWorld {

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

        String body = "hello rabbitMq...";
//
        /**
         * 发布消息。发布到不存在的交换器会导致通道级协议异常，从而关闭通道。如果资源驱动的警报 生效， Channel#basicPublish的调用最终将被阻塞。
         * 参形：
         * exchange - 将消息发布到的交换
         * routingKey – 路由键
         * props - 消息的其他属性 - 路由标头等
         * body - 消息正文
         * 抛出：
         * IOException – 如果遇到错误
         * 请参阅：
         * AMQP.Basic.Publish , 资源驱动的警报
         */
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(
                    "",
                    "hello_world",
                    null,
                    body.getBytes());
        }


        channel.close();
        connection.close();
    }
}
