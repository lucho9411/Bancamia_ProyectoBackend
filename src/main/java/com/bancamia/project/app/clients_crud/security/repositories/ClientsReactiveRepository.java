package com.bancamia.project.app.clients_crud.security.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import com.bancamia.project.app.clients_crud.security.entities.Clients;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClientsReactiveRepository extends ReactiveCrudRepository<Clients, Long> {
	
	Mono<Clients> findByIdentificationNumber(String identification);
	Flux<Clients> findByIdentificationNumberContaining(String identification);

}
