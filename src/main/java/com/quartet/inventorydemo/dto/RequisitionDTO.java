package com.quartet.inventorydemo.dto;

import java.util.Date;
import java.util.List;

public class RequisitionDTO {

  private String login;
  private String status;
  private Date creationDate;
  private Date dueDate;
  private String description;
  private String stringHolderUUID;
  private List<String> stringInventoryPositionUUIDs;

  public RequisitionDTO(String login, String status, Date creationDate, Date dueDate,
      String description, String stringHolderUUID,
      List<String> stringInventoryPositionUUIDs) {
    this.login = login;
    this.status = status;
    this.creationDate = creationDate;
    this.dueDate = dueDate;
    this.description = description;
    this.stringHolderUUID = stringHolderUUID;
    this.stringInventoryPositionUUIDs = stringInventoryPositionUUIDs;
  }

  private RequisitionDTO() {
  }

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

  public String getStringHolderUUID() {
    return stringHolderUUID;
  }

  public void setStringHolderUUID(String stringHolderUUID) {
    this.stringHolderUUID = stringHolderUUID;
  }

  public List<String> getStringInventoryPositionUUIDs() {
    return stringInventoryPositionUUIDs;
  }

  public void setStringInventoryPositionUUIDs(
      List<String> stringInventoryPositionUUIDs) {
    this.stringInventoryPositionUUIDs = stringInventoryPositionUUIDs;
  }
}
