package ec.edu.espe.banquito.banquitoclearinghouseadapter.service;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.ClearingFileResponse;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.OffUsPaymentMessage;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.FileStatus;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.exception.BatchNotFoundException;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.CompensationFile;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.repository.CompensationFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClearingQueryServiceTest {

    @Mock
    private CompensationFileRepository compensationFileRepository;

    @Mock
    private OffUsConsumerService offUsConsumerService;

    @InjectMocks
    private ClearingQueryService clearingQueryService;

    @Test
    void findByBatchId_debeRetornarResponse_cuandoArchivoExiste() {
        UUID batchId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2026, 6, 12, 10, 0, 0);

        CompensationFile file = new CompensationFile();
        file.setBatchId(batchId);
        file.setFileName("clearing_20260612.txt");
        file.setFilePath("/files/clearing_20260612.txt");
        file.setOffUsRecords(10);
        file.setTotalAmount(new BigDecimal("5000.00"));
        file.setStatus(FileStatus.GENERATED);
        file.setGeneratedAt(now);

        when(compensationFileRepository.findByBatchId(batchId)).thenReturn(Optional.of(file));

        ClearingFileResponse response = clearingQueryService.findByBatchId(batchId);

        assertThat(response.getBatchId()).isEqualTo(batchId);
        assertThat(response.getFileName()).isEqualTo("clearing_20260612.txt");
        assertThat(response.getFilePath()).isEqualTo("/files/clearing_20260612.txt");
        assertThat(response.getOffUsRecords()).isEqualTo(10);
        assertThat(response.getTotalOffUsAmount()).isEqualByComparingTo(new BigDecimal("5000.00"));
        assertThat(response.getStatus()).isEqualTo("GENERATED");
        assertThat(response.getGeneratedAt()).isEqualTo(now);
    }

    @Test
    void findByBatchId_debeLanzarBatchNotFoundException_cuandoArchivoNoExiste() {
        UUID batchId = UUID.randomUUID();
        when(compensationFileRepository.findByBatchId(batchId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clearingQueryService.findByBatchId(batchId))
                .isInstanceOf(BatchNotFoundException.class)
                .hasMessageContaining(batchId.toString());
    }

    @Test
    void consume_debeDelegarAlOffUsConsumerService() {
        OffUsPaymentMessage message = new OffUsPaymentMessage();
        message.setBatchId(UUID.randomUUID());
        message.setTransactionId(UUID.randomUUID());

        clearingQueryService.consume(message);

        verify(offUsConsumerService).process(message);
    }
}
