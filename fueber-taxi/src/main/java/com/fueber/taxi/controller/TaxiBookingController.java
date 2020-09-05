package com.fueber.taxi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fueber.taxi.exception.TaxiServiceException;
import com.fueber.taxi.service.TaxiBookingService;
import com.fueber.taxi.vo.CustomerVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping(path = "/ride")
public class TaxiBookingController {
	
	@Autowired
	private TaxiBookingService taxiBookingService;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Book a taxi ride for a customer", description = "Persist a taxi ride for customer and provide taxi details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Taxi booked successfully", content = @Content(schema = @Schema(implementation = CustomerVO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = CustomerVO.class)))
	})
	public @ResponseBody CustomerVO bookTaxiRide(@Valid @RequestBody CustomerVO customerVO) throws TaxiServiceException {
		return taxiBookingService.bookRide(customerVO);
	}
	
	@PutMapping(path = "/{bookingId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Updating the ride with any one of the following statuses started, canceled or completed", 
			   description = "Update an already persisted ride for the customer with the following statuses started, canceled or completed",
			   parameters = @Parameter(name = "bookingId", in = ParameterIn.PATH, description = "Booking id of the ride to update", required = true, 
			   schema = @Schema(type = "string", format = "uuid")))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Ride updated sucessfully", content = @Content(schema = @Schema(implementation = CustomerVO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input",  content = @Content(schema = @Schema(implementation = CustomerVO.class)))
	})
	public @ResponseBody CustomerVO updateRide(@Valid @RequestBody CustomerVO customerVO, @PathVariable(required = true) String bookingId) throws TaxiServiceException {
		return taxiBookingService.updateRide(customerVO, bookingId);
	}
}
