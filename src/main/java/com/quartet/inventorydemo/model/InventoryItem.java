package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.model.id.InventoryItemId;
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
import javax.validation.constraints.Positive;

@ApiModel(
    description =
        "This entity/form represents fact of physical inventory objects have been given to profile (inventory holder).It contains information how much of items holder has and status of these items")
@Entity(name = "InventoryItem")
@Table(name = "quartet_inventory_item", schema = "public")
public class InventoryItem extends History implements Serializable {

  @EmbeddedId
  private InventoryItemId inventoryItemId;

  @ApiModelProperty(hidden = true)
  @NotNull(message = "Holder must be not null")
  @JoinColumn(name = "holder_id")
  @MapsId("holderId")
  @ManyToOne(optional = false)
  private Holder holder;

  @ApiModelProperty(hidden = true)
  @NotNull(message = "Inventory position must be not null")
  @JoinColumn(name = "inventory_position_id")
  @MapsId("inventoryPositionId")
  @ManyToOne(optional = false)
  private InventoryPosition inventoryPosition;

  @ApiModelProperty(position = 1, notes = "Email address")
  @NotNull(message = "Status must be not null")
  @Column(name = "status", nullable = false)
  private String status;

  @ApiModelProperty(position = 2, notes = "Email address")
  @Positive(message = "Amount must be more than 0")
  @Column(name = "amount", nullable = false)
  private Integer amount;

  private InventoryItem() {
  }

  public InventoryItem(
      @NotNull Holder holder,
      @NotNull InventoryPosition inventoryPosition,
      @NotNull String status,
      @Positive Integer amount) {
    this.holder = holder;
    this.inventoryPosition = inventoryPosition;
    this.status = status;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InventoryItem)) {
      return false;
    }
    InventoryItem that = (InventoryItem) o;
    return holder.equals(that.holder)
        && inventoryPosition.equals(that.inventoryPosition)
        && status.equals(that.status)
        && amount.equals(that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(holder, inventoryPosition, status, amount);
  }

  public Holder getHolder() {
    return holder;
  }

  public void setHolder(@NotNull Holder holder) {
    this.holder = holder;
  }

  public InventoryPosition getInventoryPosition() {
    return inventoryPosition;
  }

  public void setInventoryPosition(@NotNull InventoryPosition inventoryPosition) {
    this.inventoryPosition = inventoryPosition;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(@NotNull String status) {
    this.status = status;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(@Positive Integer amount) {
    this.amount = amount;
  }
}
