package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.dto;

public class AccountingEntryResponse {
    private String entryId;

    private String status;

    private String message;

    public AccountingEntryResponse() {
    }

    public AccountingEntryResponse(String entryId, String status, String message) {
        this.entryId = entryId;
        this.status = status;
        this.message = message;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
