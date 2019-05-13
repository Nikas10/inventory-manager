package com.quartet.inventorydemo.dto;

import java.util.UUID;

public class InventoryItemDTOFromHolder {

  private UUID id;
  private String name;
  private String status;
  private Integer amount;

  public InventoryItemDTOFromHolder() {
  }

  public InventoryItemDTOFromHolder(UUID id, String name, String status, Integer amount) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.amount = amount;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}
