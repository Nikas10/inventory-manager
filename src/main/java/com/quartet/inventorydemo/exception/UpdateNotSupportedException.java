package com.quartet.inventorydemo.exception;

public class UpdateNotSupportedException extends RuntimeException {

  public UpdateNotSupportedException() {
  }

  public UpdateNotSupportedException(String message) {
    super(message);
  }

  public UpdateNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UpdateNotSupportedException(Throwable cause) {
    super(cause);
  }
}
