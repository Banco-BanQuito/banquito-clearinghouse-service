package ec.edu.espe.banquito.banquitoclearinghouseservice.mapper;

import ec.edu.espe.banquito.banquitoclearinghouseservice.dto.CompensationFileResponse;
import ec.edu.espe.banquito.banquitoclearinghouseservice.model.CompensationFile;

public class CompensationFileMapper {

    private CompensationFileMapper() {
    }

    public static CompensationFileResponse toResponse(CompensationFile file) {
        return new CompensationFileResponse(
                file.getId(),
                file.getBatchId(),
                file.getFileName(),
                file.getOffUsRecords(),
                file.getTotalAmount(),
                file.getStatus(),
                file.getGeneratedAt(),
                file.getFileType(),
                file.getPeriodFrom(),
                file.getPeriodTo()
        );
    }
}
