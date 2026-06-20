package ec.edu.espe.banquito.banquitoclearinghouseadapter.repository;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.CompensationFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompensationFileRepository extends MongoRepository<CompensationFile,String> {
    Optional<CompensationFile> findByBatchId(UUID batchId);
    List<CompensationFile> findAllByFileTypeAndPeriodFrom(String fileType, LocalDateTime periodFrom);
}
