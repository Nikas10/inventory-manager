package com.quartet.inventorydemo.exception;

public class NotBundleException extends RuntimeException {

  public NotBundleException() {
  }

  public NotBundleException(String message) {
    super(message);
  }

  public NotBundleException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotBundleException(Throwable cause) {
    super(cause);
  }
}
