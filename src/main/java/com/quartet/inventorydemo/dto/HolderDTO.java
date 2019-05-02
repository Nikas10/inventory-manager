package com.quartet.inventorydemo.dto;

public class HolderDTO {

  private String name;
  private String description;

  private HolderDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
