package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.service;

import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.dto.OffUsPaymentMessage;
import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.enums.PaymentStatus;
import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.model.OffUsPayment;
import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.repository.OffUsPaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class OffUsConsumerService {
    private final OffUsPaymentRepository offUsPaymentRepository;

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
        payment.setCreatedAt(LocalDateTime.now(ZoneId.systemDefault()));
        offUsPaymentRepository.save(payment);
    }
}
