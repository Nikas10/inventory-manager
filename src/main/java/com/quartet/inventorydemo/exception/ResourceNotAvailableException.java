package com.quartet.inventorydemo.exception;

public class ResourceNotAvailableException extends RuntimeException {
  public ResourceNotAvailableException() {
    super();
  }

  public ResourceNotAvailableException(String message) {
    super(message);
  }

  public ResourceNotAvailableException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceNotAvailableException(Throwable cause) {
    super(cause);
  }
}
