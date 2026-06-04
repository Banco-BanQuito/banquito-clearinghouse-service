package ec.edu.espe.banquito.banquitoclearinghouseadapter.dto;

import java.math.BigDecimal;

public class AccountingEntryRequest {
    private String accountCode;

    private BigDecimal amount;

    private String description;

    private String reference;

    public AccountingEntryRequest() {
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
