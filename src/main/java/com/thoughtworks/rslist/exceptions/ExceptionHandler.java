package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@ControllerAdvice
public class ExceptionHandler {
	@org.springframework.web.bind.annotation.ExceptionHandler({
			MyException.class})
	public ResponseEntity<CommonError> handleIndexOutOfException(Exception ex) {
		CommonError commonError = new CommonError();

		if (ex instanceof MyException) {
			commonError.setError(((MyException) ex).getErrorMsg());
		}
		Logger logger = LogManager.getLogger(ExceptionHandler.class);
		logger.error(commonError.getError());

		return ResponseEntity.badRequest().body(commonError);
	}
}
