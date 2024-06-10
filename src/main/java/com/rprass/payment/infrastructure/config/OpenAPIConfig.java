package com.rprass.payment.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-Payments")
                        .version("v1")
                        .description("API provided for receiving payments")
                        .termsOfService("https://github.com/ronaldoprass/erudio")
                        .license(new License().name("Apache 2.0")
                                .url("https://github.com/ronaldoprass/erudio")));
    }
}
