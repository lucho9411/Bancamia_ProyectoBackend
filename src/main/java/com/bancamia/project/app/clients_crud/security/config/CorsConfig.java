package com.bancamia.project.app.clients_crud.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import com.bancamia.project.app.clients_crud.security.constants.HttpHeaders;
import com.bancamia.project.app.clients_crud.security.constants.HttpMethods;

@Configuration
@EnableWebFlux
public class CorsConfig implements WebFluxConfigurer {
	
	 @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedMethods(HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT, HttpMethods.DELETE)
	                .allowedOriginPatterns("*")
	                .allowedHeaders(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT,
			                		HttpHeaders.AUTHORIZATION, HttpHeaders.COOKIE,
			                		HttpHeaders.ACCESS_CONTROL_CREDENTIALS,
			                		HttpHeaders.ACCESS_CONTROL_ORIGIN)
	    	        .allowedOrigins("http://localhost:4200")
	    	        .maxAge(3600)
	                .allowCredentials(true);
	    }
}