package ec.edu.espe.banquito.banquitoclearinghouseadapter.config;

import org.bson.UuidRepresentation;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    // OffUsPayment.batchId/transactionId son UUID; el driver de Mongo exige
    // especificar la representación explícitamente o falla al codificar.
    @Bean
    public MongoClientSettingsBuilderCustomizer uuidRepresentationCustomizer() {
        return builder -> builder.uuidRepresentation(UuidRepresentation.STANDARD);
    }
}
