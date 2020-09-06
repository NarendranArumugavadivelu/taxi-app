package com.fueber.taxi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fueber.taxi.service.TaxiService;
import com.fueber.taxi.vo.TaxiVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(path = "/taxi")
@CrossOrigin(origins = "*")
public class TaxiController {

	@Autowired
	private TaxiService taxiService;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Get the available taxi", description = "List the available taxi.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Available taxi listed", content = @Content(schema = @Schema(implementation = List.class)))})
	public @ResponseBody List<TaxiVO> getTaxi() {
		return taxiService.getTaxiList();
	}
}
