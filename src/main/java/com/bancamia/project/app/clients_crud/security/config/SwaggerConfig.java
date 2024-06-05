package com.bancamia.project.app.clients_crud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
					.info(new Info()
								.title("Documentación del CRUD de Clientes Bancamía")
								.version("1.0")
								.description("Documentación del CRUD de Clientes Bancamía Con OpenAPI Swagger"));
	}

}
