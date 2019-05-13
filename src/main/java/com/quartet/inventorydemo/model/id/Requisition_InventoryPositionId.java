package com.quartet.inventorydemo.model.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Requisition_InventoryPositionId implements Serializable {

  private UUID inventoryPositionId;
  private UUID requisitionId;

  public Requisition_InventoryPositionId() {
  }

  public Requisition_InventoryPositionId(
      @NotNull UUID inventoryPositionId, @NotNull UUID requisitionId) {
    this.inventoryPositionId = inventoryPositionId;
    this.requisitionId = requisitionId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(inventoryPositionId, requisitionId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Requisition_InventoryPositionId)) {
      return false;
    }
    Requisition_InventoryPositionId that = (Requisition_InventoryPositionId) o;
    return inventoryPositionId.equals(that.inventoryPositionId)
        && requisitionId.equals(that.requisitionId);
  }
}
