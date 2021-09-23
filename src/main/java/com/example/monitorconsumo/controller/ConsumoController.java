package com.example.monitorconsumo.controller;

import com.example.monitorconsumo.service.ConsumoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class ConsumoController {
	
	private final ConsumoService consumoService;
	
	@Autowired
	public ConsumoController(ConsumoService applicationService) {
		this.consumoService = applicationService;
	}
	
	@PostMapping(value = "/consumo", produces = "application/x-www-form-urlencoded")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> create(
			@RequestParam(value = "dados", required = false) String dados) {
		try {
			consumoService.salva(dados);
			return new ResponseEntity<>("ok", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("OPS", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/data-hora", produces = "text/plain")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> dataHora() {
		var dataHora = new Date();
		return new ResponseEntity<>(String.valueOf(dataHora.getTime()), HttpStatus.OK);
	}
}