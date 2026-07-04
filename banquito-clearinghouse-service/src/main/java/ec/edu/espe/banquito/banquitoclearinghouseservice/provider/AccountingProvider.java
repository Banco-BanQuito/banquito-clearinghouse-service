package ec.edu.espe.banquito.banquitoclearinghouseservice.provider;

import ec.edu.espe.banquito.banquitoclearinghouseservice.dto.AccountingEntryRequest;
import ec.edu.espe.banquito.banquitoclearinghouseservice.dto.AccountingEntryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AccountingProvider {
    private final WebClient webClient;

    @Value("${accounting.service.url}")
    private String accountingUrl;

    public AccountingProvider(WebClient webClient) {
        this.webClient = webClient;
    }

    public AccountingEntryResponse registerEntry(
            AccountingEntryRequest request) {

        return webClient
                .post()
                .uri(accountingUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AccountingEntryResponse.class)
                .block();
    }

}
