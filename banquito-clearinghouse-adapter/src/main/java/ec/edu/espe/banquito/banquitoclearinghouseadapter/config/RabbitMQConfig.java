package ec.edu.espe.banquito.banquitoclearinghouseadapter.config;

import org.springframework.amqp.support.converter.JacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    // RF-03: el routing-service publica JSON; sin este converter, Spring AMQP
    // intentaría deserializar con SimpleMessageConverter (Java serialization) y fallaría.
    @Bean
    public MessageConverter jsonMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }
}
