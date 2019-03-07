package com.quartet.inventorydemo.util;

import lombok.Data;

import java.util.Calendar;


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