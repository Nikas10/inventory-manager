package com.quartet.inventorydemo.dto;

public class AddUpdatePositionDTO {
  String id;
  Integer amount;
  String name;
  String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AddUpdatePositionDTO() {
  }

  public AddUpdatePositionDTO(String id, Integer amount, String name) {
    this.id = id;
    this.amount = amount;
  }

  public AddUpdatePositionDTO(String id, Integer amount, String name, String description) {
    this.id = id;
    this.amount = amount;
    this.name = name;
    this.description = description;
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
