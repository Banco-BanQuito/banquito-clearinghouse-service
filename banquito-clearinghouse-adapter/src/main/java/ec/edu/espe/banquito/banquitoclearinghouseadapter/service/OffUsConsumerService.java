package ec.edu.espe.banquito.banquitoclearinghouseadapter.service;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.OffUsPaymentMessage;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.PaymentStatus;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.OffUsPayment;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.repository.OffUsPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OffUsConsumerService {
    private OffUsPaymentRepository offUsPaymentRepository;

    public OffUsConsumerService(OffUsPaymentRepository offUsPaymentRepository) {
        this.offUsPaymentRepository = offUsPaymentRepository;
    }

    public void process(OffUsPaymentMessage message){
        OffUsPayment payment= new OffUsPayment();
        payment.setBatchId(message.getBatchId());
        payment.setTransactionId(message.getTransactionId());
        payment.setRoutingCode(message.getRoutingCode());
        payment.setOriginAccount(message.getOriginAccount());
        payment.setDestinationAccount(message.getDestinationAccount());
        payment.setAmount(message.getAmount());
        payment.setCurrency(message.getCurrency());
        payment.setValueDate(message.getValueDate());
        payment.setStatus(PaymentStatus.RECEIVED);
        payment.setCreatedAt(LocalDateTime.now());
        offUsPaymentRepository.save(payment);
    }
}
