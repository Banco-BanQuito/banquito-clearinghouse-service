package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.service;

import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.dto.OffUsSettlementRequest;
import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.provider.CoreSettlementProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CoreSettlementService {
    private final CoreSettlementProvider coreSettlementProvider;

    public CoreSettlementService(CoreSettlementProvider coreSettlementProvider) {
        this.coreSettlementProvider = coreSettlementProvider;
    }

    public void registerOffUsSettlement(UUID batchId, BigDecimal amount) {
        if (batchId == null) {
            throw new IllegalArgumentException("batchId no puede ser null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount no puede ser null");
        }

        OffUsSettlementRequest request = new OffUsSettlementRequest();
        request.setBatchId(batchId.toString());
        request.setAmount(amount);
        request.setTransactionUuid(UUID.randomUUID().toString());

        coreSettlementProvider.registerSettlement(request);
    }
}
