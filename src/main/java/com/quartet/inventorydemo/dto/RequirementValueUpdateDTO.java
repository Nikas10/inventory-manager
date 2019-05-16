package com.quartet.inventorydemo.dto;

public class RequirementValueUpdateDTO {

  private String requirementValue;

  public RequirementValueUpdateDTO(String requirementValue) {
    this.requirementValue = requirementValue;
  }

  public RequirementValueUpdateDTO() {
  }

  public String getRequirementValue() {
    return requirementValue;
  }

  public void setRequirementValue(String requirementValue) {
    this.requirementValue = requirementValue;
  }
}
