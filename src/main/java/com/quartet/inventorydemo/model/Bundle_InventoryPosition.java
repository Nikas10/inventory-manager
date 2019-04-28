package com.quartet.inventorydemo.model;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@ApiModel(description = "This entity/form represents inventory position inside bundle")
@Entity(name = "Bundle_InventoryPosition")
@Table(name = "quartet_bundle_position__quartet_inventory_position", schema = "public")
public class Bundle_InventoryPosition implements Serializable {

    @EmbeddedId
    private Bundle_InventoryPositionID bundle_InventoryPositionID;

    @NotNull(message = "Inventory position can not be null")
    @JoinColumn(name = "inventory_position_id", nullable = false)
    @MapsId("inventoryID")
    @ManyToOne(optional = false, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private InventoryPosition inventoryPosition;

    @NotNull(message = "Bundle position can not be null")
    @JoinColumn(name = "bundle_position_id", nullable = false)
    @MapsId("bundleID")
    @ManyToOne(optional = false, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private InventoryPosition bundlePosition;

    @Positive(message = "Amount must be more than 0")
    @Column(name = "amount", nullable = false)
    private Integer amount;

    private Bundle_InventoryPosition() {
    }

    public Bundle_InventoryPosition(@NotNull(message = "Inventory position can not be null") InventoryPosition inventoryPosition,
                                    @NotNull(message = "Bundle position can not be null") InventoryPosition bundlePosition,
                                    @Positive(message = "Amount must be more than 0") Integer amount) {
        this.inventoryPosition = inventoryPosition;
        this.bundlePosition = bundlePosition;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bundle_InventoryPosition)) return false;
        Bundle_InventoryPosition that = (Bundle_InventoryPosition) o;
        return inventoryPosition.equals(that.inventoryPosition) &&
                bundlePosition.equals(that.bundlePosition) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryPosition, bundlePosition, amount);
    }

    public InventoryPosition getInventoryPosition() {
        return inventoryPosition;
    }

    public void setInventoryPosition(@NotNull(message = "Inventory position can not be null") InventoryPosition inventoryPosition) {
        this.inventoryPosition = inventoryPosition;
    }

    public InventoryPosition getBundlePosition() {
        return bundlePosition;
    }

    public void setBundlePosition(@NotNull(message = "Bundle position can not be null") InventoryPosition bundlePosition) {
        this.bundlePosition = bundlePosition;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(@Positive(message = "Amount must be more than 0") Integer amount) {
        this.amount = amount;
    }

    @Embeddable
    public class Bundle_InventoryPositionID implements Serializable {

        private UUID inventoryID;
        private UUID bundleID;

        public Bundle_InventoryPositionID(@NotNull UUID inventoryPosition, @NotNull UUID bundlePosition) {
            this.inventoryID = inventoryPosition;
            this.bundleID = bundlePosition;
        }

        @Override
        public int hashCode() {
            return Objects.hash(bundleID, inventoryID);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Bundle_InventoryPosition.Bundle_InventoryPositionID)) return false;
            Bundle_InventoryPosition.Bundle_InventoryPositionID that = (Bundle_InventoryPosition.Bundle_InventoryPositionID) obj;
            return inventoryID.equals(that.inventoryID) &&
                    bundleID.equals(that.bundleID);
        }
    }
}
