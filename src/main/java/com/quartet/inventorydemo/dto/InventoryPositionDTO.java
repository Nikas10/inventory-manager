package com.quartet.inventorydemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InventoryPositionDTO {
  private String name;
  private String description;
  private String isBundle;


  InventoryPositionDTO(String name, String description, String isBundle) {
    this.name = name;
    this.description = description;
    this.isBundle = isBundle;
  }

  private InventoryPositionDTO() {
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getIsBundle() {
    return isBundle;
  }
}
