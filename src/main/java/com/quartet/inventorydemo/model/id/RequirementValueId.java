package com.quartet.inventorydemo.model.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class RequirementValueId implements Serializable {

  private UUID requirementId;
  private UUID inventoryPositionId;

  private RequirementValueId() {
  }

  public RequirementValueId(@NotNull UUID requirementId, @NotNull UUID inventoryPositionId) {
    this.requirementId = requirementId;
    this.inventoryPositionId = inventoryPositionId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(requirementId, inventoryPositionId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof RequirementValueId)) {
      return false;
    }
    RequirementValueId that = (RequirementValueId) obj;
    return requirementId.equals(that.requirementId)
        && inventoryPositionId.equals(that.inventoryPositionId);
  }
}
