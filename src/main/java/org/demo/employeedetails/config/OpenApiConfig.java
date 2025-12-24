package org.demo.employeedetails.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Employee Details API").version("1.0").description("Secure API to fetch employee details with role-based access"))
                .externalDocs(new ExternalDocumentation().description("Project Docs"));
    }
}

