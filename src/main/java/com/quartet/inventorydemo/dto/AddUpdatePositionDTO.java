package com.quartet.inventorydemo.dto;

public class AddUpdatePositionDTO {
  String id;
  Integer amount;
  String name;

  public AddUpdatePositionDTO() {
  }

  public AddUpdatePositionDTO(String id, Integer amount, String name) {
    this.id = id;
    this.amount = amount;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
