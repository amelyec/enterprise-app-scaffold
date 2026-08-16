package co.tz.werelay.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enterpriseAppOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Enterprise App API")
                .version("v1")
                .description("Contract-first: this spec (served at /swagger-ui.html) is the source of truth for client integration."));
    }
}
