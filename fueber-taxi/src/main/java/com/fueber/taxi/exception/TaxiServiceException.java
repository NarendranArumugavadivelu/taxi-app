package com.fueber.taxi.exception;

import java.io.Serializable;

import com.fueber.taxi.vo.ErrorVO;

public class TaxiServiceException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 1055400178965046307L;
	
	private final ErrorVO errorVO;
	
	public TaxiServiceException(String message, ErrorVO errorVO) {
		super(message);
		this.errorVO = errorVO;
	}

	public ErrorVO getErrorVO() {
		return errorVO;
	}
}
