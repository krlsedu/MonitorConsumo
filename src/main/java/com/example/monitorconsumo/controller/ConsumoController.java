package com.example.monitorconsumo.controller;

import com.example.monitorconsumo.service.ConsumoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class ConsumoController {
	
	private final ConsumoService consumoService;
	
	@Autowired
	public ConsumoController(ConsumoService applicationService) {
		this.consumoService = applicationService;
	}
	
	//@PostMapping(value = "/consumo", produces = "application/json")
	@PostMapping(value = "/consumo", produces = "application/x-www-form-urlencoded")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> create(
			@RequestParam(value = "dados", required = false) String dados) {
		consumoService.salva(dados);
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}