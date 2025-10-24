package br.uenf.eacos.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI templateAPI() {

        Info info = new Info()
                .title("EACOS API")
                .version("1.0")
                .description("Essa API expõe métodos do projeto EACOS.");

        Components securityScheme = new Components()
                .addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("jwt"));

        Server server = new Server();
        server.setUrl("https://eacosbackend.loca.lt/");

        return new OpenAPI().servers(List.of(server)).info(info).components(securityScheme);
    }

}
