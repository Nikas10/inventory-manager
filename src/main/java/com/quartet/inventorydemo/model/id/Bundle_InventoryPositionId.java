package com.quartet.inventorydemo.model.id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Bundle_InventoryPositionId implements Serializable {

  private UUID inventoryPositionId;
  private UUID bundleId;

  private Bundle_InventoryPositionId() {}

  public Bundle_InventoryPositionId(@NotNull UUID inventoryPosition, @NotNull UUID bundlePosition) {
    this.inventoryPositionId = inventoryPosition;
    this.bundleId = bundlePosition;
  }

  @Override
  public int hashCode() {
    return Objects.hash(bundleId, inventoryPositionId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Bundle_InventoryPositionId)) {
      return false;
    }
    Bundle_InventoryPositionId that = (Bundle_InventoryPositionId) obj;
    return inventoryPositionId.equals(that.inventoryPositionId) && bundleId.equals(that.bundleId);
  }
}
