package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.model.id.RequirInvPosID;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity @IdClass(RequirInvPosID.class)
@Data
@Table(name = "requirement_inventory_position", schema = "public")
public class Requirement_InventoryPosition implements Serializable {

    @Column(name = "value")
    private String value;
    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Requirement requirement;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InventoryPosition inventoryPosition;

    public Requirement_InventoryPosition() {
    }

    public Requirement_InventoryPosition(Requirement requirement, InventoryPosition inventoryPosition, String value) {
        this.value = value;
        this.requirement = requirement;
        this.inventoryPosition = inventoryPosition;
    }

    @Override
    public boolean equals(Object requirement) {
        if (!this.getClass().equals(requirement.getClass())) {
            return false;
        }

        if (!this.value.equals( ((Requirement_InventoryPosition)requirement).value )) {
            return false;
        } else if (!this.requirement.equals( ((Requirement_InventoryPosition)requirement).requirement )) {
            return false;
        } else if (!this.inventoryPosition.equals( ((Requirement_InventoryPosition)requirement).inventoryPosition )) {
            return false;
        }

        return true;
    }

}
