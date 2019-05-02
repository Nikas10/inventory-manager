package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.model.id.RequirementValueId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@ApiModel(
    description =
        "This entity/form represents value of property(requirement) for current inventory position")
@Entity(name = "RequirementValue")
@Table(name = "quartet_requirement_value", schema = "public")
public class RequirementValue extends History implements Serializable {

  @EmbeddedId
  private RequirementValueId requirementValueId = new RequirementValueId();

  @ApiModelProperty(hidden = true)
  @NotNull(message = "Requirement must be not null")
  @JoinColumn(name = "requirement_id", nullable = false)
  @MapsId("requirementId")
  @ManyToOne(optional = false)
  private Requirement requirement;

  @ApiModelProperty(hidden = true)
  @NotNull(message = "Inventory position must be not null")
  @JoinColumn(name = "inventory_position_id", nullable = false)
  @MapsId("inventoryPositionId")
  @ManyToOne(optional = false)
  private InventoryPosition inventoryPosition;

  @ApiModelProperty(position = 1, required = true, notes = "Value of property")
  @NotNull(message = "Value must be not null")
  @Column(name = "value", nullable = false)
  private String value;

  private RequirementValue() {
  }

  public RequirementValue(
      @NotNull(message = "Requirement must be not null") Requirement requirement,
      @NotNull(message = "Inventory position must be not null") InventoryPosition inventoryPosition,
      @NotNull(message = "Value must be not null") String value) {
    this.requirement = requirement;
    this.inventoryPosition = inventoryPosition;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RequirementValue)) {
      return false;
    }
    RequirementValue that = (RequirementValue) o;
    return requirement.equals(that.requirement)
        && inventoryPosition.equals(that.inventoryPosition)
        && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requirement, inventoryPosition, value);
  }

  public Requirement getRequirement() {
    return requirement;
  }

  public void setRequirement(
      @NotNull(message = "Requirement must be not null") Requirement requirement) {
    this.requirement = requirement;
  }

  public InventoryPosition getInventoryPosition() {
    return inventoryPosition;
  }

  public void setInventoryPosition(
      @NotNull(message = "Inventory position must be not null")
          InventoryPosition inventoryPosition) {
    this.inventoryPosition = inventoryPosition;
  }

  public String getValue() {
    return value;
  }

  public void setValue(@NotNull(message = "Value must be not null") String value) {
    this.value = value;
  }
}
