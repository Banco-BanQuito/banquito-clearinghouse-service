package ec.edu.espe.banquito.banquitoclearinghouseadapter.service;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.OffUsPaymentMessage;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.PaymentStatus;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.OffUsPayment;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.repository.OffUsPaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OffUsConsumerServiceTest {

    @Mock
    private OffUsPaymentRepository offUsPaymentRepository;

    @InjectMocks
    private OffUsConsumerService offUsConsumerService;

    @Test
    void process_debeSalvarPago_conEstadoRECEIVED() {
        UUID batchId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        OffUsPaymentMessage message = new OffUsPaymentMessage();
        message.setBatchId(batchId);
        message.setTransactionId(transactionId);
        message.setRoutingCode("001");
        message.setOriginAccount("0001234567");
        message.setDestinationAccount("0009876543");
        message.setAmount(new BigDecimal("100.00"));
        message.setCurrency("USD");
        message.setValueDate(LocalDate.of(2026, 6, 12));

        offUsConsumerService.process(message);

        ArgumentCaptor<OffUsPayment> captor = ArgumentCaptor.forClass(OffUsPayment.class);
        verify(offUsPaymentRepository).save(captor.capture());
        OffUsPayment saved = captor.getValue();

        assertThat(saved.getBatchId()).isEqualTo(batchId);
        assertThat(saved.getTransactionId()).isEqualTo(transactionId);
        assertThat(saved.getRoutingCode()).isEqualTo("001");
        assertThat(saved.getOriginAccount()).isEqualTo("0001234567");
        assertThat(saved.getDestinationAccount()).isEqualTo("0009876543");
        assertThat(saved.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(saved.getCurrency()).isEqualTo("USD");
        assertThat(saved.getStatus()).isEqualTo(PaymentStatus.RECEIVED);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void process_debeAsignarFechaCreacion_siempreQueSeInvoque() {
        OffUsPaymentMessage message = new OffUsPaymentMessage();
        message.setBatchId(UUID.randomUUID());
        message.setTransactionId(UUID.randomUUID());
        message.setAmount(new BigDecimal("50.00"));
        message.setCurrency("USD");

        offUsConsumerService.process(message);

        ArgumentCaptor<OffUsPayment> captor = ArgumentCaptor.forClass(OffUsPayment.class);
        verify(offUsPaymentRepository).save(captor.capture());
        assertThat(captor.getValue().getCreatedAt()).isNotNull();
    }
}
