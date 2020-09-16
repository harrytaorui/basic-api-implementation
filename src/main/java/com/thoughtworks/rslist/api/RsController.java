package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.exceptions.CommonError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RsController {



  @ExceptionHandler(IndexOutOfBoundsException.class)
  private ResponseEntity<CommonError> handleIndexOutOfBoundsException(IndexOutOfBoundsException exception) {
    CommonError commonError = new CommonError();
    commonError.setError("invalid request param");
    return ResponseEntity.badRequest().body(commonError);
  }
}
