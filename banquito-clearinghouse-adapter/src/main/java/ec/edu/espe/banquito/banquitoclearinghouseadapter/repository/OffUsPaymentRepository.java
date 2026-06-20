package ec.edu.espe.banquito.banquitoclearinghouseadapter.repository;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.model.OffUsPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OffUsPaymentRepository extends MongoRepository<OffUsPayment, String> {
    List<OffUsPayment> findByBatchId(UUID batchId);
    List<OffUsPayment> findByStatus(ec.edu.espe.banquito.banquitoclearinghouseadapter.enums.PaymentStatus status);
    Long countByBatchId(UUID batchId);
    List<OffUsPayment> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
