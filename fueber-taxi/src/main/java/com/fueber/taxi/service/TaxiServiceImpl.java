package com.fueber.taxi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fueber.taxi.dto.TaxiDTO;
import com.fueber.taxi.vo.TaxiVO;

@Service
public class TaxiServiceImpl implements TaxiService {
	
	private List<TaxiDTO> availableTaxiList;
	
	public TaxiServiceImpl(List<TaxiDTO> availableTaxiList) {
		this.availableTaxiList = availableTaxiList;
	}

	@Override
	public List<TaxiVO> getTaxiList() {
		List<TaxiVO> taxiVOs = new ArrayList<>();
		availableTaxiList.forEach(taxiDTO -> {
			if(!taxiDTO.isAssignedToCustomer()) {
				TaxiVO taxiVO = new TaxiVO();
				taxiVO.setTaxiID(taxiDTO.getTaxiID());
				taxiVO.setLatitude(taxiDTO.getLatitude());
				taxiVO.setLongitude(taxiDTO.getLongitude());
				taxiVO.setPinkTaxi(taxiDTO.isPinkTaxi());
				taxiVOs.add(taxiVO);
			}
		});
		return taxiVOs;
	}

}
