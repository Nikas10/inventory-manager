package com.quartet.inventorydemo.dto;

import java.util.Date;
import java.util.List;

public class RequisitionDTO {

  private String id;
  private String login;
  private String assignedTo;
  private String status;
  private Date creationDate;
  private Date dueDate;
  private String description;
  private String holderName;
  private String holderUUID;
  private List<RequisitionInventoryPositionDTO> inventoryPositions;



  public RequisitionDTO(String id, String login, String assignedTo, String status,
      Date creationDate, Date dueDate, String description, String holderName, String holderUUID,
      List<RequisitionInventoryPositionDTO> inventoryPositionNames) {
    this.id = id;
    this.assignedTo = assignedTo;
    this.login = login;
    this.status = status;
    this.creationDate = creationDate;
    this.dueDate = dueDate;
    this.description = description;
    this.holderName = holderName;
    this.holderUUID = holderUUID;
    this.inventoryPositions = inventoryPositionNames;
  }

  private RequisitionDTO() {
  }

  public void setId(String id) {this.id = id;}

  public String getId() {return id;}

  public String getAssignedTo() {return assignedTo;}

  public void setAssignedTo(String assignedTo) {this.assignedTo = assignedTo;}

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getStatus() {
    return status;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public String getDescription() {
    return description;
  }

  public String getHolderUUID() {
    return holderUUID;
  }

  public void setHolderUUID(String holderUUID) {
    this.holderUUID = holderUUID;
  }

  public String getHolderName() {return holderName;}

  public void setHolderName(String holderName) {this.holderName = holderName;}

  public List<RequisitionInventoryPositionDTO> getInventoryPositions() {
    return inventoryPositions;
  }

  public void setInventoryPositions(
      List<RequisitionInventoryPositionDTO> inventoryPositions) {
    this.inventoryPositions = inventoryPositions;
  }
}
