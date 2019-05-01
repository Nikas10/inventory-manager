package com.quartet.inventorydemo.util;

import java.util.Calendar;
import lombok.Data;

@Data
public class RespError {

  String message;
  Long time;
  Long code;

  public RespError(String message, Long code) {
    this.message = message;
    this.time = Calendar.getInstance().getTimeInMillis();
    this.code = code;
  }
}
