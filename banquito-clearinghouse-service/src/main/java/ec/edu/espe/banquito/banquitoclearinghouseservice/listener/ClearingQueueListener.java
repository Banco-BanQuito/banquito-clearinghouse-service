package ec.edu.espe.banquito.banquitoclearinghouseservice.listener;

import ec.edu.espe.banquito.banquitoclearinghouseservice.dto.OffUsPaymentMessage;
import ec.edu.espe.banquito.banquitoclearinghouseservice.service.OffUsConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClearingQueueListener {
    private final OffUsConsumerService offUsConsumerService;

    public ClearingQueueListener(OffUsConsumerService offUsConsumerService) {
        this.offUsConsumerService = offUsConsumerService;
    }

    @RabbitListener(queues = "${rabbitmq.clearing.queue}")
    public void consume(OffUsPaymentMessage message){
        offUsConsumerService.process(message);
    }
}
