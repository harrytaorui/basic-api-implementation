package com.thoughtworks.rslist.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

	Logger logger = LogManager.getLogger(ExceptionHandler.class);

	@org.springframework.web.bind.annotation.ExceptionHandler({
			MyException.class})
	public ResponseEntity<CommonError> handleIndexOutOfException(Exception ex) {
		CommonError commonError = new CommonError();

		if (ex instanceof MyException) {
			commonError.setError(((MyException) ex).getErrorMsg());
		}
		logger.error(commonError.getError());
		return ResponseEntity.badRequest().body(commonError);
	}
}
