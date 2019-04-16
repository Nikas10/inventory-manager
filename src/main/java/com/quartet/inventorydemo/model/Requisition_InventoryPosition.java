package com.quartet.inventorydemo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Entity
@Data
@Table(name = "requisition_inventory_position", schema = "public")
public class Requisition_InventoryPosition implements Serializable {

    public Requisition_InventoryPosition() {
    }

    @Id
    @ApiModelProperty(position = 1, required = true, notes = "Request")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Requisition requisition;

    @Id
    @ApiModelProperty(position = 2, required = true, notes = "Inventory position")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;


    @ApiModelProperty(position = 3, required = true, notes = "Inventory amount")
    @NotNull
    @Column(name = "amount")
    private Integer amount;

    public void increaseAmount(Integer value) {
        this.amount += value;
    }
}
