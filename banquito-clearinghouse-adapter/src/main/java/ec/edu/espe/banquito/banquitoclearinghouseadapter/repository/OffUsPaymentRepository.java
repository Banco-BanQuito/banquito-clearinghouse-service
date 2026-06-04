package ec.edu.espe.banquito.banquitoclearinghouseadapter.repository;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.OffUsPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface OffUsPaymentRepository extends MongoRepository<OffUsPayment, String> {
    List<OffUsPayment> findByBatchId(UUID batchId);
    Long countByBatchId(UUID batchId);
}
