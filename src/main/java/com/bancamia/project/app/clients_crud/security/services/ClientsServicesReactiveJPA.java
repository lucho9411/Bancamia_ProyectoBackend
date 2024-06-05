package com.bancamia.project.app.clients_crud.security.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.bancamia.project.app.clients_crud.security.dto.AdvancedFilterDTO;
import com.bancamia.project.app.clients_crud.security.dto.ClientsDTO;
import com.bancamia.project.app.clients_crud.security.entities.Clients;
import com.bancamia.project.app.clients_crud.security.exceptions.CustomException;
import com.bancamia.project.app.clients_crud.security.repositories.ClientsReactiveRepository;
import com.bancamia.project.app.clients_crud.security.utilities.ByteArrayInOutStream;
import com.bancamia.project.app.clients_crud.security.utilities.Utility;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ClientsServicesReactiveJPA implements IClientsServices {
	
	@Autowired
	private ClientsReactiveRepository clientsRepo;
	@Autowired
    private ModelMapper modelMapper;
	private static Logger LOGGER = LoggerFactory.getLogger(ClientsServicesReactiveJPA.class);
	
	// ---------------- CRUD Functions ------------------------\\

	@Override
	public Flux<ClientsDTO> list() {
		LOGGER.info("######### - Listando todos los clientes - #########");
		return clientsRepo.findAll()
			.groupBy(client -> client.getId())
			.flatMap(group ->
            	group.collectList()
                    .map(clients -> {
                    	LOGGER.info("######### - Convirtiendo las ClientsEntity en ClientsDTO - #########");
                    	return mapEntityToDto(clients.get(0));
                    })
            )
			.delayElements(Duration.ofMillis(100))
			.subscribeOn(Schedulers.parallel());
	}
	
	@Override
	public Flux<ClientsDTO> search(String identification) {
		LOGGER.info("######### - Buscando y filtrando clientes por su número de identificación - #########");
		return clientsRepo.findByIdentificationNumberContaining(identification)
				.groupBy(client -> client.getId())
				.flatMap(group ->
	            	group.collectList()
	                    .map(clients -> {
	                    	LOGGER.info("######### - Convirtiendo las ClientsEntity en ClientsDTO - #########");
	                    	return mapEntityToDto(clients.get(0));
	                    })
	            )
				.delayElements(Duration.ofMillis(100))
				.subscribeOn(Schedulers.parallel());
	}
	
	@Override
	public Flux<ClientsDTO> advancedSearch(AdvancedFilterDTO advancedSearchDTO) {
		LOGGER.info("######### - Buscando y filtrando clientes por los filtros avanzados - #########");
		List<Clients> clients = new ArrayList<Clients>();
		List<ClientsDTO> clientsDTO = new ArrayList<ClientsDTO>();
		try {
			clients = clientsRepo.findAll()
					.publishOn(Schedulers.boundedElastic())
					.delayElements(Duration.ofMillis(100))
					.subscribeOn(Schedulers.parallel())
					.collectList()
					.toFuture().get();
			LOGGER.info("######### - Se obtuvieron todo el listado de clientes y se inicia su recorrido - #########");
			for(Clients client: clients) {
				ClientsDTO clientDTO = advancedSearchParams(advancedSearchDTO, client);
				if (clientDTO != null) {
					LOGGER.info("######### - Se llena la lista de ClientsDTO - #########");
					clientsDTO.add(clientDTO);
				}
			}
			LOGGER.info("######### - Se retorna el Flux de ClientsDTO - #########");
			return Flux.fromIterable(clientsDTO)
					.delayElements(Duration.ofMillis(300))
					.subscribeOn(Schedulers.parallel());
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("######### - Error - #########");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return Flux.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el sistema", 500));
		}
	}
	
	@Override
	public Mono<Clients> created(ClientsDTO clientDTO) {
		LOGGER.info("######### - Se valida si el cliente con el número de identificación " + clientDTO.getIdentificationNumber() + " existe - #########");
		if (findByEmail(clientDTO.getIdentificationNumber()) != null) {
			LOGGER.warn("######### - No se puede crear el cliente - #########");
			LOGGER.warn("######### - Número de identificación de cliente ya se encuentra registrado - #########");
			return Mono.error(new CustomException(HttpStatus.CONFLICT, "Número de identificación de cliente ya se encuentra registrado", 409));
		}
		else {
			LOGGER.info("######### - Se inicia la creaión del cliente - #########");
			Clients client = mapDtoToEntity(clientDTO);
			client.setCreationDate(Utility.generateCurrentDate());
			return clientsRepo.save(client);
		}
	}
	
	@Override
	public Mono<Clients> modified(ClientsDTO clientDTO) {
		LOGGER.info("######### - Se valida si el cliente con el número de identificación " + clientDTO.getIdentificationNumber() + " existe - #########");
		if (findByEmail(clientDTO.getIdentificationNumber()) == null) {
			LOGGER.warn("######### - No se puede modificar el cliente - #########");
			LOGGER.warn("######### - Número de identificación de cliente no se encuentra registrado - #########");
			return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Número de identificación de cliente no se encuentra registrado", 404));
		}
		else {
			LOGGER.info("######### - Se inicia la modificación del cliente - #########");
			return clientsRepo.save(mapDtoToEntity(clientDTO));
		}
	}
	
	@Override
	public Mono<Void> delete(long id) {
		LOGGER.info("######### - Se inicia la eliminación del cliente con id=" + id + " - #########");
		return clientsRepo.deleteById(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Mono<ByteArrayInputStream> export(List<ClientsDTO> clients) {
		String[] columns = {"identificationType", "identificationNumber", "names", "lastNames", "creationDate"};
		return Mono.fromCallable(() -> {
            try {
                ByteArrayInOutStream stream = new ByteArrayInOutStream();
                OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
                CSVWriter writer = new CSVWriter(streamWriter);

                ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
                mappingStrategy.setType(ClientsDTO.class);
                mappingStrategy.setColumnMapping(columns);
                writer.writeNext(columns);

                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .withMappingStrategy(mappingStrategy)
                        .withSeparator(',')
                        .build();

                beanToCsv.write(clients);
                streamWriter.flush();
                return stream.getInputStream();
            }
            catch (CsvException | IOException e) {
                throw new RuntimeException(e);
            }

        }).subscribeOn(Schedulers.boundedElastic());
	}
	
	
	
	// ---------------- Other Functions ------------------------\\
	
	
	public Clients findByEmail(String identification) {
		try {
			LOGGER.info("######### - Se obtiene cliente con número de identificación=" + identification + " - #########");
			return clientsRepo.findByIdentificationNumber(identification).toFuture().get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("######### - Error - #########");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public ClientsDTO mapEntityToDto(Clients client) {
		LOGGER.info("######### - Se convierte el Entity Client en Dto con Mapper - #########");
		return this.modelMapper.map(client, ClientsDTO.class);
	}
	
	public Clients mapDtoToEntity(ClientsDTO clientDto) {
		LOGGER.info("######### - Se convierte el DTO Client en Entity con Mapper - #########");
		return this.modelMapper.map(clientDto, Clients.class);
	}
	
	public ClientsDTO advancedSearchParams(AdvancedFilterDTO advancedFilterDTO,  Clients client) {
		LOGGER.info("######### - Se inician los filtros avanzados - #########");
		boolean flag1 = advancedFilterDTO.getNames()!=null&&client.getNames().contains(advancedFilterDTO.getNames());
		boolean flag2 = advancedFilterDTO.getIdentificationType()!=null&&client.getIdentificationType().contains(advancedFilterDTO.getIdentificationType());
		boolean flag3 = advancedFilterDTO.getLastNames()!=null&&client.getLastNames().contains(advancedFilterDTO.getLastNames());
		boolean flag4 = advancedFilterDTO.getStartDate()!=null&&advancedFilterDTO.getEndDate()!=null&&
				Duration.between(advancedFilterDTO.getStartDate(), client.getCreationDate()).toDays() >= 0 &&
				Duration.between(client.getCreationDate(), advancedFilterDTO.getEndDate()).toDays() >= 0;
				LOGGER.info("######### - El resultado de los filtros avanzados es=" + (flag1 || flag2 || flag3 || flag4) + " - #########");
		return flag1 || flag2 || flag3 || flag4?mapEntityToDto(client):null;
	}
	
}