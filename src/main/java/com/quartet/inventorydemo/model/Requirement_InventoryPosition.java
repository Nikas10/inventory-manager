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
@Table(name = "requirement_inventory_position", schema = "public")
public class Requirement_InventoryPosition implements Serializable {

    @ApiModelProperty(position = 1, required = true, notes = "Amount of inventory in request")
    @NotNull
    @Column(name = "value")
    private String value;

    @Id
    @ApiModelProperty(position = 2, required = true, notes = "Requirement entity")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Requisition requirement;

    @Id
    @ApiModelProperty(position = 3, required = true, notes = "Inventory entity")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;

    public Requirement_InventoryPosition() {
    }
}
