package com.example.monitorconsumo.service;

import com.example.monitorconsumo.model.Consumo;
import com.example.monitorconsumo.repository.ConsumoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Log4j2
public class ConsumoService {
	
	private final ConsumoRepository consumoRepository;
	
	public ConsumoService(ConsumoRepository consumoRepository) {
		this.consumoRepository = consumoRepository;
	}
	
	public void salva(String dados) {
		
		log.info("Chegou: " + dados);
		
		if (dados != null) {
			var consumo = new Consumo();
			String[] dadosS = dados.split(",");
			consumo.setPotencia(new BigDecimal(dadosS[0]));
			consumo.setTemperatura(new BigDecimal(dadosS[1]));
			consumo.setKwh(new BigDecimal(dadosS[2]));
			consumo.setDispositivo(dadosS[3]);
			consumo.setData(new Date(Long.parseLong(dadosS[4])));
			consumo.setIntervaloDaLeitura(Long.parseLong(dadosS[5]));
			consumoRepository.save(consumo);
		}
	}
}