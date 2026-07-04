package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.config;

import org.springframework.amqp.support.converter.JacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.clearing.queue}")
    private String queueName;

    @Value("${rabbitmq.clearing.exchange:clearing.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.clearing.routing-key:clearing.outbound}")
    private String routingKey;

    @Bean
    public Queue clearingOutboundQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue clearingQueryQueue() {
        return new Queue("clearing-query-queue", true);
    }

    @Bean
    public DirectExchange clearingExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Binding clearingOutboundBinding(Queue clearingOutboundQueue, DirectExchange clearingExchange) {
        return BindingBuilder.bind(clearingOutboundQueue).to(clearingExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }
}
