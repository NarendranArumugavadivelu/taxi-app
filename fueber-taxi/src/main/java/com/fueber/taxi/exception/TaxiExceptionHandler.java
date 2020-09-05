package com.fueber.taxi.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fueber.taxi.vo.ErrorVO;

@ControllerAdvice
public class TaxiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorVO> handleExerciseServiceException(MethodArgumentNotValidException methodArgumentNotValidException) {
		List<ObjectError> errors = methodArgumentNotValidException.getBindingResult().getAllErrors();
		StringBuilder errorMessage = new StringBuilder();
		for(ObjectError error : errors) {
			errorMessage.append(error.getDefaultMessage()).append(",");
		}
		ErrorVO errorVO = new ErrorVO();
		errorVO.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		errorVO.setErrorMessage(errorMessage.substring(0, errorMessage.lastIndexOf(",")));
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(errorVO, httpStatus);
	}
	
	@ExceptionHandler(TaxiServiceException.class)
	@ResponseBody
	public ResponseEntity<ErrorVO> handleExerciseServiceException(TaxiServiceException taxiServiceException) {
		ErrorVO errorVO = taxiServiceException.getErrorVO();
		HttpStatus httpStatus = getHTTPStatus(errorVO);
		return new ResponseEntity<>(errorVO, httpStatus);
		
	}
	
	private HttpStatus getHTTPStatus(ErrorVO errorVO) {
		HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
		if(errorVO.getErrorCode().startsWith("400")) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}
		return httpStatus;
	}
}
