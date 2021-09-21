package com.example.monitorconsumo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TarifaDTO {
	private Long id;
	
	private String companhia;
	
	private Date dataInicioVigencia;
	
	private Date dataFimVigencia;
	
	private BigDecimal valorKwh;
}
