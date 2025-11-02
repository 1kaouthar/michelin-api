package com.example.michelin_api.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI michelinOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Michelin Restaurants API")
                        .description("API de gestion des restaurants et évaluations (Keycloak + MinIO)")
                        .version("1.0.0"));
    }
}

