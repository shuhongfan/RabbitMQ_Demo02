package com.shf.rabbitmqproducer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Objects;

@Configuration
public class RabbitMQConfig {

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "boot_topic_exchange";
    public static final String CONFIRM_EXCHANGE_NAME = "test_exchange_confirm";
    public static final String TTL_EXCHANGE_NAME = "test_exchange_ttl";
    public static final String DLX_EXCHANGE_NAME = "test_exchange_dlx";
    public static final String DLX_EXCHANGE = "exchange_dlx";
    public static final String ORDER_EXCHANGE_NAME = "order_exchange";
    public static final String ORDER_EXCHANGE_DLX_NAME = "order_exchange_dlx";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "boot_queue";
    public static final String CONFIRM_QUEUE_NAME = "test_queue_confirm";
    public static final String TTL_QUEUE_NAME = "test_queue_ttl";
    public static final String DLX_QUEUE_NAME = "test_queue_dlx";
    public static final String DLX_QUEUE = "queue_dlx";
    public static final String ORDER_QUEUE_NAME = "order_queue";
    public static final String ORDER_QUEUE_DLX_NAME = "order_queue_dlx";


    /**
     * 1.交换机
     *
     * @return
     */
    @Bean("bootExchange")
    public Exchange bootExchange() {
        return ExchangeBuilder
                .topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }


    /**
     * 2.Queue队列
     *
     * @return
     */
    @Bean("bootQueue")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /**
     * 3.队列和交互绑定关系 Binding
     * 1.知道那个队列
     * 2.知道那个交换机
     * 3.知道那个路由key
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindQueueExchange(
            @Qualifier("bootQueue") Queue queue,
            @Qualifier("bootExchange") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("boot.#")
                .noargs();
    }


    /**
     * 消息可靠投递 队列
     *
     * @return
     */
    @Bean("test_queue_confirm")
    public Queue testQueueConfirm() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    /**
     * 消息可靠投递 交换机
     *
     * @return
     */
    @Bean("test_exchange_confirm")
    public Exchange testExchangeConfirm() {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 消息可靠投递 绑定关系
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindConfirmQueueExchange(
            @Qualifier("test_queue_confirm") Queue queue,
            @Qualifier("test_exchange_confirm") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("confirm")
                .noargs();
    }

    /**
     * ttl队列
     *
     * @return
     */
    @Bean("test_queue_ttl")
    public Queue testQueueTtl() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);
        return QueueBuilder
                .durable(TTL_QUEUE_NAME)
                .withArguments(args)
                .build();
    }

    /**
     * ttl交换机
     *
     * @return
     */
    @Bean("test_exchange_ttl")
    public Exchange testExchangeTtl() {
        return ExchangeBuilder
                .topicExchange(TTL_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * ttl绑定关系
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindTtlQueueExchange(
            @Qualifier("test_queue_ttl") Queue queue,
            @Qualifier("test_exchange_ttl") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("ttl.#")
                .noargs();
    }

    /**
     * dlx队列
     *
     * @return
     */
    @Bean("test_queue_dlx")
    public Queue testQueueDlx() {
        HashMap<String, Object> args = new HashMap<>();
//        死信交换机名称
        args.put("x-dead-letter-exchange", "exchange_dlx");
//        死信交换机路由key
        args.put("x-dead-letter-routing-key", "dlx.#");

//        设置队列的过期时间ttl
        args.put("x-message-ttl", 10000);

//        设置队列的长度限制 max-length
        args.put("x-max-length", 10);

        return QueueBuilder.durable(DLX_QUEUE_NAME).withArguments(args).build();
    }

    /**
     * dlx交换机
     *
     * @return
     */
    @Bean("test_exchange_dlx")
    public Exchange testExchangeDlx() {
        return ExchangeBuilder
                .topicExchange(DLX_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * dlx绑定关系
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindtestDlxQueueExchange(
            @Qualifier("test_queue_dlx") Queue queue,
            @Qualifier("test_exchange_dlx") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("test.dlx.#")
                .noargs();
    }

    /**
     * dlx队列
     *
     * @return
     */
    @Bean("queue_dlx")
    public Queue QueueDlx() {
        return QueueBuilder
                .durable(DLX_QUEUE)
                .build();
    }

    /**
     * dlx交换机
     *
     * @return
     */
    @Bean("exchange_dlx")
    public Exchange ExchangeDlx() {
        return ExchangeBuilder
                .topicExchange(DLX_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * dlx绑定关系
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindDlxQueueExchange(
            @Qualifier("queue_dlx") Queue queue,
            @Qualifier("exchange_dlx") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("dlx.#")
                .noargs();
    }

//================================================================================================================
    /**
     * 延时队列 正常队列
     * @return
     */
    @Bean("order_queue")
    public Queue orderDlx() {
        HashMap<String, Object> args = new HashMap<>();
//        死信交换机名称
        args.put("x-dead-letter-exchange", "order_exchange_dlx");
//        死信交换机路由key
        args.put("x-dead-letter-routing-key", "dlx.order.cancel");

//        设置队列的过期时间ttl
        args.put("x-message-ttl", 10000);

        return QueueBuilder
                .durable(ORDER_QUEUE_NAME)
                .withArguments(args)
                .build();
    }

    /**
     * 延时队列 正常交换机
     * @return
     */
    @Bean("order_exchange")
    public Exchange orderExchange() {
        return ExchangeBuilder
                .topicExchange(ORDER_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /**
     * 延时队列 正常绑定关系
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindOrderDlxQueueExchange(
            @Qualifier("order_queue") Queue queue,
            @Qualifier("order_exchange") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("order.#")
                .noargs();
    }

    /**
     * 延时队列 死信队列
     * @return
     */
    @Bean("order_queue_dlx")
    public Queue orderQueueDlx() {
        return QueueBuilder
                .durable(ORDER_QUEUE_DLX_NAME)
                .build();
    }

    /**
     * 延时队列 死信交换机
     * @return
     */
    @Bean("order_exchange_dlx")
    public Exchange orderExchangeDlx() {
        return ExchangeBuilder
                .topicExchange(ORDER_EXCHANGE_DLX_NAME)
                .durable(true)
                .build();
    }

    /**
     * 延时队列 死信绑定关系
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindOrderQueueDlxExchange(
            @Qualifier("order_queue_dlx") Queue queue,
            @Qualifier("order_exchange_dlx") Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("dlx.order.#")
                .noargs();
    }

}
