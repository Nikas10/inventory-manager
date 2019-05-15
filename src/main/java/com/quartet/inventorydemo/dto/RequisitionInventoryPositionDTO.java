package com.quartet.inventorydemo.dto;

/**
 * Represents data from Requisition_InventoryPosition table
 */
public class RequisitionInventoryPositionDTO {

  private String id;
  private String name;
  private String description;
  private Integer amount;

  public RequisitionInventoryPositionDTO() {}

  public RequisitionInventoryPositionDTO(String id, Integer amount, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.amount = amount;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}
