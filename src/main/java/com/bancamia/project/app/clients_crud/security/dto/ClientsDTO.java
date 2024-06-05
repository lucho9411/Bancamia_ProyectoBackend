package com.bancamia.project.app.clients_crud.security.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ClientsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Nullable
	@JsonProperty(required = false)
	private long id;
	@NotNull
	@NotBlank
	@NotEmpty
	@JsonProperty(required = true)
	private String identificationType;
	@NotNull
	@NotBlank
	@NotEmpty
	@JsonProperty(required = true)
	@Size(min = 1, max = 15)
	private String identificationNumber;
	@NotNull
	@NotBlank
	@NotEmpty
	@JsonProperty(required = true)
	@Size(min = 1, max = 100)
	private String names;
	@NotNull
	@NotBlank
	@NotEmpty
	@JsonProperty(required = true)
	@Size(min = 1, max = 100)
	private String lastNames;
	@Nullable
	@JsonProperty(required = false)
	private java.time.LocalDateTime creationDate;

}