package com.example.monitorconsumo.dto;

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
public class ConsumoDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String dispositivo;
	
	private Date data;
	
	private BigDecimal potencia;
	
	private BigDecimal temperatura;
	
	@Column(name = "kwh", precision = 20, scale = 8)
	private BigDecimal kwh;
	
	private Long intervaloDaLeitura;
}
