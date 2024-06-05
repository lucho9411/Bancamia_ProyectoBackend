package com.bancamia.project.app.clients_crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@Configuration
@OpenAPIDefinition(info = @Info(
		title = "Documentación del CRUD de Clientes Bancamía",
		version = "1.0",
		description = "Documentación del CRUD de Clientes Bancamía Con OpenAPI Swagger"
))
public class ClientsCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientsCrudApplication.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ClientsCrudApplication.class);
	}

}
