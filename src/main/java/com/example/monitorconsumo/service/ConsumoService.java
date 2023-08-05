package com.example.monitorconsumo.service;

import com.example.monitorconsumo.model.Consumo;
import com.example.monitorconsumo.repository.ConsumoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
@Log4j2
public class ConsumoService {

    private final ConsumoRepository consumoRepository;

    public ConsumoService(ConsumoRepository consumoRepository) {
        this.consumoRepository = consumoRepository;
    }

    public Consumo salva(String dados) throws IllegalArgumentException {
        try {
            log.info("Chegou: " + dados);

            if (dados != null) {
                var consumo = new Consumo();
                String[] dadosS = dados.split(",");
                consumo.setPotencia(new BigDecimal(dadosS[0]));

                BigDecimal potencia = consumo.getPotencia().subtract(new BigDecimal(600));

                if (potencia.compareTo(BigDecimal.ZERO) < 0) {
                    potencia = BigDecimal.ZERO;
                }

                long intervaloDaLeitura = Long.parseLong(dadosS[5]);

                BigDecimal potenciaKw = potencia.divide(new BigDecimal(1000), 10, RoundingMode.HALF_UP);
                BigDecimal kwh = potenciaKw.divide(
                        new BigDecimal(3600).divide(
                                new BigDecimal(intervaloDaLeitura).divide(
                                        new BigDecimal(1000)
                                        , 10, RoundingMode.HALF_UP)
                                , 10, RoundingMode.HALF_UP)
                        , 10, RoundingMode.HALF_UP);

                consumo.setPotencia(potencia);
                consumo.setTemperatura(new BigDecimal(dadosS[1]));
                consumo.setKwh(kwh);
                consumo.setDispositivo(dadosS[3]);
                //consumo.setData(new Date(Long.parseLong(dadosS[4])));
                consumo.setData(new Date());
                consumo.setIntervaloDaLeitura(intervaloDaLeitura);
                return consumoRepository.save(consumo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error");
        }
        throw new IllegalArgumentException("ops");
    }
}