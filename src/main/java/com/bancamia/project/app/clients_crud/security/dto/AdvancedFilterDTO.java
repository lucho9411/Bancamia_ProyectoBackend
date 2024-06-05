package com.bancamia.project.app.clients_crud.security.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AdvancedFilterDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Nullable
	@JsonProperty(required = false)
	private String identificationType;
	@Nullable
	@JsonProperty(required = false)
	private String names;
	@Nullable
	@JsonProperty(required = false)
	private String lastNames;
	@Nullable
	@JsonProperty(required = false)
	private java.time.LocalDateTime startDate;
	@Nullable
	@JsonProperty(required = false)
	private java.time.LocalDateTime endDate;

}