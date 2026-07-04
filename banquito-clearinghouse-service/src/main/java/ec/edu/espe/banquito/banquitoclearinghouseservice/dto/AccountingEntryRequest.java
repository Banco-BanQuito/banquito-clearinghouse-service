package ec.edu.espe.banquito.banquitoclearinghouseservice.dto;

import java.math.BigDecimal;

public class AccountingEntryRequest {
    private String accountCode;

    private BigDecimal amount;

    private String description;

    private String reference;

    public AccountingEntryRequest() {
        // Constructor por defecto intencionalmente vacío.
        // Motivo: frameworks de (de)serialización como Jackson requieren un constructor sin argumentos
        // para crear instancias antes de establecer propiedades vía setters/reflection.
    }

    // Constructor con todos los campos para conveniencia al crear instancias programáticamente.
    public AccountingEntryRequest(String accountCode, BigDecimal amount, String description, String reference) {
        this.accountCode = accountCode;
        this.amount = amount;
        this.description = description;
        this.reference = reference;
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
