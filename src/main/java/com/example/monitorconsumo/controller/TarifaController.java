package com.example.monitorconsumo.controller;

import com.example.monitorconsumo.dto.TarifaDTO;
import com.example.monitorconsumo.model.Tarifa;
import com.example.monitorconsumo.service.TarifaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class TarifaController {
	
	private final TarifaService tarifaService;
	
	@Autowired
	public TarifaController(TarifaService applicationService) {
		this.tarifaService = applicationService;
	}
	
	@PostMapping(value = "/tarifa", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Tarifa> create(@RequestBody final TarifaDTO tarifaDTO) {
		return new ResponseEntity<>(tarifaService.salva(tarifaDTO), HttpStatus.OK);
	}
	
	@GetMapping(value = "/tarifas", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<List<TarifaDTO>> get() {
		return new ResponseEntity<>(tarifaService.getTarifaList(), HttpStatus.OK);
	}
}