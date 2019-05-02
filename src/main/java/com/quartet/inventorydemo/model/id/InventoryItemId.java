package com.quartet.inventorydemo.model.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class InventoryItemId implements Serializable {

  private UUID holderId;
  private UUID inventoryPositionId;

  private InventoryItemId() {}

  public InventoryItemId(@NotNull UUID holderId, @NotNull UUID inventoryPositionId) {
    this.holderId = holderId;
    this.inventoryPositionId = inventoryPositionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InventoryItemId)) {
      return false;
    }
    InventoryItemId that = (InventoryItemId) o;
    return holderId.equals(that.holderId) && inventoryPositionId.equals(that.inventoryPositionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(holderId, inventoryPositionId);
  }
}
