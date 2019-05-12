package com.quartet.inventorydemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class Bundle_InventoryPositionDTO {

  private int amount;

  private Bundle_InventoryPositionDTO() {
  }

  public int getAmount() {
    return amount;
  }
}
