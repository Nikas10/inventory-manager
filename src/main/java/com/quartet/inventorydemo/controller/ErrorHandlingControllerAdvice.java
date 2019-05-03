package com.quartet.inventorydemo.controller;

import com.quartet.inventorydemo.dto.ValidationErrorResponse;
import com.quartet.inventorydemo.dto.Violation;
import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.NotBundleException;
import com.quartet.inventorydemo.exception.NotEnoughItemsException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.exception.UpdateNotSupportedException;
import java.util.NoSuchElementException;
import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (ConstraintViolation violation : e.getConstraintViolations()) {
      error
          .getViolations()
          .add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      error
          .getViolations()
          .add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    return error;
  }

  @ExceptionHandler(OptimisticLockException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  String onConcurrentChangingSameEntity(OptimisticLockException e) {
    return "Changes from a side were occurred. Please retry again";
  }

  @ExceptionHandler({ResourceNotFoundException.class, NoSuchElementException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  String onMissingEntity(RuntimeException e) {
    return e.getMessage();
  }

  @ExceptionHandler(DeletionNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  String onUnsupportedDeletion(DeletionNotSupportedException e) {
    return e.getMessage();
  }

  @ExceptionHandler(UpdateNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  String onUnsupportedUpdate(UpdateNotSupportedException e) {
    return e.getMessage();
  }

  @ExceptionHandler({ResourceAlreadyExistsException.class, NotBundleException.class,
      NotEnoughItemsException.class})
  @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
  @ResponseBody
  String onResourceExistance(RuntimeException e) {
    return e.getMessage();
  }
}
