package com.quartet.inventorydemo.dto;

import java.util.Date;

public class RequisitionDTO {
  private String login;
  private String status;
  private Date creationDate;
  private Date dueDate;
  private String description;

  public RequisitionDTO(
      String login, String status, Date creationDate, Date dueDate, String description) {
    this.login = login;
    this.status = status;
    this.creationDate = creationDate;
    this.dueDate = dueDate;
    this.description = description;
  }

  private RequisitionDTO() {}

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
}
