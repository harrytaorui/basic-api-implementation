package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
	@org.springframework.web.bind.annotation.ExceptionHandler({IndexOutOfBoundsException.class,
			NullPointerException.class,
			MethodArgumentNotValidException.class})
	public ResponseEntity<CommonError> handleIndexOutOfException(Exception ex) {
		CommonError commonError = new CommonError();
		if (ex instanceof MethodArgumentNotValidException) {
			commonError.setError("invalid param");
		}
		commonError.setError("invalid index");
		return ResponseEntity.badRequest().body(commonError);
	}
}
