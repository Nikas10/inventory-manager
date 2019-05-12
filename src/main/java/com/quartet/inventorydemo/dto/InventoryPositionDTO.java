package com.quartet.inventorydemo.dto;

public class InventoryPositionDTO {

  private String name;
  private String description;
  private Boolean bundle;

  InventoryPositionDTO(String name, String description, Boolean bundle) {
    this.name = name;
    this.description = description;
    this.bundle = bundle;
  }

  private InventoryPositionDTO() {
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getBundle() {return bundle;}
}
