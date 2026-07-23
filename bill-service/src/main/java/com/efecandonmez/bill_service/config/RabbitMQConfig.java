package com.efecandonmez.bill_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "wallet.exchange";
    public static final String BILL_PAYMENT_ROUTING_KEY = "bill.payment.routing.key";
    public static final String BILL_PAYMENT_QUEUE = "bill.payment.queue";
    public static final String BILL_ROLLBACK_QUEUE = "bill.rollback.queue";

    @Bean
    public Queue billPaymentQueue() { return new Queue(BILL_PAYMENT_QUEUE, true); }

    @Bean
    public TopicExchange exchange() { return new TopicExchange(EXCHANGE_NAME); }

    @Bean
    public Binding binding(Queue billPaymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(billPaymentQueue).to(exchange).with(BILL_PAYMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue billRollbackQueue() {
        return new Queue(BILL_ROLLBACK_QUEUE, true);
    }
}