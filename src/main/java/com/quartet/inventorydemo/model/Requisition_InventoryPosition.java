package com.quartet.inventorydemo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@ApiModel(description = "This entity/form represents what and how much of inventory positions connected to requisition")
@Entity(name = "Requisition_InventoryPosition")
@Table(name = "quartet_requisition__quartet_inventory_position", schema = "public")
public class Requisition_InventoryPosition extends History implements Serializable {

    @ApiModelProperty(hidden = true)
    @NotNull(message = "Inventory position must be not null")
    @Id
    @JoinColumn(name = "inventory_position_id", nullable = false)
    @ManyToOne(optional = false)
    private InventoryPosition inventoryPosition;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "Requisition must be not null")
    @Id
    @JoinColumn(name = "requisition_id", nullable = false)
    @ManyToOne(optional = false)
    private Requisition requisition;

    @ApiModelProperty(position = 1, required = true, notes = "Inventory amount")
    @Positive(message = "Amount must be more than 0")
    @Column(name = "amount", nullable = false)
    private Integer amount;

    private Requisition_InventoryPosition() {
    }

    public Requisition_InventoryPosition(@NotNull(message = "Inventory position must be not null") InventoryPosition inventoryPosition,
                                         @NotNull(message = "Requisition must be not null") Requisition requisition,
                                         @Positive(message = "Amount must be more than 0") Integer amount) {
        this.inventoryPosition = inventoryPosition;
        this.requisition = requisition;
        this.amount = amount;
    }

    public InventoryPosition getInventoryPosition() {
        return inventoryPosition;
    }

    public void setInventoryPosition(@NotNull(message = "Inventory position must be not null") InventoryPosition inventoryPosition) {
        this.inventoryPosition = inventoryPosition;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public void setRequisition(@NotNull(message = "Requisition must be not null") Requisition requisition) {
        this.requisition = requisition;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(@Positive(message = "Amount must be more than 0") Integer amount) {
        this.amount = amount;
    }
}
