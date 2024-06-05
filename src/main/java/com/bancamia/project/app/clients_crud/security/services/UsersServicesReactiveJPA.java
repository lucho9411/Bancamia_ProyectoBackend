package com.bancamia.project.app.clients_crud.security.services;

import org.springframework.stereotype.Service;
import com.bancamia.project.app.clients_crud.security.dto.LoginDTO;
import com.bancamia.project.app.clients_crud.security.dto.TokenDTO;
import com.bancamia.project.app.clients_crud.security.entities.Users;
import com.bancamia.project.app.clients_crud.security.exceptions.CustomException;
import com.bancamia.project.app.clients_crud.security.jwt.JwtProvider;
import com.bancamia.project.app.clients_crud.security.repositories.UsersReactiveRepository;
import reactor.core.publisher.Mono;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsersServicesReactiveJPA implements IUsersServices {
	
	@Autowired
	private UsersReactiveRepository usersRepo;
	private static Logger LOGGER = LoggerFactory.getLogger(UsersServicesReactiveJPA.class);

	@Override
	public Mono<TokenDTO> generateToken(JwtProvider jwtProvider, PasswordEncoder passwordEncoder, LoginDTO loginDTO) throws InterruptedException, ExecutionException{
		byte[] decodedBytesUser = Base64.getDecoder().decode(loginDTO.getEmail()),
				decodedBytesPassword = Base64.getDecoder().decode(loginDTO.getPassword());
		Mono<Users> userMono = usersRepo.findByEmail(new String(decodedBytesUser));
		if (userMono.toFuture().get() != null) {
			LOGGER.info("######### - Usuario encontrado - #########");
			return Mono.just(userMono
	                .filter(user -> passwordEncoder.matches(new String(decodedBytesPassword), user.getPassword()))
	                .map(user -> new TokenDTO(jwtProvider.generateToken(user, user.getId()))).toFuture().get());
		}
		else {
			LOGGER.warn("######### - Usuario no encontrado - #########");
			return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Usuario no encontrado", 404));
		}
		
	}

	@Override
	public Mono<Users> findByEmail(String email) {
		LOGGER.info("######### - Obteniendo usuario por el email - #########");
		return usersRepo.findByEmail(email);
	}

}