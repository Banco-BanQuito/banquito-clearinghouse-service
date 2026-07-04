package ec.edu.espe.banquito.banquitoclearinghouseservice.dto;

import ec.edu.espe.banquito.banquitoclearinghouseservice.enums.FileStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompensationFileResponse(
        String id,
        UUID batchId,
        String fileName,
        Integer offUsRecords,
        BigDecimal totalAmount,
        FileStatus status,
        LocalDateTime generatedAt,
        String fileType,
        LocalDateTime periodFrom,
        LocalDateTime periodTo
) {
}
