package com.quartet.inventorydemo.exception;

public class DeletionNotSupportedException extends RuntimeException {
    public DeletionNotSupportedException() {
        super();
    }

    public DeletionNotSupportedException(String message) {
        super(message);
    }

    public DeletionNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletionNotSupportedException(Throwable cause) {
        super(cause);
    }
}
