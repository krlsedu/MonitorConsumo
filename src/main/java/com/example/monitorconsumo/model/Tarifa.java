package com.example.monitorconsumo.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tarifa", schema = "monitor_consumo")
public class Tarifa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String companhia;
	
	private Date dataInicioVigencia;
	
	private Date dataFimVigencia;
	
	private BigDecimal valorKwh;
}
