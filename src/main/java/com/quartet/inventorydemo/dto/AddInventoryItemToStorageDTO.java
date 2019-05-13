package com.quartet.inventorydemo.dto;

public class AddInventoryItemToStorageDTO {
  Integer amount;

  public AddInventoryItemToStorageDTO() {
  }

  public AddInventoryItemToStorageDTO(Integer amount) {
    this.amount = amount;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}
