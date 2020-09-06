package com.fueber.taxi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fueber.taxi.vo.TaxiVO;

@Service
public interface TaxiService {

	public List<TaxiVO> getTaxiList();
}
