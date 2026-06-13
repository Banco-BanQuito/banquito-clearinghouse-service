package ec.edu.espe.banquito.banquitoclearinghouseadapter.exception;

public class AccountingIntegrationException extends RuntimeException {

    public AccountingIntegrationException() {
        super();
    }

    public AccountingIntegrationException(String message) {
        super(message);
    }

    public AccountingIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountingIntegrationException(Throwable cause) {
        super(cause);
    }
}
