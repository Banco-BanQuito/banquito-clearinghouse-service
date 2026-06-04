package ec.edu.espe.banquito.banquitoclearinghouseadapter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.clearing.queue}")
    private String queueName;

    @Bean
    public Queue clearingOutboundQueue() {
        return new Queue(queueName, true);
    }

}
