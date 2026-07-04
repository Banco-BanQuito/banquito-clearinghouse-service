package ec.edu.espe.banquito.banquitoclearinghouseservice.service;

import ec.edu.espe.banquito.banquitoclearinghouseservice.dto.ClearingFileResponse;
import ec.edu.espe.banquito.banquitoclearinghouseservice.dto.OffUsPaymentMessage;
import ec.edu.espe.banquito.banquitoclearinghouseservice.exception.BatchNotFoundException;
import ec.edu.espe.banquito.banquitoclearinghouseservice.model.CompensationFile;
import ec.edu.espe.banquito.banquitoclearinghouseservice.repository.CompensationFileRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClearingQueryService {
    private final OffUsConsumerService offUsConsumerService;
    private final CompensationFileRepository compensationFileRepository;
    public ClearingQueryService(OffUsConsumerService offUsConsumerService, CompensationFileRepository compensationFileRepository) {
        this.offUsConsumerService = offUsConsumerService;
        this.compensationFileRepository = compensationFileRepository;
    }

    @RabbitListener(queues = "clearing-query-queue")
    public void consume(OffUsPaymentMessage message){
        offUsConsumerService.process(message);
    }

    public ClearingFileResponse findByBatchId(UUID batchId) {

        CompensationFile file = compensationFileRepository
                .findByBatchId(batchId)
                .orElseThrow(() ->
                        new BatchNotFoundException(
                                "No existe el lote: " + batchId));

        ClearingFileResponse response =
                new ClearingFileResponse();

        response.setBatchId(file.getBatchId());
        response.setFileName(file.getFileName());
        response.setFilePath(file.getFilePath());
        response.setOffUsRecords(file.getOffUsRecords());
        response.setTotalOffUsAmount(file.getTotalAmount());
        response.setStatus(file.getStatus().name());
        response.setGeneratedAt(file.getGeneratedAt());

        return response;
    }
}
