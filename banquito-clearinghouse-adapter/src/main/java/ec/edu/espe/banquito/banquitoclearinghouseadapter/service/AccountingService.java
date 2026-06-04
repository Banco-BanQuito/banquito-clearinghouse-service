package ec.edu.espe.banquito.banquitoclearinghouseadapter.service;

import ec.edu.espe.banquito.banquitoclearinghouseadapter.dto.AccountingEntryRequest;
import ec.edu.espe.banquito.banquitoclearinghouseadapter.provider.AccountingProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountingService {
    private final AccountingProvider accountingProvider;

    public AccountingService(AccountingProvider accountingProvider) {
        this.accountingProvider = accountingProvider;
    }
    public void registerOffUsAccountingEntry(
            UUID batchId,
            BigDecimal amount) {

        AccountingEntryRequest request =
                new AccountingEntryRequest();

        request.setAccountCode("1.1.0.01");

        request.setAmount(amount);

        request.setDescription(
                "Compensacion bancaria OffUs");

        request.setReference(
                batchId.toString());

        accountingProvider.registerEntry(request);
    }
}
