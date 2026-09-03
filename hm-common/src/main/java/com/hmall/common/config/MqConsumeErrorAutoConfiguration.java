package com.hmall.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "spring.rabbitmq.listener.simple.retry.enabled", havingValue = "true")
public class MqConsumeErrorAutoConfiguration {

    @Value("${spring.application.name}")
    private String serviceName;

    @Bean
    public DirectExchange errorDirectExchange() {
        return new DirectExchange("error.direct", true, false);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(serviceName + ".error.queue", true, false, false);
    }

    @Bean
    public Binding errorBinding() {
        return BindingBuilder
                .bind(errorQueue())
                .to(errorDirectExchange())
                .with(serviceName);
    }

    @Bean
    public RepublishMessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", serviceName);
    }
}