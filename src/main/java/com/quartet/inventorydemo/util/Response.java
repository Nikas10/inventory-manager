package com.quartet.inventorydemo.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {

  public static ResponseEntity<?> createErrorResponse(HttpStatus httpStatus, String message) {
    return new ResponseEntity<>(
        new RespError(message, Long.parseLong("" + httpStatus.value())), httpStatus);
  }

  public static ResponseEntity<?> createResponse(Object object) {
    return new ResponseEntity<>(object, HttpStatus.OK);
  }

  public static ResponseEntity<?> createResponse() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
