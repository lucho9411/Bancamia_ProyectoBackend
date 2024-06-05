package com.bancamia.project.app.clients_crud.security.services;

import java.util.concurrent.ExecutionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bancamia.project.app.clients_crud.security.dto.LoginDTO;
import com.bancamia.project.app.clients_crud.security.dto.TokenDTO;
import com.bancamia.project.app.clients_crud.security.entities.Users;
import com.bancamia.project.app.clients_crud.security.jwt.JwtProvider;

import reactor.core.publisher.Mono;

public interface IUsersServices {

	Mono<TokenDTO> generateToken(JwtProvider jwtProvider, PasswordEncoder passwordEncoder, LoginDTO login) throws InterruptedException, ExecutionException;
	Mono<Users> findByEmail(String email);
}
