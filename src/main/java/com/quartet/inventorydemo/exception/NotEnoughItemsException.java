package com.quartet.inventorydemo.exception;

public class NotEnoughItemsException extends RuntimeException {

  public NotEnoughItemsException() {
  }

  public NotEnoughItemsException(String message) {
    super(message);
  }

  public NotEnoughItemsException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotEnoughItemsException(Throwable cause) {
    super(cause);
  }
}
