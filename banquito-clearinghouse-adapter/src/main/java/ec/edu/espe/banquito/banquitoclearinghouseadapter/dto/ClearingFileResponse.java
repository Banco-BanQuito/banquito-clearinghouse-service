package ec.edu.espe.banquito.banquitoclearinghouseadapter.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ClearingFileResponse {
    private UUID batchId;

    private String fileName;

    private String filePath;

    private Integer offUsRecords;

    private BigDecimal totalOffUsAmount;

    private String status;

    private LocalDateTime generatedAt;

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getOffUsRecords() {
        return offUsRecords;
    }

    public void setOffUsRecords(Integer offUsRecords) {
        this.offUsRecords = offUsRecords;
    }

    public BigDecimal getTotalOffUsAmount() {
        return totalOffUsAmount;
    }

    public void setTotalOffUsAmount(BigDecimal totalOffUsAmount) {
        this.totalOffUsAmount = totalOffUsAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
