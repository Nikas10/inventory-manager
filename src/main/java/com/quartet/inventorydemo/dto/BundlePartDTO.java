package com.quartet.inventorydemo.dto;

public class BundlePartDTO {

  String positionName;
  String stringPositionId;
  String amount;

  public BundlePartDTO() {
  }

  public BundlePartDTO(String positionName, String stringPositionId, String amount) {
    this.positionName = positionName;
    this.stringPositionId = stringPositionId;
    this.amount = amount;
  }

  public String getPositionName() {
    return positionName;
  }

  public void setPositionName(String positionName) {
    this.positionName = positionName;
  }

  public String getStringPositionId() {
    return stringPositionId;
  }

  public void setStringPositionId(String stringPositionId) {
    this.stringPositionId = stringPositionId;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }
}
