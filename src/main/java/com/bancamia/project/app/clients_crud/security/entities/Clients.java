package com.bancamia.project.app.clients_crud.security.entities;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.annotation.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Builder
@Table(name = "clients")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Clients implements Serializable, Persistable<Long>{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	@NotNull
	@Column(value = "identification_type")
	private String identificationType;
	@NotNull
	@Size(min = 1, max = 15)
	@Column(value = "identification_number")
	private String identificationNumber;
	@NotNull
	@Size(min = 1, max = 100)
	@Column(value = "names")
	private String names;
	@NotNull
	@Size(min = 1, max = 100)
	@Column(value = "last_names")
	private String lastNames;
	@Nullable
	@Column(value = "creation_date")
	private java.time.LocalDateTime creationDate;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public boolean isNew() {
		return id == 0;
	}
}