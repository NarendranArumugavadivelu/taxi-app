package com.fueber.taxi;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fueber.taxi.dto.CustomerDTO;
import com.fueber.taxi.dto.TaxiDTO;

@RunWith(MockitoJUnitRunner.class)
class FueberTaxiApplicationTests {
	
	private List<TaxiDTO> availableTaxiDTOList = new ArrayList<>();
	
	private List<CustomerDTO> customerDetailsList = new ArrayList<>();

	@BeforeEach
	public void initializeMockito() {
		MockitoAnnotations.initMocks(this);
		
		availableTaxiDTOList.add(new TaxiDTO("KA04MG4516", 12.979270, 77.571680, false, false));
	}
	
	
	
}
