package com.quartet.inventorydemo.dto;

import java.util.UUID;

public class RequirementValueDTO {
  String value;
  String name;
  UUID requirementId;

  public RequirementValueDTO() {
  }

  public RequirementValueDTO(String value, String name, UUID requirementId) {
    this.value = value;
    this.name = name;
    this.requirementId = requirementId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getRequirementId() {
    return requirementId;
  }

  public void setRequirementId(UUID requirementId) {
    this.requirementId = requirementId;
  }
}
