package ec.edu.espe.banquito.banquitoclearinghouseservice.dto;

public class AccountingEntryResponse {
    private String entryId;

    private String status;

    private String message;

    public AccountingEntryResponse() {
        // Constructor por defecto intencionalmente vacío.
        // Motivo: frameworks de (de)serialización como Jackson requieren un constructor sin argumentos
        // para crear instancias antes de establecer propiedades vía setters/reflection.
    }

    // Constructor con todos los campos para conveniencia al crear instancias programáticamente.
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
