package com.fueber.taxi.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fueber.taxi.dto.CustomerDTO;
import com.fueber.taxi.dto.TaxiDTO;

@Configuration
public class FueberTaxiConfiguration {

	@Bean(name = "availableTaxiList")
	public List<TaxiDTO> createTaxiList() {
		List<TaxiDTO> taxiDTOs = new ArrayList<>();
		taxiDTOs.add(new TaxiDTO("KA04MG4516", 12.979270, 77.571680, false, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4517", 12.914490, 77.666512, false, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4518", 13.028530, 77.491821, false, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4519", 12.931560, 77.625557, false, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4520", 12.961580, 77.596779, false, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4521", 12.903688, 77.624169, true, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4522", 12.844630, 77.659568, true, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4523", 12.952399, 77.576364, true, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4524", 12.925475, 77.546427, true, false));
		taxiDTOs.add(new TaxiDTO("KA04MG4525", 13.016648, 77.703385, true, false));
		return taxiDTOs;
	}
	
	@Bean(name = "bookedCustomerList")
	public List<CustomerDTO> createOnRideCustomerList() {
		return new ArrayList<>();
	}
}
