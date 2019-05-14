package com.quartet.inventorydemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bundle_InventoryPositionDTO {

  private Integer amount;

  private Bundle_InventoryPositionDTO() {
  }

  public Bundle_InventoryPositionDTO(Integer amount) {
    this.amount = amount;
  }

  public Integer getAmount() {
    return amount;
  }
}
