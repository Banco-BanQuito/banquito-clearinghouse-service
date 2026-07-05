package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.dto;

import java.math.BigDecimal;

public class OffUsSettlementRequest {

    private String batchId;
    private BigDecimal amount;
    private String transactionUuid;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionUuid() {
        return transactionUuid;
    }

    public void setTransactionUuid(String transactionUuid) {
        this.transactionUuid = transactionUuid;
    }
}
