package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.model.id.Bundle_InventoryPositionId;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ApiModel(description = "This entity/form represents inventory position inside bundle")
@Entity(name = "Bundle_InventoryPosition")
@Table(name = "quartet_bundle_position__quartet_inventory_position", schema = "public")
public class Bundle_InventoryPosition extends History implements Serializable {

  @EmbeddedId private Bundle_InventoryPositionId bundle_inventoryPositionId;

  @NotNull(message = "Inventory position can not be null")
  @JoinColumn(name = "inventory_position_id", nullable = false)
  @MapsId("inventoryPositionId")
  @ManyToOne(
      optional = false,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private InventoryPosition inventoryPosition;

  @NotNull(message = "Bundle position can not be null")
  @JoinColumn(name = "bundle_position_id", nullable = false)
  @MapsId("bundleId")
  @ManyToOne(
      optional = false,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private InventoryPosition bundlePosition;

  @Positive(message = "Amount must be more than 0")
  @Column(name = "amount", nullable = false)
  private Integer amount;

  private Bundle_InventoryPosition() {}

  public Bundle_InventoryPosition(
      @NotNull(message = "Inventory position can not be null") InventoryPosition inventoryPosition,
      @NotNull(message = "Bundle position can not be null") InventoryPosition bundlePosition,
      @Positive(message = "Amount must be more than 0") Integer amount) {
    this.inventoryPosition = inventoryPosition;
    this.bundlePosition = bundlePosition;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Bundle_InventoryPosition)) {
      return false;
    }
    Bundle_InventoryPosition that = (Bundle_InventoryPosition) o;
    return inventoryPosition.equals(that.inventoryPosition)
        && bundlePosition.equals(that.bundlePosition)
        && amount.equals(that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inventoryPosition, bundlePosition, amount);
  }

  public InventoryPosition getInventoryPosition() {
    return inventoryPosition;
  }

  public void setInventoryPosition(
      @NotNull(message = "Inventory position can not be null")
          InventoryPosition inventoryPosition) {
    this.inventoryPosition = inventoryPosition;
  }

  public InventoryPosition getBundlePosition() {
    return bundlePosition;
  }

  public void setBundlePosition(
      @NotNull(message = "Bundle position can not be null") InventoryPosition bundlePosition) {
    this.bundlePosition = bundlePosition;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(@Positive(message = "Amount must be more than 0") Integer amount) {
    this.amount = amount;
  }
}
