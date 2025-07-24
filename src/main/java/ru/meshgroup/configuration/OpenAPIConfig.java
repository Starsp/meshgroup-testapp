package ru.meshgroup.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Тестовое задание");

        Contact contact = new Contact();
        contact.setEmail("vassilev@live.ru");
        contact.setName("gvassilev");

        Info info = new Info()
                .title("API")
                .version("1.0")
                .contact(contact)
                .description("API тестового задания");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

}
