package com.example.monitorconsumo.service;

import com.example.monitorconsumo.dto.TarifaDTO;
import com.example.monitorconsumo.model.Tarifa;
import com.example.monitorconsumo.repository.TarifaRepository;
import com.example.monitorconsumo.utils.ConversorEntidadeDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class TarifaService {
	
	private final TarifaRepository tarifaRepository;
	
	private final ConversorEntidadeDTO<Tarifa, TarifaDTO> conversorEntidadeDTO;
	
	public TarifaService(TarifaRepository tarifaRepository) {
		this.conversorEntidadeDTO = new ConversorEntidadeDTO<>(Tarifa.class, TarifaDTO.class);
		this.tarifaRepository = tarifaRepository;
	}
	
	public Tarifa salva(TarifaDTO tarifa) {
		
		log.info("Chegou: " + tarifa);
		
		return tarifaRepository.save(conversorEntidadeDTO.toEntity(tarifa));
	}
	
	public List<TarifaDTO> getTarifaList() {
		return conversorEntidadeDTO.toDTO(tarifaRepository.findAll());
	}
}