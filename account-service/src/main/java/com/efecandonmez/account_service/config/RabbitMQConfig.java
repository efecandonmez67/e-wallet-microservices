package com.efecandonmez.account_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue refundQueue() {
        return new Queue("refund_queue", true);
    }

    @Bean
    public Queue billPaymentQueue() {
        return new Queue("bill.payment.queue", true);
    }
}