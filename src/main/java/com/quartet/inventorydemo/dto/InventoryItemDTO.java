package com.quartet.inventorydemo.dto;

public class InventoryItemDTO {

  private String holder;
  private String name;
  private String status;
  private Integer amount;

  private InventoryItemDTO() {
  }

  public InventoryItemDTO(String holder, String name, String status,
      Integer amount) {
    this.holder = holder;
    this.name = name;
    this.status = status;
    this.amount = amount;
  }

  public String getHolder() {
    return holder;
  }

  public void setHolder(String holder) {
    this.holder = holder;
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
