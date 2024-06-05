package com.bancamia.project.app.clients_crud.security.services;

import java.io.ByteArrayInputStream;
import java.util.List;
import com.bancamia.project.app.clients_crud.security.dto.AdvancedFilterDTO;
import com.bancamia.project.app.clients_crud.security.dto.ClientsDTO;
import com.bancamia.project.app.clients_crud.security.entities.Clients;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientsServices {

	Flux<ClientsDTO> list();
	Flux<ClientsDTO> search(String alias);
	Flux<ClientsDTO> advancedSearch(AdvancedFilterDTO advancedSearch);
	Mono<Clients> created(ClientsDTO clientDTO);
	Mono<Clients> modified(ClientsDTO clientDTO);
	Mono<Void> delete(long id);
	Mono<ByteArrayInputStream> export(List<ClientsDTO> clients);
}
