package ec.edu.espe.banquito.banquitoclearinghouseadapter.service;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.AccountingEntryRequest;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.provider.AccountingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountingService {
    private final AccountingProvider accountingProvider;

    @Value("${accounting.default.account.code:1.1.0.01}")
    private String defaultAccountCode;

    public AccountingService(AccountingProvider accountingProvider) {
        this.accountingProvider = accountingProvider;
    }

    public void registerOffUsAccountingEntry(
            UUID batchId,
            BigDecimal amount) {

        if (batchId == null) {
            throw new IllegalArgumentException("batchId no puede ser null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount no puede ser null");
        }

        AccountingEntryRequest request = new AccountingEntryRequest();

        // Usar el código de cuenta configurable (por defecto '1.1.0.01')
        request.setAccountCode(defaultAccountCode);

        request.setAmount(amount);

        request.setDescription("Compensacion bancaria OffUs");

        request.setReference(batchId.toString());

        accountingProvider.registerEntry(request);
    }
}
