package com.fueber.taxi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fueber.taxi.exception.TaxiServiceException;
import com.fueber.taxi.service.TaxiBookingService;
import com.fueber.taxi.vo.CustomerVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping(path = "/taxi")
public class TaxiBookingController {
	
	@Autowired
	private TaxiBookingService taxiBookingService;

	@PostMapping(path = "/book/ride", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Book a taxi ride for a customer", description = "Persist a taxi ride for customer and provide taxi details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Taxi booked successfully", content = @Content(schema = @Schema(implementation = CustomerVO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = CustomerVO.class)))
	})
	public @ResponseBody CustomerVO bookTaxiRide(@Valid @RequestBody CustomerVO customerVO) throws TaxiServiceException {
		return taxiBookingService.bookTaxi(customerVO);
	}
}
