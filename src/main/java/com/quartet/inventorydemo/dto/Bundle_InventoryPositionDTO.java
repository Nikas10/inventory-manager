package com.quartet.inventorydemo.dto;

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
