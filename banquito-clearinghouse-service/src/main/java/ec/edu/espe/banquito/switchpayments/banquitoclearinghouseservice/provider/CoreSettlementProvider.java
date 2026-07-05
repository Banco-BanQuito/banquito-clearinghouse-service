package ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.provider;

import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.dto.OffUsSettlementRequest;
import ec.edu.espe.banquito.switchpayments.banquitoclearinghouseservice.dto.OffUsSettlementResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CoreSettlementProvider {
    private final WebClient webClient;

    @Value("${core.service.settlement-url}")
    private String settlementUrl;

    public CoreSettlementProvider(WebClient webClient) {
        this.webClient = webClient;
    }

    public OffUsSettlementResponse registerSettlement(OffUsSettlementRequest request) {
        return webClient
                .post()
                .uri(settlementUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OffUsSettlementResponse.class)
                .block();
    }
}
