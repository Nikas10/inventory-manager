package com.quartet.inventorydemo.dto;

public class InventoryItemDTO {

  private String holderName;
  private String positionName;
  private String status;
  private Integer amount;

  private InventoryItemDTO() {
  }

  public InventoryItemDTO(String holderName, String positionName, String status,
      Integer amount) {
    this.holderName = holderName;
    this.positionName = positionName;
    this.status = status;
    this.amount = amount;
  }

  public String getHolderName() {
    return holderName;
  }

  public void setHolderName(String holderName) {
    this.holderName = holderName;
  }

  public String getPositionName() {
    return positionName;
  }

  public void setPositionName(String positionName) {
    this.positionName = positionName;
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
